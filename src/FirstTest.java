import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

public class FirstTest {
    private AppiumDriver driver;


    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformVersion", "6.0");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "Users/jrozental/Desktop/JavaAppiumAutomation/apks/org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void firstTest() {

        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find Search Wikipedia input",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by 'Java'",
                15
        );
    }

    @Test
    public void testCancelSearch() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search field",
                5

        );


        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel search",
                5
        );

        waitForElementNotPresent(
                By.id("org.wikipedia:id/search_close_btn"),
                "X is still present on the page",
                5
        );

    }

    @Test
    public void testCompareArticleTitle() {

        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find Search Wikipedia input",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find Search Wikipedia input",
                5
        );

        WebElement title_element = waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15

        );

        String article_title = title_element.getAttribute("text");

        Assert.assertEquals(
                "We see unexpected title!",
                "Java (programming language)",
                article_title
        );

    }

    @Test
    public void testSearchText() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find search field",
                5
        );

        WebElement search = waitForElementPresent(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find 'Search…' in search field",
                5
        );

        String search_text = search.getAttribute("text");

        Assert.assertEquals(
                "We see unexpected word!",
                "Search…",
                search_text
        );
    }

     @Test
    public void testNumberOfSearchResults() {
        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

       int countOfElements = waitForElementAndCount(
                By.id("org.wikipedia:id/page_list_item_container"),
                "Cannot find more than few results",
                5
        );

       if (countOfElements < 2)
       {
           Assert.fail("Count of articles was less than 2");
       }

        waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search field",
                5
        );


        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel search",
                5
        );

        waitForElementNotPresent(
                By.id("org.wikipedia:id/search_close_btn"),
                "X is still present on the page",
                5
        );
    }


     @Test
    public void testSearchWordIsPresent() {

         waitForElementAndClick(
                 By.id("org.wikipedia:id/search_container"),
                 "Cannot find 'Search Wikipedia' input",
                 5
         );

         waitForElementAndSendKeys(
                 By.xpath("//*[contains(@text, 'Search…')]"),
                 "Selenium",
                 "Cannot find search input",
                 5
         );

         boolean textFound =  waitForElementAndCheckText(
                 By.id("org.wikipedia:id/page_list_item_title"),
                 "Selenium",
                 "No search results",
                 5
         );

         Assert.assertTrue("Search result does not contain desired word",textFound);

    }

     @Test
    public void testSwipeArticleTitle() {

        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find Search Wikipedia input",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text, 'Search…')]"),
                "Appium",
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_title'][@text='Appium']"),
                "Cannot find 'Appium' in search",
                5
        );

        waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15

        );

        swipeUpToFindElement(
                By.xpath("//*[@text='View page in browser']"),
                "Test error message"
        );


    }


    private WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
    }


    private WebElement waitForElementPresent(By by, String error_message) {
        return waitForElementPresent(by, error_message, 5);
    }


    private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }


    private boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }


    private WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }


    private int waitForElementAndCount(By by, String error_message, long timeoutInSeconds)
    {
        waitForElementPresent(by,error_message,timeoutInSeconds);
        int count = driver.findElements(by).size();
        return count;
    }

    private boolean waitForElementAndCheckText(By by, String containsString, String error_message, long timeoutInSeconds)
    {
        List<WebElement> elements = driver.findElements(by);
        for (WebElement element: elements) {
            if (!element.getText().contains(containsString))
            {
                return false;
            }
        }
        return true;
    }

    protected void swipeUp(int timeOfSwipe)
    {
       TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int start_y = (int) (size.height * 0.8);
        int end_y = (int) (size.height * 0.2);

       action.press(x, start_y).waitAction(timeOfSwipe).moveTo(x, end_y).release().perform();

    }

    protected void swipeUpQuick()
    {
        swipeUp(200);
    }

    protected void swipeUpToFindElement(By by, String error_message)
    {
        while (driver.findElements(by).size() == 0){
            swipeUpQuick();
        }
    }



}


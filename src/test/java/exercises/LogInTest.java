package exercises;

import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.xml.dom.Tag;
import org.testng.ITestResult;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.JavascriptExecutor;


import pages.LogInPage;

import java.net.MalformedURLException;
import java.net.URL;
import java.lang.reflect.Method;


public class LogInTest  {
    protected WebDriver driver;
    @BeforeMethod
    public void setup(Method method) throws MalformedURLException {
        String sauceUsername = System.getenv("SAUCE_USERNAME");
        String sauceAccessKey = System.getenv("SAUCE_ACCESS_KEY");
        String methodName = method.getName();

        ChromeOptions chromeOpts = new ChromeOptions();
        chromeOpts.setExperimentalOption("w3c", true);

        MutableCapabilities sauceOpts = new MutableCapabilities();
        sauceOpts.setCapability("username", sauceUsername);
        sauceOpts.setCapability("accessKey", sauceAccessKey);
        sauceOpts.setCapability("name", methodName);
        sauceOpts.setCapability("seleniumVersion", "3.141.59");
        sauceOpts.setCapability("name", methodName);
        sauceOpts.setCapability("build", "saucecon19-best-practices");
        sauceOpts.setCapability("tags", "['best-practices', 'saucecon19']");

        MutableCapabilities caps = new MutableCapabilities();
        caps.setCapability(ChromeOptions.CAPABILITY,  chromeOpts);
        caps.setCapability("sauce:options", sauceOpts);
        caps.setCapability("browserName", "googlechrome");
        caps.setCapability("browserVersion", "71.0");
        caps.setCapability("platformName", "windows 10");

        String sauceUrl = "https://ondemand.saucelabs.com:443/wd/hub";
        URL url = new URL(sauceUrl);
        driver = new RemoteWebDriver(url, caps);
    }

    @Tag(name = "logInSuccessfully()")
    @Test
    /** Tests for a successful login **/
    public void logInSuccessfully(Method method) {
        LogInPage logInPage = LogInPage.visit(driver);
        logInPage.signInSuccessfully();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html");
    }

    @AfterMethod
    public void teardown(ITestResult result) {
        ((JavascriptExecutor)driver).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
        driver.quit();
    }
}

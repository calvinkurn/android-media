package com.tokopedia.tkpd;

import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.test.InstrumentationTestCase;
import android.view.KeyEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.util.Log;

import org.junit.Before;
import org.junit.runner.RunWith;

import java.io.File;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

// UiAutomator library test
/**
 * Created by normansyahputa on 3/23/18.
 */
@RunWith(AndroidJUnit4.class)
public class AutomatorTest {
    private UiDevice uiDevice;

    @Before
    public void setUp() {
        uiDevice = UiDevice.getInstance(getInstrumentation());
        uiDevice.pressHome();
    }

    @Test
    public void testCalculator() throws Exception {
        uiDevice.findObject(new UiSelector().descriptionContains("Apps")).clickAndWaitForNewWindow();

//        UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));
//        appViews.setAsHorizontalList();

        uiDevice.findObject(new UiSelector().descriptionContains("Calculator")).clickAndWaitForNewWindow();

//        UiObject calculatorApp = appViews.getChildByText(new UiSelector()
//                .className(android.widget.TextView.class.getName()), "Calculator");
//        calculatorApp.clickAndWaitForNewWindow();

        // Calculator app
        UiObject threeButton = uiDevice.findObject(new UiSelector().text("3"));
        threeButton.click();

        UiObject plusButton = uiDevice.findObject(new UiSelector().text("+"));
        plusButton.click();

        UiObject fiveButton = uiDevice.findObject(new UiSelector().text("5"));
        fiveButton.click();

        UiObject equalsButton = uiDevice.findObject(new UiSelector().text("="));
        equalsButton.click();

        UiObject display = uiDevice.findObject(new UiSelector()
                .resourceId("com.android.calculator2:id/display"));
        UiObject displayNumber = display.getChild(new UiSelector().index(0));

        assertEquals(displayNumber.getText(), "8");

        uiDevice.pressHome();
    }

    public void testBrowserApp() throws Exception {
        uiDevice.findObject(new UiSelector().descriptionContains("Apps")).clickAndWaitForNewWindow();

        UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));
        appViews.setAsHorizontalList();

        UiObject browserApp = appViews.getChildByText(new UiSelector()
                .className(android.widget.TextView.class.getName()), "Browser");
        browserApp.clickAndWaitForNewWindow();

        // Browser App set url
        UiObject urlForm = uiDevice.findObject(new UiSelector()
                .resourceId("com.android.browser:id/url"));
        urlForm.setText("http://www.google.cz");
        //uiDevice.pressKeyCode(KeyEvent.KEYCODE_ENTER);
        uiDevice.pressEnter();

        // Wait to load page
        SystemClock.sleep(10000);

        // Click on webview to lose focus from form
        UiObject webView = uiDevice.findObject(new UiSelector()
                .className("android.webkit.WebView"));
        webView.click();

        uiDevice.pressMenu();

        // Sleep to show the menu
        SystemClock.sleep(1000);
        UiObject refreshButton = uiDevice.findObject(new UiSelector()
                .text("Refresh"));
        refreshButton.click();
    }

    @Test
    public void test() throws InterruptedException, UiObjectNotFoundException {
        openApp("com.android.calculator2");

        // Calculator app
        UiObject threeButton = uiDevice.findObject(new UiSelector().text("3"));
        threeButton.click();

        UiObject plusButton = uiDevice.findObject(new UiSelector().text("+"));
        plusButton.click();

        UiObject fiveButton = uiDevice.findObject(new UiSelector().text("5"));
        fiveButton.click();

        UiObject equalsButton = uiDevice.findObject(new UiSelector().text("="));
        equalsButton.click();

        UiObject display = uiDevice.findObject(new UiSelector()
                .resourceId("com.android.calculator2:id/display"));
        UiObject displayNumber = display.getChild(new UiSelector().index(0));

        assertEquals(displayNumber.getText(), "8");

        uiDevice.pressHome();

        takeScreenshot("screenshot-1.png");

//        editText.setText("123456");
//        UiObject2 protectObject = waitForObject(By.text("Submit"));
//        protectObject.click();
//
//        takeScreenshot("screenshot-2.png");

        Thread.sleep(10000);
    }

    private void openApp(String packageName) {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private UiObject2 waitForObject(BySelector selector) throws InterruptedException {
        UiObject2 object = null;
        int timeout = 30000;
        int delay = 1000;
        long time = System.currentTimeMillis();
        while (object == null) {
            object = uiDevice.findObject(selector);
            Thread.sleep(delay);
            if (System.currentTimeMillis() - timeout > time) {
                fail();
            }
        }
        return object;
    }

    private void takeScreenshot(String name) {
        Log.d("TEST", "takeScreenshot");
        String dir = String.format("%s/%s", Environment.getExternalStorageDirectory().getPath(), "test-screenshots");
        File theDir = new File(dir);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
        uiDevice.takeScreenshot(new File(String.format("%s/%s", dir, name)));
    }
}

package com.tokopedia.tkpd;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
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
    public void test() throws InterruptedException, UiObjectNotFoundException {
        openApp("com.android.calculator2");

        // Calculator app
        UiObject threeButton = uiDevice.findObject(new UiSelector().text("3"));
        threeButton.click();

        UiObject plusButton = uiDevice.findObject(new UiSelector().resourceId("com.android.calculator2:id/op_add"));
        plusButton.click();

        UiObject fiveButton = uiDevice.findObject(new UiSelector().text("5"));
        fiveButton.click();

        UiObject equalsButton = uiDevice.findObject(new UiSelector().resourceId("com.android.calculator2:id/eq"));
        equalsButton.click();

        UiObject display = uiDevice.findObject(new UiSelector()
                .resourceId("com.android.calculator2:id/display"));
        UiObject displayNumber = display.getChild(new UiSelector().index(1));

        assertEquals(displayNumber.getText(), "8");

        uiDevice.pressHome();

        takeScreenshot("screenshot-1.png");

        Thread.sleep(1_000);
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

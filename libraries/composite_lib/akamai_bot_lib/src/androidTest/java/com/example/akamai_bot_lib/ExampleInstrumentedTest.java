package com.example.akamai_bot_lib;

import android.content.Context;
import android.content.res.Resources;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;


import com.tokopedia.akamai_bot_lib.UtilsKt;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext();

        assertEquals("com.tokopedia.akamai_bot_lib.test", appContext.getPackageName());
    }

    @Test
    public void regex() {

        Context context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext();
        Resources resources = context.getResources();

        String input = GraphqlHelper.loadRawString(resources, com.tokopedia.akamai_bot_lib.test.R.raw.mutation_af_register_username);
        assertTrue(UtilsKt.getMutation(input, "RegisterUsername"));
    }
}

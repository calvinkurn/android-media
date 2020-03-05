package com.example.akamai_bot_lib;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;


import com.tokopedia.akamai_bot_lib.UtilsKt;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

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

    @Test
    public void regex2() {

        Context context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext();
        Resources resources = context.getResources();

        String input = GraphqlHelper.loadRawString(resources, com.tokopedia.akamai_bot_lib.test.R.raw.mutation_af_register_username);
        List<String> any = UtilsKt.getAny(input);
        Log.d(this.getClass().getName(), any.toString());
        assertTrue(UtilsKt.getMutation(input, "RegisterUsername"));
        assertEquals(1,any.size());
        assertTrue(any.get(0).equalsIgnoreCase("bymeRegisterAffiliateName"));
    }

    @Test
    public void regex3() {

        Context context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().getContext();
        Resources resources = context.getResources();

        String input = GraphqlHelper.loadRawString(resources, com.tokopedia.akamai_bot_lib.test.R.raw.mutation_register);
        List<String> any = UtilsKt.getAny(input);
        Log.d(this.getClass().getName(), any.toString());
        assertEquals(1,any.size());
        assertTrue(any.get(0).equalsIgnoreCase("register"));
    }
}

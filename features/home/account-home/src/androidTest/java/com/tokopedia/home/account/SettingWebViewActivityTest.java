package com.tokopedia.home.account;

import android.app.Activity;
import android.app.Instrumentation;
import androidx.test.InstrumentationRegistry;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.util.Log;

import com.tokopedia.home.account.presentation.activity.SettingWebViewActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
@SmallTest
@RunWith(AndroidJUnit4.class)
public class SettingWebViewActivityTest {
    @Rule
    public ActivityTestRule<SettingWebViewActivity> mActivityRule =
            new ActivityTestRule<>(SettingWebViewActivity.class);

    private void setContentView(final int layoutId) throws Throwable {
        final Activity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(() -> activity.setContentView(layoutId));
    }

    @Test
    public void inflation() throws Throwable {

        Log.d("Test", "kakka");

    }

    private Activity getActivity() {
        return mActivityRule.getActivity();
    }

    private Instrumentation getInstrumentation() {
        return InstrumentationRegistry.getInstrumentation();
    }
}
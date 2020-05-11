package com.tokopedia.home.account;

import android.app.Activity;
import android.app.Instrumentation;
import android.util.Log;

import androidx.test.InstrumentationRegistry;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.tokopedia.home.account.presentation.activity.AccountSettingActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class AccountSettingActivityTest {
    @Rule
    public ActivityTestRule<AccountSettingActivity> mActivityRule =
            new ActivityTestRule<>(AccountSettingActivity.class);

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
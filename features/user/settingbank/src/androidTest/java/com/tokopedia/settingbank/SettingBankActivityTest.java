package com.tokopedia.settingbank;

import android.app.Activity;
import android.app.Instrumentation;
import android.util.Log;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.banklist.di.test.DaggerSettingBankTestComponent;
import com.banklist.di.test.SettingBankTestModule;
import com.google.gson.Gson;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.settingbank.R;
import com.tokopedia.settingbank.banklist.di.DaggerSettingBankComponent;
import com.tokopedia.settingbank.banklist.di.SettingBankComponent;
import com.tokopedia.settingbank.banklist.view.activity.DebugSettingBankActivity;
import com.tokopedia.settingbank.banklist.view.fragment.DebugSettingBankFragment;
import com.tokopedia.settingbank.util.FetchingIdlingResource;
import com.tokopedia.tkpd.BaseRetrofitJsonFactory;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class SettingBankActivityTest {
    @Rule
    public ActivityTestRule<DebugSettingBankActivity> mActivityRule =
            new ActivityTestRule<>(DebugSettingBankActivity.class, false, false);

    private void setContentView(final int layoutId) throws Throwable {
        final Activity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(() -> activity.setContentView(layoutId));
    }

    @Before
    public void setup() {
        mActivityRule.launchActivity(null);
        IdlingRegistry.getInstance().register(FetchingIdlingResource.INSTANCE.get());
    }

    @Test
    public void inflation() throws Throwable {

        Thread.sleep(10_000);

        DebugSettingBankFragment fragment = (DebugSettingBankFragment)
                mActivityRule.
                        getActivity().
                        getSupportFragmentManager().
                        findFragmentByTag("TAG_FRAGMENT");

        // wait until onresume
        androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().waitForIdleSync();

        BaseMainApplication application = (BaseMainApplication) getActivity().getApplication();

        DaggerSettingBankComponent
                .builder()
                .settingBankModule(new SettingBankTestModule())
                .baseAppComponent(application.getBaseAppComponent())
                .build();


        // Test mode
        SettingBankTestModule settingBankTestModule = new SettingBankTestModule();
        settingBankTestModule.getMockWebServer().enqueue(
                new BaseRetrofitJsonFactory(InstrumentationRegistry.getInstrumentation().getContext())
                        .createSuccess200Response("setting_bank.json")
        );

        SettingBankComponent settingBankComponent = DaggerSettingBankTestComponent
                .builder()
                .settingBankTestModule(settingBankTestModule)
                .baseAppComponent(application.getBaseAppComponent())
                .build();

        if (fragment != null) {

            fragment.reInitInjector(settingBankComponent);

            mActivityRule.getActivity().runOnUiThread(fragment::getBankList);

            Espresso.onView(ViewMatchers.withId(R.id.account_list_rv))
                    .inRoot(RootMatchers.withDecorView(
                            Matchers.is(mActivityRule.getActivity().getWindow().getDecorView())))
                    .check(matches(isDisplayed()));

            Log.d("Test", "kakka");
        }


    }

    private Activity getActivity() {
        return mActivityRule.getActivity();
    }

    private Instrumentation getInstrumentation() {
        return InstrumentationRegistry.getInstrumentation();
    }
}
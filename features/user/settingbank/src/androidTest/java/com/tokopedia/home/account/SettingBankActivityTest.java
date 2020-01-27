package com.tokopedia.home.account;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.banklist.di.test.DaggerSettingBankTestComponent;
import com.banklist.di.test.SettingBankTestComponent;
import com.banklist.di.test.SettingBankTestModule;
import com.google.gson.Gson;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.settingbank.R;
import com.tokopedia.settingbank.banklist.di.DaggerSettingBankComponent;
import com.tokopedia.settingbank.banklist.di.SettingBankComponent;
import com.tokopedia.settingbank.banklist.view.activity.SettingBankActivity;
import com.tokopedia.settingbank.banklist.view.fragment.SettingBankFragment;
import com.tokopedia.tkpd.BaseJsonFactory;
import com.tokopedia.tkpd.BaseRetrofitJsonFactory;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class SettingBankActivityTest {
    @Rule
    public ActivityTestRule<SettingBankActivity> mActivityRule =
            new ActivityTestRule<>(SettingBankActivity.class);

    private void setContentView(final int layoutId) throws Throwable {
        final Activity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(() -> activity.setContentView(layoutId));
    }

    @Test
    public void test(){
        Assert.assertEquals(
                //new JSONObject(A.INSTANCE.getTest2()).toString(),
                new Gson().toJson(A.INSTANCE.getTest2()),
                A.INSTANCE.getTest2().toString()

        );
    }

    @Test
    public void inflation() throws Throwable {

        Thread.sleep(10_000);

        SettingBankFragment fragment = (SettingBankFragment)
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
                new BaseRetrofitJsonFactory(InstrumentationRegistry.getContext())
                        .createSuccess200Response("setting_bank.json")
        );

        SettingBankComponent settingBankComponent = DaggerSettingBankTestComponent
                .builder()
                .settingBankTestModule(settingBankTestModule)
                .baseAppComponent(application.getBaseAppComponent())
                .build();

        fragment.reInitInjector(settingBankComponent);

        fragment.getBankList();

        Thread.sleep(2000);

        Espresso.onView(ViewMatchers.withId(R.id.account_list_rv))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(mActivityRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

        Log.d("Test", "kakka");

    }

    private Activity getActivity() {
        return mActivityRule.getActivity();
    }

    private Instrumentation getInstrumentation() {
        return InstrumentationRegistry.getInstrumentation();
    }
}
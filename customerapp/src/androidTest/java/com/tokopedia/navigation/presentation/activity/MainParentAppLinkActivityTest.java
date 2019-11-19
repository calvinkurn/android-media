package com.tokopedia.navigation.presentation.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.module.TestAppModule;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase;
import com.tokopedia.pms.payment.view.activity.PaymentListActivity;
import com.tokopedia.showcase.ShowCasePreference;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.rule.GuessTokopediaTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


/**
 * Created by normansyahputa on 26/7/18.
 */
@RunWith(AndroidJUnit4.class)
public class MainParentAppLinkActivityTest {

    private static final String Feed_TAG = "Feed";
    private static final String Inbox_TAG = "Inbox";
    private static final String Keranjang_TAG = "Keranjang";
    private static final String Akun_TAG = "Akun";
    private static final String Home_TAG = "Home";
    @Rule
    public GuessTokopediaTestRule<DeeplinkHandlerActivity> mDeepLinkRule = new GuessTokopediaTestRule<>(
            DeeplinkHandlerActivity.class, true, false, 3
    );
    @Inject
    HomePresenter homePresenter;

    GetDrawerNotificationUseCase getDrawerNotificationUseCase;
    private String jsons[] = {"feed_check_whitelist.json", "feed_query.json", "recent_product_views.json", "inbox_home.json"};
    private BaseAppComponent baseAppComponent;
    private TestAppModule testAppModule;

    @Before
    public void before() {
        BaseMainApplication application = (BaseMainApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        baseAppComponent = application.reinitBaseAppComponent(testAppModule = new TestAppModule(application));

        disableOnBoard(application);

        // disable showcase
        final String showCaseTag = MainParentActivity.class.getName() + ".bottomNavigation";
        ShowCasePreference.setShown(application, showCaseTag, true);
    }

    private boolean disableOnBoard(BaseMainApplication baseMainApplication) {
        SharedPreferences sharedPreferences = baseMainApplication.getSharedPreferences("LOGIN_SESSION", Context.MODE_PRIVATE);
        return sharedPreferences.edit().putBoolean("IS_FIRST_TIME_NEW_ONBOARDING", false).commit();
    }

    @After
    public void after() {
        // disable showcase
        final String showCaseTag = MainParentActivity.class.getName() + ".bottomNavigation";
        ShowCasePreference.setShown(baseAppComponent.getContext(), showCaseTag, false);


        baseAppComponent = null;
        testAppModule = null;
        TestAppModule.userSession = null;
    }

    @Test
    public void test_app_link() throws Exception {

        UserSession userSession = baseAppComponent.userSession();

        doReturn(false).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        prepareForFullSmartLockBundle();

        startDeepLinkActivity();

        onView(allOf(withText(Feed_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        when(userSession.isLoggedIn()).thenReturn(true);

        mockAlreadyLogin(userSession);

        onView(allOf(withText(Inbox_TAG), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());
    }

    @Test
    public void test_pms_app_link() throws Exception {

        UserSession userSession = baseAppComponent.userSession();

        doReturn(false).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();

        prepareForFullSmartLockBundle();

        startDeepLinkActivity(ApplinkConst.PMS);

        intended(hasComponent(PaymentListActivity.class.getName()));
    }

    private void prepareForFullSmartLockBundle() {
        Intent resultData = new Intent();
        Bundle bundle = new Bundle();

        resultData.putExtras(bundle);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        intending(hasComponent(LoginActivity.class.getName())).respondWith(result);
    }

    private void mockAlreadyLogin(UserSession userSession) {
        doReturn(true).when(userSession).isLoggedIn();
        doReturn("1234").when(userSession).getUserId();
    }

    private void startDeepLinkActivity() {
        startDeepLinkActivity(ApplinkConst.HOME);
    }

    private void startDeepLinkActivity(String AppLinkConst) {
        Intent startIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppLinkConst));
        mDeepLinkRule.launchActivity(startIntent);
    }
}

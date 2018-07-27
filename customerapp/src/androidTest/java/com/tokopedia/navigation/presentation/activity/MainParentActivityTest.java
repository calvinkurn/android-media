package com.tokopedia.navigation.presentation.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.CacheUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.session.register.view.activity.SmartLockActivity;
import com.tokopedia.tkpd.ConsumerRouterApplication;
import com.tokopedia.tkpd.R;
import com.tokopedia.session.google.GoogleSignInActivity;
import com.tokopedia.session.register.view.activity.RegisterEmailActivity;
import com.tokopedia.session.register.view.presenter.RegisterEmailPresenterImpl;
import com.tokopedia.tkpd.model.ErrorMessageModel;
import com.tokopedia.tkpd.model.session.RegisterModel;
import com.tokopedia.tkpd.rule.GuessTokopediaTestRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.openLinkWithText;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.tokopedia.tkpd.Utils.allowPermissionsIfNeeded;
import static com.tokopedia.tkpd.Utils.childAtPosition;
import static com.tokopedia.tkpd.Utils.matchToolbarTitle;
import static com.tokopedia.tkpd.Utils.mockGoogleActivityResult;
import static com.tokopedia.tkpd.Utils.nthChildOf;
import static com.tokopedia.tkpd.Utils.setField;
import static com.tokopedia.tkpd.Utils.snackbarMatcher;
import static com.tokopedia.tkpd.rule.GuessTokopediaTestRule.SESSION_URL_ACCOUNTS_DOMAIN;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by normansyahputa on 26/7/18.
 */
@RunWith(AndroidJUnit4.class)
public class MainParentActivityTest {

    private UserSession userSession;

    @Rule
    public GuessTokopediaTestRule<MainParentActivity> mIntentsRule = new GuessTokopediaTestRule<>(
            MainParentActivity.class, true, false, 3
    );

    @Test
    public void testInitialUI() throws Exception {
        prepareForFullSmartLockBundle();

        startEmptyActivity();

        onView(allOf(withText("Feed"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        onView(withText("Feed masih kosong")).check(matches(isDisplayed()));

        when(userSession.isLoggedIn()).thenReturn(true);

        mockAlreadyLogin3();

        onView(allOf(withText("Inbox"), isDescendantOfA(withId(R.id.bottomnav)), isDisplayed())).perform(click());

        // TODO tap all nav bar button
        // TODO verify go to all locations
        // TODO tap toolbar then go back
        // TODO check checked nav bar
    }

    @Test
    public void testSluggishTap(){
        // TODO iterate 1000 times, and randomly tap navbar
    }

    @Test
    public void testSearchGoToDiscovery(){

    }

    @Test
    public void testTapGoToBarCode(){

    }

    private void prepareForFullSmartLockBundle() {
        Intent resultData = new Intent();
        Bundle bundle = new Bundle();
        String phoneNumber = "123-345-6789";
        bundle.putString("phone", phoneNumber);
        bundle.putString("username", "cincin.jati+47@tokopedia.com");
        bundle.putString("password", "optimus");

        resultData.putExtras(bundle);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        intending(hasComponent(LoginActivity.class.getName())).respondWith(result);
    }

    private void mockAlreadyLogin2(){
        mIntentsRule.getActivity().setUserSession(userSession);
    }

    private void mockAlreadyLogin3(){
        setField(mIntentsRule.getActivity().getClass(), mIntentsRule.getActivity(), "userSession", userSession);
    }

    private void mockAlreadyLogin(){
        Context applicationContext = InstrumentationRegistry.getTargetContext().getApplicationContext();
        if(applicationContext != null && applicationContext instanceof ConsumerRouterApplication){
            ConsumerRouterApplication routerApplication = (ConsumerRouterApplication) applicationContext;

            setField(ConsumerRouterApplication.class, routerApplication, "userSession", userSession);

        }
    }

    private Matcher<View> bottomNavViewMatcher(@IdRes final int id_) {
        return allOf(nthChildOf(
                withId(R.id.bottomnav),
                0), withId(id_));
    }

    private ViewInteraction bottomNavViewTextMatcher(Matcher<View> parent) {
        return onView(nthChildOf(nthChildOf(parent, 2), 0 ));
    }

    private void startEmptyActivity() {
        userSession = mock(UserSession.class);

        mIntentsRule.launchActivity(null);
    }
}

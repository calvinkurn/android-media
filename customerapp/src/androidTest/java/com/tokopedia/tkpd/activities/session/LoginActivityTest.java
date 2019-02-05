package com.tokopedia.tkpd.activities.session;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.webkit.WebView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.google.android.libraries.cloudtesting.screenshots.ScreenShotter;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.login.view.activity.LoginActivity;
import com.tokopedia.loginregister.login.view.fragment.LoginFragment;
import com.tokopedia.loginregister.loginthirdparty.facebook.GetFacebookCredentialSubscriber;
import com.tokopedia.loginregister.loginthirdparty.google.GoogleSignInActivity;
import com.tokopedia.loginregister.loginthirdparty.google.SmartLockActivity;
import com.tokopedia.loginregister.registerinitial.view.activity.RegisterInitialActivity;
import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.tkpd.WebViewIdlingResource;
import com.tokopedia.tkpd.activities.session.modules.TestSessionModule;
import com.tokopedia.tkpd.rule.GuessTokopediaTestRule;
import com.tokopedia.usecase.RequestParams;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.tokopedia.tkpd.Utils.matchToolbarTitle;
import static com.tokopedia.tkpd.Utils.mockGoogleActivityResult;
import static com.tokopedia.tkpd.Utils.nthChildOf;
import static com.tokopedia.tkpd.Utils.snackbarAnyMatcher;
import static com.tokopedia.tkpd.Utils.snackbarMatcher;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

/**
 * Created by normansyahputa on 3/21/18.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public GuessTokopediaTestRule<LoginActivity> mIntentsRule = new GuessTokopediaTestRule<LoginActivity>(
            LoginActivity.class, true, false, 3
    );

    private WebViewIdlingResource webViewIdlingResource;

    /**
     * 1. start the app
     * 2. because for some reason (it trigger another activity then it should avoid that.
     * put intent to do that
     *
     * @throws Exception just throw exception
     */
    @Test
    public void testEmailLogin() throws Exception {
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));

        startLoginActivity();

        onView(withId(R.id.email_auto))
                .check(matches(withText("noiz354@gmail.com")));

        Thread.sleep(3_000);

        onView(withId(R.id.accounts_sign_in))
                .check(matches(isEnabled()));

        Thread.sleep(3_000);

        onView(withId(R.id.email_auto))
                .perform(replaceText("noiz354@gmail.com"));

        Thread.sleep(3_000);

        onView(withId(R.id.password))
                .perform(replaceText("test1234"));

        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("info.json"));
        mIntentsRule.getIndexMockWebServer(1).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("make_login.json"));

        onView(withId(R.id.accounts_sign_in))
                .perform(click());
    }

    /**
     * test_id {"YH/003"}
     * Test yahoo login
     */
    @Test
    public void testYahooLogin() throws Exception {
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("info.json"));
        mIntentsRule.getIndexMockWebServer(1).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("make_login.json"));

        startLoginActivity();

        ViewInteraction loginTextView = onView(
                nthChildOf(
                        withId(R.id.login_buttons_container),
                        3));

        ScreenShotter.takeScreenshot("screenshot_btn_load_more", mIntentsRule.getActivity());

        UiObject plusButton = mIntentsRule.getDevice().findObject(new UiSelector().resourceId("com.tokopedia.tkpd:id/btn_load_more"));
        if (plusButton.exists())
            onView(withId(R.id.btn_load_more)).perform(click());

        Thread.sleep(3_000);

        loginTextView.perform(click());

        // necessary to make it wait.
        Thread.sleep(10000);

        ScreenShotter.takeScreenshot("testYahooReLogin_see_dialog", mIntentsRule.getActivity());

        // waiting all url to be finished
        DialogFragment dialog = (DialogFragment) mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        final WebView webview = dialog.getView().findViewById(com.tokopedia.core2.R.id.web_oauth);
        if (webview != null) {
            try {
                mIntentsRule.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webViewIdlingResource = new WebViewIdlingResource(webview);
                        Espresso.registerIdlingResources(webViewIdlingResource);
                    }
                });
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        // set target fragment bundle
        Bundle bundle = new Bundle();
        bundle.putString("server", "accounts.tokopedia.com");
        bundle.putString("path", "/mappauth/code");
        bundle.putString("NAME", "Yahoo");
        bundle.putString("code", "v");
        bundle.putString("state", "ad5d4603-7243-4e8e-85a2-d5ae4bcc8deb");
        Intent intent = new Intent();
        intent.putExtra("bundle", bundle);
        dialog.getTargetFragment().onActivityResult(dialog.getTargetRequestCode(), Activity.RESULT_OK, intent);

        if (webViewIdlingResource != null)
            unregisterIdlingResources(webViewIdlingResource);
        webViewIdlingResource = null;

        // dismiss fragment or press back
        dialog.dismiss();

        ScreenShotter.takeScreenshot("dismiss_dialog", mIntentsRule.getActivity());

        Thread.sleep(3000);

        assertTrue(mIntentsRule.getActivity().isDestroyed());
    }

    /**
     * test_id {"YH/003", "YH/007"}
     * Test yahoo login
     */
    @Test
    public void testCancelYahooLogin() throws Exception {

        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));

        startLoginActivity();

        ViewInteraction loginTextView = onView(
                nthChildOf(
                        withId(R.id.login_buttons_container),
                        3));

        UiObject plusButton = mIntentsRule.getDevice().findObject(new UiSelector().resourceId("com.tokopedia.tkpd:id/btn_load_more"));
        if (plusButton.exists())
            onView(withId(R.id.btn_load_more)).perform(click());


        Thread.sleep(3_000);

        loginTextView.perform(click());

        // necessary to make it wait.
        Thread.sleep(10000);

        // waiting all url to be finished
        DialogFragment dialog = (DialogFragment) mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        final WebView webview = dialog.getView().findViewById(com.tokopedia.core2.R.id.web_oauth);
        if (webview != null) {
            try {
                mIntentsRule.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webViewIdlingResource = new WebViewIdlingResource(webview);
                        Espresso.registerIdlingResources(webViewIdlingResource);
                    }
                });
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        // dismiss fragment or press back
        dialog.dismiss();

        Thread.sleep(1_000);

        assertFalse(mIntentsRule.getActivity().isDestroyed());
    }

    /**
     * test_id {"YH/008"}
     * Test yahoo login
     */
    @Test
    public void testYahooReLogin() throws Exception {
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("relogin.json"));
        mIntentsRule.getIndexMockWebServer(2).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(2).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_gcm_update.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("info.json"));
        mIntentsRule.getIndexMockWebServer(1).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("make_login.json"));

        startLoginActivity();

        ViewInteraction loginTextView = onView(
                nthChildOf(
                        withId(R.id.login_buttons_container),
                        3));

        ScreenShotter.takeScreenshot("1", mIntentsRule.getActivity());

        UiObject plusButton = mIntentsRule.getDevice().findObject(new UiSelector().resourceId("com.tokopedia.tkpd:id/btn_load_more"));
        if (plusButton.exists())
            onView(withId(R.id.btn_load_more)).perform(click());

        Thread.sleep(3_000);

        loginTextView.perform(click());

        // necessary to make it wait.
        Thread.sleep(10000);

        ScreenShotter.takeScreenshot("2", mIntentsRule.getActivity());

        // waiting all url to be finished
        DialogFragment dialog = (DialogFragment) mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        final WebView webview = dialog.getView().findViewById(com.tokopedia.core2.R.id.web_oauth);
        if (webview != null) {
            try {
                mIntentsRule.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webViewIdlingResource = new WebViewIdlingResource(webview);
                        Espresso.registerIdlingResources(webViewIdlingResource);
                    }
                });
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }


        // set target fragment bundle
        Bundle bundle = new Bundle();
        bundle.putString("server", "accounts.tokopedia.com");
        bundle.putString("path", "/mappauth/code");
        bundle.putString("NAME", "Yahoo");
        bundle.putString("code", "v");
        bundle.putString("state", "ad5d4603-7243-4e8e-85a2-d5ae4bcc8deb");
        Intent intent = new Intent();
        intent.putExtra("bundle", bundle);
        dialog.getTargetFragment().onActivityResult(dialog.getTargetRequestCode(), Activity.RESULT_OK, intent);

        if (webViewIdlingResource != null)
            unregisterIdlingResources(webViewIdlingResource);
        webViewIdlingResource = null;

        dialog.dismiss();

        snackbarAnyMatcher();

        ScreenShotter.takeScreenshot("3", mIntentsRule.getActivity());

        Thread.sleep(2000);

        loginTextView.perform(click());

        // necessary to make it wait.
        Thread.sleep(10000);

        ScreenShotter.takeScreenshot("4", mIntentsRule.getActivity());

        // waiting all url to be finished
        dialog = (DialogFragment) mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        final WebView webview2 = dialog.getView().findViewById(com.tokopedia.core2.R.id.web_oauth);
        if (webview != null) {
            try {
                mIntentsRule.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webViewIdlingResource = new WebViewIdlingResource(webview2);
                        Espresso.registerIdlingResources(webViewIdlingResource);
                    }
                });
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        // set target fragment bundle
        bundle = new Bundle();
        bundle.putString("server", "accounts.tokopedia.com");
        bundle.putString("path", "/mappauth/code");
        bundle.putString("NAME", "Yahoo");
        bundle.putString("code", "v");
        bundle.putString("state", "ad5d4603-7243-4e8e-85a2-d5ae4bcc8deb");
        intent = new Intent();
        intent.putExtra("bundle", bundle);
        dialog.getTargetFragment().onActivityResult(dialog.getTargetRequestCode(), Activity.RESULT_OK, intent);

        if (webViewIdlingResource != null)
            unregisterIdlingResources(webViewIdlingResource);
        webViewIdlingResource = null;

        dialog.dismiss();

        ScreenShotter.takeScreenshot("5", mIntentsRule.getActivity());

        Thread.sleep(2000);

        assertTrue(mIntentsRule.getActivity().isDestroyed());
    }

    @Deprecated
    private void performClickYahoo(boolean isFirstTime) throws InterruptedException {
        ViewInteraction loginTextView = onView(
                nthChildOf(
                        withId(R.id.login_buttons_container),
                        3));

        ScreenShotter.takeScreenshot("screenshot_btn_load_more", mIntentsRule.getActivity());

        if (isFirstTime)
            onView(withId(R.id.btn_load_more)).perform(click());


        Thread.sleep(3_000);

        loginTextView.perform(click());

        // necessary to make it wait.
        Thread.sleep(10000);

        ScreenShotter.takeScreenshot("testYahooReLogin_see_dialog", mIntentsRule.getActivity());

        // waiting all url to be finished
        DialogFragment dialog = (DialogFragment) mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag("dialog");

        if (isFirstTime) {
            final WebView webview = dialog.getView().findViewById(com.tokopedia.core2.R.id.web_oauth);
            if (webview != null) {
                try {
                    mIntentsRule.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webViewIdlingResource = new WebViewIdlingResource(webview);
                            Espresso.registerIdlingResources(webViewIdlingResource);
                        }
                    });
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }

        // set target fragment bundle
        Bundle bundle = new Bundle();
        bundle.putString("server", "accounts.tokopedia.com");
        bundle.putString("path", "/mappauth/code");
        bundle.putString("NAME", "Yahoo");
        bundle.putString("code", "v");
        bundle.putString("state", "ad5d4603-7243-4e8e-85a2-d5ae4bcc8deb");
        Intent intent = new Intent();
        intent.putExtra("bundle", bundle);
        dialog.getTargetFragment().onActivityResult(dialog.getTargetRequestCode(), Activity.RESULT_OK, intent);

        if (webViewIdlingResource != null)
            unregisterIdlingResources(webViewIdlingResource);
        webViewIdlingResource = null;

        // dismiss fragment or press back
        dialog.dismiss();

        ScreenShotter.takeScreenshot("dismiss_dialog", mIntentsRule.getActivity());


        Thread.sleep(1_000);
    }

    /**
     * test_id : {"FB/002", "FB/003"}
     *
     * @throws Exception
     */
    @Test
    public void testFacebookLogin() throws Exception {
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("info.json"));
        mIntentsRule.getIndexMockWebServer(1).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("make_login.json"));

        startLoginActivity();

        Fragment fragment = mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName());
        if (fragment != null && fragment.isVisible()) {
            TestSessionModule testSessionModule = new TestSessionModule();
            ((LoginFragment) fragment).initOuterInjector(testSessionModule);

            AccessTokenSource accessTokenSource = AccessTokenSource.CLIENT_TOKEN;

            ArrayList<String> declinedPermissions = new ArrayList<>();
            declinedPermissions.add("user_mobile_phone");

            final AccessToken accessToken = new AccessToken(
                    "lalala",
                    "126665634029576",
                    "lalala",
                    Collections.<String>emptyList(),
                    declinedPermissions,
                    accessTokenSource,
                    new Date(),
                    new Date()
            );

            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    GetFacebookCredentialSubscriber gcmHandlerListener =
                            (GetFacebookCredentialSubscriber) invocation.getArguments()[1];
                    gcmHandlerListener.onSuccess(accessToken, "noiz354@gmail.com");
                    return null;
                }
            }).when(testSessionModule.getGetFacebookCredentialUseCase()).execute(any(RequestParams.class), any(GetFacebookCredentialSubscriber.class));
        }

        ViewInteraction loginTextView = onView(
                nthChildOf(
                        withId(R.id.login_buttons_container),
                        0));

        loginTextView.perform(click());

        Thread.sleep(1_000);

        assertTrue(mIntentsRule.getActivity().isDestroyed());
    }

    /**
     * test_id : {"FB/009"}
     *
     * @throws Exception
     */
    @Test
    public void testFacebookLoginGoToRegisterPage() throws Exception {
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("info_go_to_create_password.json"));

        startLoginActivity();

        Fragment fragment = mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName());
        if (fragment != null && fragment.isVisible()) {
            TestSessionModule testSessionModule = new TestSessionModule();
            ((LoginFragment) fragment).initOuterInjector(testSessionModule);

            AccessTokenSource accessTokenSource = AccessTokenSource.CLIENT_TOKEN;

            ArrayList<String> declinedPermissions = new ArrayList<>();
            declinedPermissions.add("user_mobile_phone");

            final AccessToken accessToken = new AccessToken(
                    "lalala",
                    "126665634029576",
                    "lalala",
                    Collections.<String>emptyList(),
                    declinedPermissions,
                    accessTokenSource,
                    new Date(),
                    new Date()
            );

            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    GetFacebookCredentialSubscriber gcmHandlerListener =
                            (GetFacebookCredentialSubscriber) invocation.getArguments()[1];
                    gcmHandlerListener.onSuccess(accessToken, "noiz354@gmail.com");
                    return null;
                }
            }).when(testSessionModule.getGetFacebookCredentialUseCase()).execute(any(RequestParams.class), any(GetFacebookCredentialSubscriber.class));
        }

        ViewInteraction loginTextView = onView(
                nthChildOf(
                        withId(R.id.login_buttons_container),
                        0));

        loginTextView.perform(click());

        Thread.sleep(1_000);

        assertFalse(mIntentsRule.getActivity().isDestroyed());

        onView(withText(com.tokopedia.core2.R.string.title_personal_id)).check(matches(isDisplayed()));
    }

    /**
     * test_id : {"FB/004","FB/005","FB/007","FB/008",,"FB/013"}
     *
     * @throws Exception
     */
    @Test
    public void testFacebookLoginFailed() throws Exception {
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));

        startLoginActivity();


        Fragment fragment = mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName());
        if (fragment != null && fragment.isVisible()) {
            TestSessionModule testSessionModule = new TestSessionModule();
            ((LoginFragment) fragment).initOuterInjector(testSessionModule);

            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    GetFacebookCredentialSubscriber gcmHandlerListener =
                            (GetFacebookCredentialSubscriber) invocation.getArguments()[1];
                    gcmHandlerListener.onError(new ErrorMessageException(
                            mIntentsRule.getActivity().getString(R.string.facebook_error_not_authorized),
                            ErrorCode.FACEBOOK_AUTHORIZATION_EXCEPTION));
                    return null;
                }
            }).when(testSessionModule.getGetFacebookCredentialUseCase()).execute(any(RequestParams.class), any(GetFacebookCredentialSubscriber.class));
        }

        ViewInteraction loginTextView = onView(
                nthChildOf(
                        withId(R.id.login_buttons_container),
                        0));

        loginTextView.perform(click());

        Thread.sleep(200);

        assertFalse(mIntentsRule.getActivity().isDestroyed());

        snackbarMatcher(ErrorHandler.getErrorMessage(new ErrorMessageException(
                mIntentsRule.getActivity().getString(R.string.facebook_error_not_authorized),
                ErrorCode.FACEBOOK_AUTHORIZATION_EXCEPTION)));
    }

    /**
     * test_id : {"FB/014"}
     * somehow this test require more hit than necessary codes.
     * expected : shouldn't evolved any UI.
     *
     * @throws Exception
     */
    @Test
    public void testFacebookReLogin() throws Exception {
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("relogin.json"));
        mIntentsRule.getIndexMockWebServer(2).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(2).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_gcm_update.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("info.json"));
        mIntentsRule.getIndexMockWebServer(1).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("make_login.json"));

        startLoginActivity();

        Fragment fragment = mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName());
        if (fragment != null && fragment.isVisible()) {
            TestSessionModule testSessionModule = new TestSessionModule();
            ((LoginFragment) fragment).initOuterInjector(testSessionModule);

            AccessTokenSource accessTokenSource = AccessTokenSource.CLIENT_TOKEN;

            ArrayList<String> declinedPermissions = new ArrayList<>();
            declinedPermissions.add("user_mobile_phone");

            final AccessToken accessToken = new AccessToken(
                    "lalala",
                    "126665634029576",
                    "lalala",
                    Collections.<String>emptyList(),
                    declinedPermissions,
                    accessTokenSource,
                    new Date(),
                    new Date()
            );

            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    GetFacebookCredentialSubscriber gcmHandlerListener =
                            (GetFacebookCredentialSubscriber) invocation.getArguments()[1];
                    gcmHandlerListener.onSuccess(accessToken, "noiz354@gmail.com");
                    return null;
                }
            }).when(testSessionModule.getGetFacebookCredentialUseCase()).execute(any(RequestParams.class), any(GetFacebookCredentialSubscriber.class));
        }

        ViewInteraction loginTextView = onView(
                nthChildOf(
                        withId(R.id.login_buttons_container),
                        0));

        loginTextView.perform(click());

        Thread.sleep(200);

        snackbarAnyMatcher();

        ScreenShotter.takeScreenshot("222", mIntentsRule.getActivity());

        loginTextView.perform(click());

        Thread.sleep(2000);

        ScreenShotter.takeScreenshot("313", mIntentsRule.getActivity());

        assertTrue(mIntentsRule.getActivity().isDestroyed());
    }

    private void defaultMockGoogle() {
        intending(hasComponent(GoogleSignInActivity.class.getName())).respondWith(
                mockGoogleActivityResult("test123@gmailcom", null, "12345")
        );
    }

    /**
     * test_id : {"GP/001","GP/003","GP/004","GP/013","GP/014"}
     *
     * @throws Exception
     */
    @Test
    public void testGoogleLogin() throws Exception {
        defaultMockGoogle();

        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("info.json"));
        mIntentsRule.getIndexMockWebServer(1).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("make_login.json"));

        startLoginActivity();

        ViewInteraction loginTextView = onView(
                nthChildOf(
                        withId(R.id.login_buttons_container),
                        1));

        loginTextView.perform(click());

        Thread.sleep(200);

        assertTrue(mIntentsRule.getActivity().isDestroyed());
    }

    /**
     * test_id {"GP/012", "GP/005"}
     *
     * @throws Exception
     */
    @Test
    public void testCancelGoogleLogin() throws Exception {
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null);

        intending(hasComponent(GoogleSignInActivity.class.getName())).respondWith(result);

        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));

        startLoginActivity();

        ViewInteraction loginTextView = onView(
                nthChildOf(
                        withId(R.id.login_buttons_container),
                        1));

        loginTextView.perform(click());

        Thread.sleep(200);

        assertFalse(mIntentsRule.getActivity().isDestroyed());
    }

    /**
     * test_id {"GP/017"}
     *
     * @throws Exception
     */
    @Test
    public void testGoogleRelogin() throws Exception {
        defaultMockGoogle();

        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("relogin.json"));
        mIntentsRule.getIndexMockWebServer(2).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(2).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_gcm_update.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("info.json"));
        mIntentsRule.getIndexMockWebServer(1).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("make_login.json"));

        startLoginActivity();

        ViewInteraction loginTextView = onView(
                nthChildOf(
                        withId(R.id.login_buttons_container),
                        1));

        loginTextView.perform(click());

        Thread.sleep(200);

        snackbarAnyMatcher();

        // this is necessary, wait for snackbar to dismiss
        Thread.sleep(2000);

        loginTextView.perform(click());

        Thread.sleep(2000);

        assertTrue(mIntentsRule.getActivity().isDestroyed());
    }

    /**
     * test_id {"TL/001","TL/002"}
     *
     * @throws Exception
     */
    @Test
    public void testTokopediaLoginEnterSecurityQuestion() throws Exception {
        preparePartialSmartLockBundle();

        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("info.json"));
        mIntentsRule.getIndexMockWebServer(1).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("make_login_security_question.json"));

        startEmptyIntentLoginActivity();

        onView(withId(R.id.email_auto)).perform(typeText("cincin.jati+47@tokopedia.com"));
        onView(withId(R.id.password)).perform(typeText("optimus"));

        onView(withId(R.id.accounts_sign_in)).check(matches(isDisplayed())).perform(click());

        onView(withText(R.string.verification_for_security)).check(matches(isDisplayed()));
    }

    @Test
    public void testTokopediaLoginEmailNotActivated() throws Exception {
        preparePartialSmartLockBundle();

        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("info.json"));
        mIntentsRule.getIndexMockWebServer(1).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("error_not_activated_messages.json"));

        startEmptyIntentLoginActivity();

        onView(withId(R.id.email_auto)).perform(typeText("cincin.jati+47@tokopedia.com"));
        onView(withId(R.id.password)).perform(typeText("optimus"));

        onView(withId(R.id.accounts_sign_in)).check(matches(isDisplayed())).perform(click());

        matchToolbarTitle(mIntentsRule.getActivity().getString(R.string.title_activity_activation));
    }

    /**
     * test_id {"TL/003"}
     *
     * @throws Exception
     */
    @Test
    public void testFailedTokopediaLogin() throws Exception {
        preparePartialSmartLockBundle();

        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("info.json"));
        mIntentsRule.getIndexMockWebServer(1).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("relogin.json"));

        startEmptyIntentLoginActivity();

        onView(withId(R.id.email_auto)).perform(typeText("cincin.jati+47@tokopedia.com"));
        onView(withId(R.id.password)).perform(typeText("optimus"));

        onView(withId(R.id.accounts_sign_in)).check(matches(isDisplayed())).perform(click());

        snackbarAnyMatcher();
    }

    @Test
    public void testVerifyEmailTokopediaLogin() throws Exception {
        preparePartialSmartLockBundle();

        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));

        startEmptyIntentLoginActivity();

        onView(withId(R.id.password)).perform(typeText("optimus"));

        onView(withId(R.id.accounts_sign_in)).check(matches(isDisplayed())).perform(click());

        ViewInteraction loginTextView = onView(
                nthChildOf(nthChildOf(nthChildOf(nthChildOf(
                        withId(R.id.wrapper_email),
                        0), 2), 0), 1));
        loginTextView.check(matches(withText(R.string.error_field_required)));

        onView(withId(R.id.email_auto)).perform(typeText("cincin.jati+47tokopedia.com"));
        onView(withId(R.id.accounts_sign_in)).check(matches(isDisplayed())).perform(click());

        loginTextView.check(matches(withText(R.string.error_invalid_email)));

        ViewInteraction passwordTextView = onView(
                nthChildOf(nthChildOf(nthChildOf(nthChildOf(
                        withId(R.id.wrapper_password),
                        0), 2), 0), 1));

        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.accounts_sign_in)).check(matches(isDisplayed())).perform(click());
        passwordTextView.check(matches(withText(R.string.error_field_required)));

        onView(withId(R.id.password)).perform(typeText("123"));
        onView(withId(R.id.accounts_sign_in)).check(matches(isDisplayed())).perform(click());
        passwordTextView.check(matches(withText(R.string.error_incorrect_password)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testSmartLockFullBundle() throws Exception {
        prepareForFullSmartLockBundle();

        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("info.json"));
        mIntentsRule.getIndexMockWebServer(1).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("make_login.json"));

        startEmptyIntentLoginActivity();

        Thread.sleep(3000);

        assertTrue(mIntentsRule.getActivity().isDestroyed());
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

        intending(hasComponent(SmartLockActivity.class.getName())).respondWith(result);
    }

    @Test
    public void testSmartLockFullBundleWithErrorResponse() throws Exception {
        prepareForFullSmartLockBundle();

        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("token.json"));
        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("info.json"));
        mIntentsRule.getIndexMockWebServer(1).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("error_messages.json"));

        startEmptyIntentLoginActivity();

        Thread.sleep(3000);

        assertFalse(mIntentsRule.getActivity().isDestroyed());
    }

    @Test
    public void testSmartLockPartialBundle() throws Exception {
        preparePartialSmartLockBundle();

        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));

        startEmptyIntentLoginActivity();

        Thread.sleep(3000);

        assertFalse(mIntentsRule.getActivity().isDestroyed());
    }

    @Test
    public void testEnterRegisterPage() {

        mIntentsRule.getIndexMockWebServer(0).enqueue(mIntentsRule.getBaseJsonFactory().createSuccess200Response("api_discover.json"));

        startLoginActivity();

        onView(withId(R.id.action_register)).perform(click());

        intended(hasComponent(RegisterInitialActivity.class.getName()));
    }

    private void preparePartialSmartLockBundle() {
        Intent resultData = new Intent();
        Bundle bundle = new Bundle();
        String phoneNumber = "123-345-6789";
        bundle.putString("phone", phoneNumber);
        bundle.putString("username", "cincin.jati+47@tokopedia.com");

        resultData.putExtras(bundle);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        intending(hasComponent(SmartLockActivity.class.getName())).respondWith(result);
    }

    private void startLoginActivity() {
        Intent intent = new Intent();
        intent.putExtra("auto_login", true);
        intent.putExtra("method", 444);
        intent.putExtra("email", "noiz354@gmail.com");
        intent.putExtra("pws", "lalala");
        mIntentsRule.launchActivity(intent);
    }

    private void startEmptyIntentLoginActivity() {
        mIntentsRule.launchActivity(null);
    }
}

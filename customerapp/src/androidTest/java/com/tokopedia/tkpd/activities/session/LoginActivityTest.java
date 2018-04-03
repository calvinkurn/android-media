package com.tokopedia.tkpd.activities.session;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.webkit.WebView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.ErrorCode;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.network.SessionUrl;
import com.tokopedia.session.google.GoogleSignInActivity;
import com.tokopedia.tkpd.BaseJsonFactory;
import com.tokopedia.sellerapp.RxJavaTestPlugins;
import com.tokopedia.tkpd.Utils;
import com.tokopedia.tkpd.WebViewIdlingResource;
import com.tokopedia.tkpd.activities.session.modules.TestSessionModule;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.session.login.loginemail.view.fragment.LoginFragment;
import com.tokopedia.session.register.view.activity.SmartLockActivity;
import com.tokopedia.session.register.view.subscriber.registerinitial.GetFacebookCredentialSubscriber;
import com.tokopedia.tkpd.ConsumerMainApplication;
import com.tokopedia.session.R;
import com.tokopedia.usecase.RequestParams;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.inject.Inject;

import okhttp3.mockwebserver.MockWebServer;
import rx.Scheduler;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.tokopedia.tkpd.Utils.changeScreenOrientation;
import static com.tokopedia.tkpd.Utils.nthChildOf;
import static com.tokopedia.tkpd.Utils.snackbarAnyMatcher;
import static com.tokopedia.tkpd.Utils.snackbarMatcher;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by normansyahputa on 3/21/18.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mIntentsRule = new ActivityTestRule<LoginActivity>(
            LoginActivity.class, true, false
    );

    @Inject
    GCMHandler gcmHandler;

    @Inject
    Gson gson;

    BaseJsonFactory baseJsonFactory;

    Context context;
    private MockWebServer server, server2;
    private String loginSuccessKainan;

    private UiDevice device;
    private WebViewIdlingResource webViewIdlingResource;
    private ErrorMessageException facebookErrorNotAuthorizedException;
    private ErrorMessageException facebookException;
    private MockWebServer server3;

    @Before
    public void setup() throws Exception {
        Intents.init();

        server3 = new MockWebServer();
        server3.start();

        server2 = new MockWebServer();
        server2.start();

        server = new MockWebServer();
        server.start();

        device = UiDevice.getInstance(getInstrumentation());

//        assertTrue("Back button can't be pressed", device.pressBack());

        RxJavaHooks.setOnIOScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        RxJavaHooks.setOnNewThreadScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        RxJavaHooks.setOnNewThreadScheduler(new Func1<Scheduler, Scheduler>() {
            @Override
            public Scheduler call(Scheduler scheduler) {
                return Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        SessionUrl.BASE_DOMAIN = server2.url("/").toString();
        SessionUrl.ACCOUNTS_DOMAIN = server.url("/").toString();
        TkpdBaseURL.ACCOUNTS_DOMAIN = server3.url("/").toString();

        RxJavaTestPlugins.setAsyncTaskScheduler();

        ConsumerMainApplication application = (ConsumerMainApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();

        facebookErrorNotAuthorizedException = new ErrorMessageException(
                application.getString(R.string.facebook_error_not_authorized),
                ErrorCode.FACEBOOK_AUTHORIZATION_EXCEPTION);

        facebookException = new ErrorMessageException(
                application.getString(R.string.facebook_error_not_authorized),
                ErrorCode.FACEBOOK_EXCEPTION);

        new GlobalCacheManager().deleteAll();

        SessionHandler.clearUserData(application);

        baseJsonFactory = new BaseJsonFactory(InstrumentationRegistry.getContext());
    }

    /**
     * 1. start the app
     * 2. because for some reason (it trigger another activity then it should avoid that.
     * put intent to do that
     *
     * @throws Exception just throw exception
     */
    @Test
    public void testEmailLogin() throws Exception {
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));

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

        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("info.json")));
        server2.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("make_login.json")));

        onView(withId(R.id.accounts_sign_in))
                .perform(click());
    }

    /**
     * test_id {"YH/003"}
     * Test yahoo login
     */
    @Test
    public void testYahooLogin() throws Exception {

        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("info.json")));
        server2.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("make_login.json")));

        startLoginActivity();

        performClickYahoo(true);

        assertTrue(mIntentsRule.getActivity().isDestroyed());
    }

    /**
     * test_id {"YH/003", "YH/007"}
     * Test yahoo login
     */
    @Test
    public void testCancelYahooLogin() throws Exception {

        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));

        startLoginActivity();

        ViewInteraction loginTextView = onView(
                nthChildOf(
                        withId(R.id.login_buttons_container),
                        3));

        onView(withId(R.id.btn_load_more)).perform(click());//.check(not(isVisible()));


        Thread.sleep(3_000);

        loginTextView.perform(click());

        // necessary to make it wait.
        Thread.sleep(10000);

        // waiting all url to be finished
        DialogFragment dialog = (DialogFragment) mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        final WebView webview = dialog.getView().findViewById(com.tokopedia.core.R.id.web_oauth);
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

        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("relogin.json")));
        server3.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server3.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_gcm_update.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("info.json")));
        server2.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("make_login.json")));

        startLoginActivity();

        performClickYahoo(true);

        snackbarAnyMatcher();

        Thread.sleep(2000);

        performClickYahoo(false);

        assertTrue(mIntentsRule.getActivity().isDestroyed());
    }

    private void performClickYahoo(boolean isFirstTime) throws InterruptedException {
        ViewInteraction loginTextView = onView(
                nthChildOf(
                        withId(R.id.login_buttons_container),
                        3));

        if(isFirstTime)
            onView(withId(R.id.btn_load_more)).perform(click());


        Thread.sleep(3_000);

        loginTextView.perform(click());

        // necessary to make it wait.
        Thread.sleep(10000);

        // waiting all url to be finished
        DialogFragment dialog = (DialogFragment) mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        final WebView webview = dialog.getView().findViewById(com.tokopedia.core.R.id.web_oauth);
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

        // dismiss fragment or press back
        dialog.dismiss();

        Thread.sleep(1_000);
    }

    /**
     * test_id : {"FB/002", "FB/003"}
     * @throws Exception
     */
    @Test
    public void testFacebookLogin() throws Exception{
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("info.json")));
        server2.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("make_login.json")));

        startLoginActivity();

        Fragment fragment = mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName());
        if(fragment != null && fragment.isVisible()){
            TestSessionModule testSessionModule = new TestSessionModule();
            ((LoginFragment)fragment).initOuterInjector(testSessionModule);

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
     * @throws Exception
     */
    @Test
    public void testFacebookLoginGoToRegisterPage() throws Exception{
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("info_go_to_create_password.json")));

        startLoginActivity();

        Fragment fragment = mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName());
        if(fragment != null && fragment.isVisible()){
            TestSessionModule testSessionModule = new TestSessionModule();
            ((LoginFragment)fragment).initOuterInjector(testSessionModule);

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

        onView(withText(com.tokopedia.core.R.string.title_personal_id)).check(matches(isDisplayed()));
    }

    /**
     * test_id : {"FB/004","FB/005","FB/007","FB/008",,"FB/013"}
     * @throws Exception
     */
    @Test
    public void testFacebookLoginFailed() throws Exception{
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));

        startLoginActivity();

        Fragment fragment = mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName());
        if(fragment != null && fragment.isVisible()){
            TestSessionModule testSessionModule = new TestSessionModule();
            ((LoginFragment)fragment).initOuterInjector(testSessionModule);

            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    GetFacebookCredentialSubscriber gcmHandlerListener =
                            (GetFacebookCredentialSubscriber) invocation.getArguments()[1];
                    gcmHandlerListener.onError(facebookErrorNotAuthorizedException);
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

        snackbarMatcher(ErrorHandler.getErrorMessage(facebookErrorNotAuthorizedException));
    }

    /**
     * test_id : {"FB/014"}
     * somehow this test require more hit than necessary codes.
     * expected : shouldn't evolved any UI.
     * @throws Exception
     */
    @Test
    public void testFacebookReLogin() throws Exception{
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("relogin.json")));
        server3.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server3.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_gcm_update.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("info.json")));
        server2.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("make_login.json")));

        startLoginActivity();

        Fragment fragment = mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getSimpleName());
        if(fragment != null && fragment.isVisible()){
            TestSessionModule testSessionModule = new TestSessionModule();
            ((LoginFragment)fragment).initOuterInjector(testSessionModule);

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

        loginTextView.perform(click());

        assertTrue(mIntentsRule.getActivity().isDestroyed());
    }

    /**
     * test_id : {"GP/001","GP/003","GP/004","GP/013","GP/014"}
     * @throws Exception
     */
    @Test
    public void testGoogleLogin() throws Exception{
        Intent resultData = new Intent();
        Bundle bundle = new Bundle();
        GoogleSignInAccount account = mock(GoogleSignInAccount.class);
        when(account.getEmail()).thenReturn("test123@gmailcom");
        bundle.putParcelable(GoogleSignInActivity.KEY_GOOGLE_ACCOUNT, account);
        bundle.putString(GoogleSignInActivity.KEY_GOOGLE_ACCOUNT_TOKEN, "12345");

        resultData.putExtras(bundle);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        intending(hasComponent(GoogleSignInActivity.class.getName())).respondWith(result);


        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("info.json")));
        server2.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("make_login.json")));

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
     * @throws Exception
     */
    @Test
    public void testCancelGoogleLogin() throws Exception{
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null);

        intending(hasComponent(GoogleSignInActivity.class.getName())).respondWith(result);

        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));

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
     * @throws Exception
     */
    @Test
    public void testGoogleRelogin() throws Exception{
        Intent resultData = new Intent();
        Bundle bundle = new Bundle();
        GoogleSignInAccount account = mock(GoogleSignInAccount.class);
        when(account.getEmail()).thenReturn("test123@gmailcom");
        bundle.putParcelable(GoogleSignInActivity.KEY_GOOGLE_ACCOUNT, account);
        bundle.putString(GoogleSignInActivity.KEY_GOOGLE_ACCOUNT_TOKEN, "12345");

        resultData.putExtras(bundle);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        intending(hasComponent(GoogleSignInActivity.class.getName())).respondWith(result);

        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("relogin.json")));
        server3.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server3.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_gcm_update.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("info.json")));
        server2.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("make_login.json")));

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

        assertTrue(mIntentsRule.getActivity().isDestroyed());
    }

    @Test
    public void testTokopediaLoginEnterSecurityQuestion() throws Exception{
        preparePartialSmartLockBundle();

        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("info.json")));
        server2.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("make_login_security_question.json")));

        startEmptyIntentLoginActivity();

        onView(withId(R.id.accounts_sign_in)).check(matches(isDisplayed())).perform(click());
        onView(withId(com.tokopedia.design.R.id.email_auto)).perform(typeText("cincin.jati+47@tokopedia.com"));
        onView(withId(com.tokopedia.design.R.id.password)).perform(typeText("cincin.jati+47@tokopedia.com"));

        Thread.sleep(2000);

        Log.d("NORMANSYAH", "test lalalala");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testSmartLockFullBundle() throws Exception {
        prepareForFullSmartLockBundle();

        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("info.json")));
        server2.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("make_login.json")));

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

        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("info.json")));
        server2.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("error_messages.json")));

        startEmptyIntentLoginActivity();

        Thread.sleep(3000);

        assertFalse(mIntentsRule.getActivity().isDestroyed());
    }

    @Test
    public void testSmartLockPartialBundle() throws Exception {
        preparePartialSmartLockBundle();

        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));

        startEmptyIntentLoginActivity();

        Thread.sleep(3000);

        assertFalse(mIntentsRule.getActivity().isDestroyed());
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

    @After
    public void tearDown() throws Exception {
        RxJavaTestPlugins.resetJavaTestPlugins();
        if (webViewIdlingResource != null)
            unregisterIdlingResources(webViewIdlingResource);

        Intents.release();
        server.shutdown();
        server2.shutdown();
        server3.shutdown();
    }
}

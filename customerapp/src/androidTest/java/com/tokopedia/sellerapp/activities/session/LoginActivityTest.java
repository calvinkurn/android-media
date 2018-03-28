package com.tokopedia.sellerapp.activities.session;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.app.DialogFragment;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.SessionUrl;
import com.tokopedia.sellerapp.BaseJsonFactory;
import com.tokopedia.sellerapp.RxJavaTestPlugins;
import com.tokopedia.sellerapp.Utils;
import com.tokopedia.sellerapp.WebViewIdlingResource;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
import com.tokopedia.session.register.view.activity.SmartLockActivity;
import com.tokopedia.tkpd.ConsumerMainApplication;
import com.tokopedia.tkpd.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.tokopedia.sellerapp.Utils.nthChildOf;
import static org.hamcrest.core.Is.is;

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

    @Before
    public void setup() throws Exception {
        Intents.init();

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

        RxJavaTestPlugins.setAsyncTaskScheduler();

        ConsumerMainApplication application = (ConsumerMainApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();

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
     * Test yahoo login
     */
    @Test
    public void testYahooLogin() throws Exception {

        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("info.json")));
        server2.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("make_login.json")));

        startLoginActivity();

        // click button
        onView(withText("You should car about that"))
                .perform(scrollTo());

        onView(nthChildOf(withId(R.id.login_buttons_container), 3)).check(matches(withText("You should car about that")));

        onView(nthChildOf(withId(R.id.login_buttons_container), 3)).perform(click());

        // necessary to make it wait.
        Thread.sleep(3000);

        // waiting all url to be finished
        DialogFragment dialog = (DialogFragment) mIntentsRule.getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
        final WebView webview = dialog.getView().findViewById(R.id.web_oauth);
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

        onView(withId(R.id.login_status)).check(matches(isDisplayed()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testSmartLockFullBundle() throws Exception {
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

        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("token.json")));
        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("info.json")));
        server2.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("make_login.json")));

        startEmptyIntentLoginActivity();

        Thread.sleep(3000);
    }

    @Test
    public void testSmartLockPartialBundle() throws Exception {
        Intent resultData = new Intent();
        Bundle bundle = new Bundle();
        String phoneNumber = "123-345-6789";
        bundle.putString("phone", phoneNumber);
        bundle.putString("username", "cincin.jati+47@tokopedia.com");

        resultData.putExtras(bundle);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);

        intending(hasComponent(SmartLockActivity.class.getName())).respondWith(result);

        server.enqueue(Utils.createSuccess200Response(baseJsonFactory.convertFromAndroidResource("api_discover.json")));

        startEmptyIntentLoginActivity();

        Thread.sleep(3000);
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
        if(webViewIdlingResource!=null)
            unregisterIdlingResources(webViewIdlingResource);

        Intents.release();
        server.shutdown();
        server2.shutdown();
    }
}

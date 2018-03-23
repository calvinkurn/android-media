package com.tokopedia.sellerapp.activities.session;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.SessionUrl;
import com.tokopedia.sellerapp.BaseJsonFactory;
import com.tokopedia.sellerapp.RxJavaTestPlugins;
import com.tokopedia.sellerapp.Utils;
import com.tokopedia.session.login.loginemail.view.activity.LoginActivity;
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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by normansyahputa on 3/21/18.
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> activityTestRule =
            new ActivityTestRule<LoginActivity>(
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

    @Before
    public void setup() throws Exception {
        server2 = new MockWebServer();
        server2.start();

        server = new MockWebServer();
        server.start();

        device = UiDevice.getInstance(getInstrumentation());

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
    public void testFirstRunSellerHome() throws Exception {
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


    private void startLoginActivity() {
        Intent intent = new Intent();
        intent.putExtra("auto_login", true);
        intent.putExtra("method", 444);
        intent.putExtra("email", "noiz354@gmail.com");
        intent.putExtra("pws", "lalala");
        activityTestRule.launchActivity(intent);
    }

    @After
    public void tearDown() throws Exception {
        RxJavaTestPlugins.resetJavaTestPlugins();
        server.shutdown();
        server2.shutdown();
    }
}

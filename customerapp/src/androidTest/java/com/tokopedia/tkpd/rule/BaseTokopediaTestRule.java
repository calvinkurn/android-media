package com.tokopedia.tkpd.rule;

import android.app.Activity;
import android.os.AsyncTask;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import com.tokopedia.sellerapp.RxJavaTestPlugins;
import com.tokopedia.tkpd.BaseRetrofitJsonFactory;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;
import rx.Scheduler;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 4/6/18.
 */

public abstract class BaseTokopediaTestRule<T extends Activity> extends ActivityTestRule<T> {
    protected MockWebServer[] mockWebServers;
    protected BaseRetrofitJsonFactory baseJsonFactory;

    public BaseTokopediaTestRule(Class<T> activityClass, int numOfServer) {
        super(activityClass);
        mockWebServers = new MockWebServer[numOfServer];
        baseJsonFactory = new BaseRetrofitJsonFactory(InstrumentationRegistry.getContext());
    }

    public BaseTokopediaTestRule(Class<T> activityClass, boolean initialTouchMode, int numOfServer) {
        super(activityClass, initialTouchMode);
        mockWebServers = new MockWebServer[numOfServer];
        baseJsonFactory = new BaseRetrofitJsonFactory(InstrumentationRegistry.getContext());
    }

    public BaseTokopediaTestRule(Class<T> activityClass, boolean initialTouchMode, boolean launchActivity, int numOfServer) {
        super(activityClass, initialTouchMode, launchActivity);
        mockWebServers = new MockWebServer[numOfServer];
        baseJsonFactory = new BaseRetrofitJsonFactory(InstrumentationRegistry.getContext());
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new TokopediaStatement(super.apply(base, description), mockWebServers);
    }

    public MockWebServer getIndexMockWebServer(int index) {
        if (index < 0 && index >= mockWebServers.length) {
            throw new IllegalArgumentException("please get mockwebserver based on current length");
        }

        return mockWebServers[index];
    }

    public BaseRetrofitJsonFactory getBaseJsonFactory() {
        return baseJsonFactory;
    }

    public static class TokopediaStatement extends Statement {

        protected final Statement mBase;
        protected final MockWebServer[] mockWebServers;

        public TokopediaStatement(Statement base, MockWebServer[] mockWebServers) {
            mBase = base;
            this.mockWebServers = mockWebServers;
            for (int i = 0; i < mockWebServers.length; i++) {
                mockWebServers[i] = new MockWebServer();
                try {
                    mockWebServers[i].start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void evaluate() throws Throwable {
            initBaseTokopedia();
            try {
                mBase.evaluate();
            } finally {
                resetBaseTokopedia();
            }

        }

        protected void resetBaseTokopedia() throws IOException {
            RxJavaTestPlugins.resetJavaTestPlugins();

            RxJavaHooks.reset();

            for (MockWebServer mockWebServer : mockWebServers) {
                mockWebServer.shutdown();
            }
            Intents.release();

        }

        protected void initBaseTokopedia() throws IOException {
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

            RxJavaTestPlugins.setAsyncTaskScheduler();
            Intents.init();
        }
    }
}

package com.tokopedia.test.application.environment;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.benchmark.IsolationActivity;
import androidx.benchmark.junit4.AndroidBenchmarkRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.tokopedia.instrumentation.test.R;
import com.tokopedia.test.application.benchmark_component.activity.TkpdIsolationActivity;

public class TkpdAndroidBenchmarkTestRunner extends AndroidBenchmarkRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return super.newApplication(cl, InstrumentationTestApp.class.getName(), context);
    }

    @Override
    public void onCreate(Bundle arguments) {
        /* This is how we enable output on FirebaseTestLab, as the
         environment variable name isn't valid there. */
        arguments.putString("androidx.benchmark.output.enable", "true");
        super.onCreate(arguments);
    }

    @Override
    protected void waitForActivitiesToComplete() {
        final boolean[] isResumed = {false};
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                isResumed[0] = TkpdIsolationActivity.Companion.getResumed();
            }
        });
        if (!isResumed[0]) {
            IsolationActivity.Companion.launchSingleton();
            TkpdIsolationActivity.Companion.launchSingleton(androidx.appcompat.R.style.Theme_AppCompat_Light_NoActionBar);
        }
    }

    @Override
    public void onDestroy() {
        IsolationActivity.Companion.finishSingleton();
        TkpdIsolationActivity.Companion.finishSingleton();
        super.waitForActivitiesToComplete();
        super.onDestroy();
    }

}

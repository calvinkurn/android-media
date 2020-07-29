package com.tokopedia.test.application.environment;

import android.app.Application;
import android.content.Context;

import androidx.benchmark.junit4.AndroidBenchmarkRunner;

public class TkpdAndroidBenchmarkTestRunner extends AndroidBenchmarkRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return super.newApplication(cl, InstrumentationTestApp.class.getName(), context);
    }
}

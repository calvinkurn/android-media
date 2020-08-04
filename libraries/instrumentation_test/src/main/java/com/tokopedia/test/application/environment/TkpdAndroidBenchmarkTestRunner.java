package com.tokopedia.test.application.environment;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.benchmark.junit4.AndroidBenchmarkRunner;

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
}

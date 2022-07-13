package com.tokopedia.test.application.environment;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import androidx.test.runner.AndroidJUnitRunner;

import com.github.tmurakami.dexopener.DexOpener;
import com.tokopedia.test.application.environment.InstrumentationTestApp;

public class InstrumentationTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        // We only need to enable DexOpener for < 28
        // MockK supports for mocking final classes on Android 9+.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            DexOpener.install(this);
        }
        return super.newApplication(cl, InstrumentationTestApp.class.getName(), context);
    }
}

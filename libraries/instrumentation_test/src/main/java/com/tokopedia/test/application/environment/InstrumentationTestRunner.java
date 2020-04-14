package com.tokopedia.test.application.environment;

import android.app.Application;
import android.content.Context;

import androidx.test.runner.AndroidJUnitRunner;

import com.tokopedia.test.application.environment.InstrumentationTestApp;

public class InstrumentationTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return super.newApplication(cl, InstrumentationTestApp.class.getName(), context);
    }
}

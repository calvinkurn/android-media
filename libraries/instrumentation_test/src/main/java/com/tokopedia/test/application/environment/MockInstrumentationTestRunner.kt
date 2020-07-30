package com.tokopedia.test.application.environment

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class MockInstrumentationTestRunner : AndroidJUnitRunner() {
    @Throws(ClassNotFoundException::class, IllegalAccessException::class, InstantiationException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, MockInstrumentationTestApp::class.java.name, context)
    }
}
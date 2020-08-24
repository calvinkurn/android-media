package com.tokopedia.test.application.util

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.tokopedia.test.application.environment.InstrumentationTestApp

fun setupRemoteConfig(mapOfRemoteConfigs: Map<String, String>) {
    val application = getInstrumentation().targetContext.applicationContext as InstrumentationTestApp
    application.enableRemoteConfig(mapOfRemoteConfigs)
}

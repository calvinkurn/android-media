package com.tokopedia.test.application.util

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.tokopedia.test.application.environment.InstrumentationTestApp

fun setupTopAdsDetector() {
    val application = getInstrumentation().targetContext.applicationContext as InstrumentationTestApp
    application.enableTopAdsDetector()
}

package com.tokopedia.test.application.util

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.tokopedia.test.application.environment.InstrumentationTestApp
import com.tokopedia.test.application.environment.callback.GqlResponseAnalyzerInterface

fun setupTotalSizeInterceptor() {
    val application = getInstrumentation().targetContext.applicationContext as InstrumentationTestApp
    application.enableSizeDetector()
    (application as GqlResponseAnalyzerInterface).reset()
}

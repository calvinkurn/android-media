package com.tokopedia.test.application.util

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.tokopedia.test.application.environment.InstrumentationTestApp
import com.tokopedia.test.application.environment.interceptor.size.GqlNetworkAnalyzerInterceptor

fun setupTotalSizeInterceptor(listToAnalyze: List<String>?) {
    val application = getInstrumentation().targetContext.applicationContext as InstrumentationTestApp
    application.enableSizeDetector(listToAnalyze)
}

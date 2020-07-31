package com.tokopedia.test.application.util

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.tokopedia.test.application.environment.InstrumentationTestApp
import com.tokopedia.test.application.environment.callback.ResponseTotalSizeInterface
import com.tokopedia.test.application.environment.interceptor.size.SizeModelConfig

fun setupTotalSizeInterceptor(sizeModelConfig: SizeModelConfig) {
    val application = getInstrumentation().targetContext.applicationContext as InstrumentationTestApp
    application.enableSizeDetector(sizeModelConfig)
}

//in kb
fun getTotalResponseSize(): String {
    val application = getInstrumentation().targetContext.applicationContext as InstrumentationTestApp
    val totalSizeInterface = application as ResponseTotalSizeInterface
    return application.responseTotalSize.toString()
}
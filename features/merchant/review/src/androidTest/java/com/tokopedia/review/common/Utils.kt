package com.tokopedia.review.common

import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.common.network.util.CommonUtil
import java.util.concurrent.TimeUnit

object Utils {
    inline fun <reified T> parseFromJson(filePath: String): T {
        val stringFile = String(Utils::class.java.classLoader!!.getResourceAsStream(filePath).readBytes())
        return CommonUtil.fromJson(stringFile, T::class.java)
    }

    fun waitForCondition(timeout: Long = TimeUnit.SECONDS.toMillis(5), predicate: () -> Boolean) {
        val start = System.currentTimeMillis()
        while (!predicate() && System.currentTimeMillis() - start <= timeout) {
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        }
    }
}
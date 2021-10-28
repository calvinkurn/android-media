package com.tokopedia.kyc_centralized.util

import android.util.Log
import com.tokopedia.common.network.util.CommonUtil
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*

import org.junit.Test
import kotlin.system.measureTimeMillis

class ImageEncryptionUtilTest {

    @Test
    fun UsingCommonUtilWheninitAesEncryptUsingThenDecodedSymmetrically() {
        val aes = ImageEncryptionUtil.initAesEncrypt()
        val iv = aes.iv

        val time = measureTimeMillis {
            val savedStr = CommonUtil.toJson(iv)
            val extractedByte = CommonUtil.fromJson<ByteArray>(savedStr, ByteArray::class.java)
            assertThat(extractedByte, `is`(iv))
        }
        Log.d(this.javaClass.canonicalName, "commonUtil took $time ms")
    }

}
package com.tokopedia.home_account.common

import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.test.application.util.InstrumentationMockHelper
import java.io.IOException
import java.lang.reflect.Type

object AndroidFileUtil {

    private fun readFileContent(resId: Int): String {
        val context = InstrumentationRegistry
            .getInstrumentation()
            .context
        return InstrumentationMockHelper.getRawString(
            context, resId
        )
    }

    fun <T> parse(resId: Int, typeOfT: Type): T {
        val stringFile = readFileContent(resId)
        return CommonUtil.fromJson(stringFile, typeOfT)
    }
}
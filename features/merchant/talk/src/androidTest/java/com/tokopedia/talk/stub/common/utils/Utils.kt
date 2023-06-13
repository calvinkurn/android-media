package com.tokopedia.talk.stub.common.utils

import com.tokopedia.common.network.util.CommonUtil

object Utils {
    inline fun <reified T> parseFromJson(filePath: String): T {
        val stringFile = String(Utils::class.java.classLoader!!.getResourceAsStream(filePath).readBytes())
        return CommonUtil.fromJson(stringFile, T::class.java)
    }
}

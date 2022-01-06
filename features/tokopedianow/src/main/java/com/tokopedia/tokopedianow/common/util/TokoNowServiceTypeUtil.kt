package com.tokopedia.tokopedianow.common.util

object TokoNowServiceTypeUtil {
    private const val NOW_15M = "15m"

    fun getServiceTypeCopyWriting(serviceType: String, copyWriting15M: String, copyWriting2H: String) = if (serviceType == NOW_15M) copyWriting15M else copyWriting2H

    fun getServiceTypeCopyWritingRes(serviceType: String, copyWriting15MRes: Int, copyWriting2HRes: Int) = if (serviceType == NOW_15M) copyWriting15MRes else copyWriting2HRes
}
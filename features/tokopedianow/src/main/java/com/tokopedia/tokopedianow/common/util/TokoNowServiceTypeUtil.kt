package com.tokopedia.tokopedianow.common.util

import android.content.Context
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ServiceType.NOW_15M

object TokoNowServiceTypeUtil {
    fun getServiceTypeCopyWriting(serviceType: String, context: Context) = if (serviceType == NOW_15M)
        context.getString(R.string.tokopedianow_15m_copy)
    else
        context.getString(R.string.tokopedianow_2h_copy)

    fun getServiceTypeCopyWritingRes(serviceType: String, copyWriting15MRes: Int, copyWriting2HRes: Int) = if (serviceType == NOW_15M)
        copyWriting15MRes
    else
        copyWriting2HRes
}
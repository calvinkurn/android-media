package com.tokopedia.buyerorderdetail.common.utils

import com.tokopedia.remoteconfig.RemoteConfigInstance

object BuyerOrderDetailShareUtils {

    private const val SHARE_EX_ROLLENCE_KEY = "shareex_an_order"

    fun isUsingShareEx(): Boolean {
        val rollenceKey = SHARE_EX_ROLLENCE_KEY
        return RemoteConfigInstance.getInstance().abTestPlatform.getString(
            rollenceKey,
            ""
        ) == rollenceKey
    }
}

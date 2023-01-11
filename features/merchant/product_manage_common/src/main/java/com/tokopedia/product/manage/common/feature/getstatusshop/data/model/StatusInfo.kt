package com.tokopedia.product.manage.common.feature.getstatusshop.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toIntSafely

data class StatusInfo(
    @SerializedName("shopStatus")
    val shopStatus: String,
    @SerializedName("statusTitle")
    val statusTitle: String,
    @SerializedName("statusMessage")
    val statusMessage: String,
    @SerializedName("tickerType")
    val tickerType: String
) {

    companion object {
        const val ON_MODERATED_STAGE = 3
        const val ON_MODERATED_PERMANENTLY = 5
    }

    fun isOnModerationMode(): Boolean {
        val status = shopStatus.toIntSafely()
        return status == ON_MODERATED_STAGE || status == ON_MODERATED_PERMANENTLY
    }
}

package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toIntOrZero

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
        val status = shopStatus.toIntOrZero()
        return status == ON_MODERATED_STAGE || status ==  ON_MODERATED_PERMANENTLY
    }
}

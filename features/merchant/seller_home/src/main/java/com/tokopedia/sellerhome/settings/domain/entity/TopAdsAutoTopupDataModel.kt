package com.tokopedia.sellerhome.settings.domain.entity

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException

data class TopAdsAutoTopupDataModel (
        @SerializedName("topAdsAutoTopup")
        var topAdsAutoTopup: TopAdsAutoTopup? = TopAdsAutoTopup()
)

data class TopAdsAutoTopup (
        @SerializedName("data")
        var autoTopupStatus: AutoTopupStatus? = AutoTopupStatus(),
        @SerializedName("errors")
        var error: List<TopAdsAutoTopErrorCode>? = listOf()
)

class AutoTopupStatus (
        @SerializedName("status")
        var status: String? = "") {

        val isAutoTopup: Boolean =
                when(status) {
                        "0" -> false
                        "1" -> true
                        else -> throw ResponseErrorException()
                }
}

data class TopAdsAutoTopErrorCode (
        @SerializedName("Code")
        var errorCode: String? = ""
)
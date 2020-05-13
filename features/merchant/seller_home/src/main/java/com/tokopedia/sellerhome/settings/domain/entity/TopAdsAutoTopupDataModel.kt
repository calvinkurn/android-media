package com.tokopedia.sellerhome.settings.domain.entity

import com.google.gson.annotations.SerializedName

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

data class AutoTopupStatus (
        @SerializedName("status")
        var status: String? = "")

data class TopAdsAutoTopErrorCode (
        @SerializedName("Code")
        var errorCode: String? = ""
)
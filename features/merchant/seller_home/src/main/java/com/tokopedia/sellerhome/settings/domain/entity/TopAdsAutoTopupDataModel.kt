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
        var error: List<TopAdsAutoTopError>? = listOf()
)

data class AutoTopupStatus (
        @SerializedName("status")
        var status: String? = "")

data class TopAdsAutoTopError (
        @SerializedName("Code")
        var errorCode: String? = "",
        @SerializedName("Title")
        val title: String = "",
        @SerializedName("Detail")
        val detail: String = "",
        @SerializedName("Object")
        val errorObject: TopadsAutoTopUpErrorObject = TopadsAutoTopUpErrorObject()
)

data class TopadsAutoTopUpErrorObject (
        @SerializedName("Text")
        val errorTextList: List<String> = listOf(),
        @SerializedName("Type")
        val errorType: Int = 0
)
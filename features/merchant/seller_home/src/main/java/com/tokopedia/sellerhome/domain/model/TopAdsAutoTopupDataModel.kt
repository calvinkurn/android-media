package com.tokopedia.sellerhome.domain.model

import com.google.gson.annotations.SerializedName

data class TopAdsAutoTopupDataModel (
        @SerializedName("topAdsAutoTopupV2")
        val topAdsAutoTopup: TopAdsAutoTopup? = TopAdsAutoTopup()
)

data class TopAdsAutoTopup (
        @SerializedName("data")
        val autoTopupStatus: AutoTopupStatus? = AutoTopupStatus(),
        @SerializedName("errors")
        val error: List<TopAdsAutoTopError>? = listOf()
)

data class AutoTopupStatus (
        @SerializedName("status")
        val status: String? = "")

data class TopAdsAutoTopError (
        @SerializedName("Code")
        val errorCode: String? = "",
        @SerializedName("Title")
        val title: String? = "",
        @SerializedName("Detail")
        val detail: String? = "",
        @SerializedName("Object")
        val errorObject: TopadsAutoTopUpErrorObject? = TopadsAutoTopUpErrorObject()
)

data class TopadsAutoTopUpErrorObject (
        @SerializedName("Text")
        val errorTextList: List<String> = listOf(),
        @SerializedName("Type")
        val errorType: Int = 0
)
package com.tokopedia.shop.open.shop_open_revamp.data.model


import com.google.gson.annotations.SerializedName

data class SendSurveyData(
        @SerializedName("sendSurveyData")
        val sendSurveyData: Data = Data()
)

data class Data(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("success")
        val success: Boolean = true
)
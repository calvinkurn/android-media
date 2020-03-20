package com.tokopedia.thankyou_native.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckWhiteListStatusResponse(
        @SerializedName("checkWhiteListStatus")
        @Expose
        val checkWhiteListStatus: CheckWhiteListStatus?
)

data class CheckWhiteListStatus(
        @SerializedName("message")
        val message: String?,
        @SerializedName("status_code")
        val statusCode: Int,
        @SerializedName("data")
        val data: ArrayList<WhiteListData>?)

data class WhiteListData(
        @SerializedName("user_email")
        val userEmail: String?,
        @SerializedName("state")
        val state: Int?
)

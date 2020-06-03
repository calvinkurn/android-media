package com.tokopedia.payment.setting.authenticate.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckWhiteListResponse(
        @SerializedName("checkWhiteListStatus")
        @Expose
        var checkWhiteListStatus: CheckWhiteListStatus? = null
)


data class CheckWhiteListStatus(
        @SerializedName("message")
        @Expose
        var message: String?,
        @SerializedName("status_code")
        @Expose
        var statusCode: Int,
        @SerializedName("data")
        @Expose
        var data: List<WhiteListData>?
)


data class WhiteListData(
        @SerializedName("user_email")
        val userEmail: String?,
        @SerializedName("user_id")
        val userId: Long,
        @SerializedName("state")
        val state: Int,
        @SerializedName("Token")
        val token: String?
)




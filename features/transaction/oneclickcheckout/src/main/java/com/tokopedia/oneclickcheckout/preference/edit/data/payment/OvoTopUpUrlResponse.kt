package com.tokopedia.oneclickcheckout.preference.edit.data.payment

import com.google.gson.annotations.SerializedName

data class OvoTopUpUrlGqlResponse(
        @SerializedName("fetchInstantTopupURL")
        val response: OvoTopUpUrlResponse = OvoTopUpUrlResponse()
)

data class OvoTopUpUrlResponse(
        @SerializedName("success")
        val success: Boolean = false,
        @SerializedName("data")
        val data: OvoTopUpUrlData = OvoTopUpUrlData(),
        @SerializedName("errors")
        val errors: List<OvoTopUpUrlErrorData> = emptyList()
)

data class OvoTopUpUrlData(
        @SerializedName("redirectURL")
        val redirectURL: String = ""
)

data class OvoTopUpUrlErrorData(
        @SerializedName("code")
        val code: String = "",
        @SerializedName("message")
        val message: String = ""
)
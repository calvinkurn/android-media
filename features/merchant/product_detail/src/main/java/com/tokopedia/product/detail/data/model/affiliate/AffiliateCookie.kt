package com.tokopedia.product.detail.data.model.affiliate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AffiliateCookie(
        @SerializedName("createAffiliateCookie")
        @Expose
        val response: AffiliateCookieResponse = AffiliateCookieResponse()
) {
    companion object {
        private const val SUCCESS_STATUS = 1
    }

    fun isSuccess(): Boolean {
        return response.data.status == SUCCESS_STATUS
    }
}

data class AffiliateCookieResponse(
        @SerializedName("Data")
        @Expose
        val data: AffiliateCookieData = AffiliateCookieData(),

        @SerializedName("AffiliateUUID")
        @Expose
        val affiliateUuId: String = ""
)

data class AffiliateCookieData(
        @SerializedName("Status")
        @Expose
        val status: Int = 0,

        @SerializedName("Error")
        @Expose
        val error: AffiliateCookieError = AffiliateCookieError()
)

data class AffiliateCookieError(
        @SerializedName("ErrorType")
        @Expose
        val errorCode: Int = 0,

        @SerializedName("Message")
        @Expose
        val errorMessage: String = ""
)
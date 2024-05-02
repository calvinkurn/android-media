package com.tokopedia.checkoutpayment.topup.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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

@Parcelize
data class PaymentCustomerData(
    val name: String = "",
    val email: String = "",
    val msisdn: String = ""
) : Parcelable

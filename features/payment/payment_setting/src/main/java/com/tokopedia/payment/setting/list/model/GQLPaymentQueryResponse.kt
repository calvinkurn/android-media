package com.tokopedia.payment.setting.list.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class GQLPaymentQueryResponse(
        @SerializedName("PaymentQuery")
        @Expose
        var paymentQueryResponse: PaymentQueryResponse
)

data class PaymentQueryResponse(
        @SerializedName("paymentSignature")
        @Expose
        var paymentSignature: PaymentSignature,
        @SerializedName("creditCard")
        @Expose
        var creditCard: CreditCardData
)

@Parcelize
data class PaymentSignature(
        @SerializedName("merchantCode")
        var merchantCode: String,
        @SerializedName("profileCode")
        var profileCode: String,
        @SerializedName("ipAddress")
        var ipAddress: String,
        @SerializedName("date")
        var date: String,
        @SerializedName("userId")
        var userId: Int,
        @SerializedName("customerName")
        var customerName: String,
        @SerializedName("customerEmail")
        var customerEmail: String,
        @SerializedName("customerMsisdn")
        var customerMsisdn: String,
        @SerializedName("callbackUrl")
        var callbackUrl: String,
        @SerializedName("hash")
        var hash: String
) : Parcelable

data class CreditCardData(
        @SerializedName("error")
        @Expose
        val error: String,
        @SerializedName("cards")
        @Expose
        var cards: List<SettingListPaymentModel>? = null)


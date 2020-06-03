package com.tokopedia.payment.setting.list.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


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
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(merchantCode)
        parcel.writeString(profileCode)
        parcel.writeString(ipAddress)
        parcel.writeString(date)
        parcel.writeInt(userId)
        parcel.writeString(customerName)
        parcel.writeString(customerEmail)
        parcel.writeString(customerMsisdn)
        parcel.writeString(callbackUrl)
        parcel.writeString(hash)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaymentSignature> {
        override fun createFromParcel(parcel: Parcel): PaymentSignature {
            return PaymentSignature(parcel)
        }

        override fun newArray(size: Int): Array<PaymentSignature?> {
            return arrayOfNulls(size)
        }
    }
}

data class CreditCardData(
        @SerializedName("error")
        @Expose
        val error: String,
        @SerializedName("cards")
        @Expose
        var cards: List<SettingListPaymentModel>? = null)


package com.tokopedia.thankyou_native.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ThanksPageResponse(
        @SerializedName("thanksPageData")
        val thanksPageData: ThanksPageData
)

data class ThanksPageData(
        @SerializedName("payment_status")
        val paymentStatus: Int,
        @SerializedName("gateway_name")
        val gatewayName: String,
        @SerializedName("gateway_image")
        val gatewayImage: String,
        @SerializedName("expire_time_unix")
        val expireTimeUnix: Long,
        @SerializedName("amount")
        val amount: Long,
        @SerializedName("amount_str")
        val amountStr: String,
        @SerializedName("order_list")
        val orderList: ArrayList<OrderList>,
        @SerializedName("additional_info")
        val additionalInfo: AdditionalInfo,
        @SerializedName("how_to_pay")
        val howToPay: String,
        @SerializedName("whitelisted_rba")
        val whitelistedRBA: String,
        @SerializedName("payment_type")
        val paymentType: String,
        @SerializedName("expire_time_str")
        val expireTimeStr: String,
        @SerializedName("page_type")
        val pageType: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readLong(),
            parcel.readLong(),
            parcel.readString() ?: "",
            parcel.createTypedArrayList(OrderList) ?: arrayListOf(),
            parcel.readParcelable(AdditionalInfo::class.java.classLoader),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(paymentStatus)
        parcel.writeString(gatewayName)
        parcel.writeString(gatewayImage)
        parcel.writeLong(expireTimeUnix)
        parcel.writeLong(amount)
        parcel.writeString(amountStr)
        parcel.writeTypedList(orderList)
        parcel.writeParcelable(additionalInfo, flags)
        parcel.writeString(howToPay)
        parcel.writeString(whitelistedRBA)
        parcel.writeString(paymentType)
        parcel.writeString(expireTimeStr)
        parcel.writeString(pageType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ThanksPageData> {
        override fun createFromParcel(parcel: Parcel): ThanksPageData {
            return ThanksPageData(parcel)
        }

        override fun newArray(size: Int): Array<ThanksPageData?> {
            return arrayOfNulls(size)
        }
    }
}

data class AdditionalInfo(
        @SerializedName("account_number")
        val accountNumber: String,
        @SerializedName("account_dest")
        val accountDest: String,
        @SerializedName("bank_name")
        val bankName: String,
        @SerializedName("payment_code")
        val paymentCode: String,
        @SerializedName("masked_number")
        val maskedNumber: String,
        @SerializedName("installment_info")
        val installmentInfo: String,
        @SerializedName("interest")
        val interest: Float
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readFloat())


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accountNumber)
        parcel.writeString(accountDest)
        parcel.writeString(bankName)
        parcel.writeString(paymentCode)
        parcel.writeString(maskedNumber)
        parcel.writeString(installmentInfo)
        parcel.writeFloat(interest)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdditionalInfo> {
        override fun createFromParcel(parcel: Parcel): AdditionalInfo {
            return AdditionalInfo(parcel)
        }

        override fun newArray(size: Int): Array<AdditionalInfo?> {
            return arrayOfNulls(size)
        }
    }

}

data class OrderList(
        @SerializedName("store_name")
        val storeName: String,
        @SerializedName("item_list")
        val purchaseItemList: ArrayList<PurchaseItem>
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.createTypedArrayList(PurchaseItem) ?: arrayListOf())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(storeName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderList> {
        override fun createFromParcel(parcel: Parcel): OrderList {
            return OrderList(parcel)
        }

        override fun newArray(size: Int): Array<OrderList?> {
            return arrayOfNulls(size)
        }
    }

}

data class PurchaseItem(
        @SerializedName("product_name")
        val productName: String,
        @SerializedName("quantity")
        val quantity: Int,
        @SerializedName("weight")
        val weight: Double,
        @SerializedName("thumbnail_product")
        val thumbnailProduct: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productName)
        parcel.writeInt(quantity)
        parcel.writeDouble(weight)
        parcel.writeString(thumbnailProduct)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PurchaseItem> {
        override fun createFromParcel(parcel: Parcel): PurchaseItem {
            return PurchaseItem(parcel)
        }

        override fun newArray(size: Int): Array<PurchaseItem?> {
            return arrayOfNulls(size)
        }
    }

}
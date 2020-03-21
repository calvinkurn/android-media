package com.tokopedia.thankyou_native.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

//722246
//

data class ThanksPageResponse(
        @SerializedName("thanksPageData")
        val thanksPageData: ThanksPageData
)

data class ThanksPageData(
        @SerializedName("profile_code")
        val profileCode: String,
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
        val whitelistedRBA: Boolean,
        @SerializedName("payment_type")
        val paymentType: String,
        @SerializedName("expire_time_str")
        val expireTimeStr: String,
        @SerializedName("page_type")
        val pageType: String,
        @SerializedName("payment_items")
        val paymentItems: ArrayList<PaymentItem>?,
        @SerializedName("payment_deduction")
        val paymentDeductions: ArrayList<PaymentItem>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readLong(),
            parcel.readLong(),
            parcel.readString() ?: "",
            parcel.createTypedArrayList(OrderList) ?: arrayListOf(),
            parcel.readParcelable(AdditionalInfo::class.java.classLoader),
            parcel.readString() ?: "",
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.createTypedArrayList(PaymentItem) ?: arrayListOf(),
            parcel.createTypedArrayList(PaymentItem) ?: arrayListOf())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(profileCode)
        parcel.writeInt(paymentStatus)
        parcel.writeString(gatewayName)
        parcel.writeString(gatewayImage)
        parcel.writeLong(expireTimeUnix)
        parcel.writeLong(amount)
        parcel.writeString(amountStr)
        parcel.writeTypedList(orderList?.let { it }?: run { arrayListOf<OrderList>()})
        parcel.writeParcelable(additionalInfo, flags)
        parcel.writeString(howToPay)
        parcel.writeByte(if (whitelistedRBA) 1 else 0)
        parcel.writeString(paymentType)
        parcel.writeString(expireTimeStr)
        parcel.writeString(pageType)
        parcel.writeTypedList(paymentItems?.let { it }?: run { arrayListOf<PaymentItem>()})
        parcel.writeTypedList(paymentDeductions?.let { it }?: run { arrayListOf<PaymentItem>()})


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
            parcel.readFloat()) {
    }

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
        @SerializedName("order_id")
        val orderId: String,
        @SerializedName("store_id")
        val storeId: String,
        @SerializedName("store_type")
        val storeType: String,
        @SerializedName("logistic_type")
        val logisticType: String,
        @SerializedName("store_name")
        val storeName: String,
        @SerializedName("item_list")
        val purchaseItemList: ArrayList<PurchaseItem>,
        @SerializedName("shipping_amount")
        val shippingAmount: Float,
        @SerializedName("shipping_amount_str")
        val shippingAmountStr: String,
        @SerializedName("shipping_desc")
        val shippingDesc: String,
        @SerializedName("insurance_amount")
        val insuranceAmount: Float,
        @SerializedName("insurance_amount_str")
        val insuranceAmountStr: String,
        @SerializedName("address")
        val address: String,
        @SerializedName("promo_data")
        val promoData: ArrayList<PromoData>?,
        @SerializedName("tax")
        val tax: Long,
        @SerializedName("coupon")
        val coupon: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.createTypedArrayList(PurchaseItem) ?: arrayListOf(),
            parcel.readFloat(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.createTypedArrayList(PromoData) ?: arrayListOf(),
            parcel.readLong(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(orderId)
        parcel.writeString(storeId)
        parcel.writeString(storeType)
        parcel.writeString(logisticType)
        parcel.writeString(storeName)
        parcel.writeTypedList(purchaseItemList?.let { purchaseItemList }?:run { arrayListOf<PurchaseItem>() })
        parcel.writeFloat(shippingAmount)
        parcel.writeString(shippingAmountStr)
        parcel.writeString(shippingDesc)
        parcel.writeFloat(insuranceAmount)
        parcel.writeString(insuranceAmountStr)
        parcel.writeString(address)
        parcel.writeTypedList(promoData?.let { promoData }?:run { arrayListOf<PromoData>() })
        parcel.writeLong(tax)
        parcel.writeString(coupon)
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

data class PromoData(
        @SerializedName("promo_code")
        val promoCode: String,
        @SerializedName("promo_desc")
        val promoDesc: String,
        @SerializedName("total_cashback")
        val totalCashback: Float,
        @SerializedName("total_cashback_str")
        val totalCashbackStr: String,
        @SerializedName("total_discount")
        val totalDiscount: Float,
        @SerializedName("total_discount_str")
        val totalDiscountStr: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(promoCode)
        parcel.writeString(promoDesc)
        parcel.writeFloat(totalCashback)
        parcel.writeString(totalCashbackStr)
        parcel.writeFloat(totalDiscount)
        parcel.writeString(totalDiscountStr)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PromoData> {
        override fun createFromParcel(parcel: Parcel): PromoData {
            return PromoData(parcel)
        }

        override fun newArray(size: Int): Array<PromoData?> {
            return arrayOfNulls(size)
        }
    }
}

data class PurchaseItem(
        @SerializedName("product_id")
        val productId: String,
        @SerializedName("product_name")
        val productName: String,
        @SerializedName("product_brand")
        val productBrand: String,
        @SerializedName("price")
        val price: Float,
        @SerializedName("price_str")
        val priceStr: String,
        @SerializedName("quantity")
        val quantity: Int,
        @SerializedName("weight")
        val weight: Float,
        @SerializedName("weight_unit")
        val weightUnit: String,
        @SerializedName("total_price")
        val totalPrice: Float,
        @SerializedName("total_price_str")
        val totalPriceStr: String,
        @SerializedName("promo_code")
        val promoCode: String,
        @SerializedName("category")
        val category: String,
        @SerializedName("variant")
        val variant: String,
        @SerializedName("thumbnail_product")
        val thumbnailProduct: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readString() ?: "",
            parcel.readInt(),
            parcel.readFloat(),
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(productName)
        parcel.writeString(productBrand)
        parcel.writeFloat(price)
        parcel.writeString(priceStr)
        parcel.writeInt(quantity)
        parcel.writeFloat(weight)
        parcel.writeString(weightUnit)
        parcel.writeFloat(totalPrice)
        parcel.writeString(totalPriceStr)
        parcel.writeString(promoCode)
        parcel.writeString(category)
        parcel.writeString(variant)
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

data class PaymentItem(
        @SerializedName("item_name")
        val itemName: String,
        @SerializedName("item_desc")
        val itemDesc: String,
        @SerializedName("amount")
        val amount: Float,
        @SerializedName("amount_str")
        val amountStr: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(itemName)
        parcel.writeString(itemDesc)
        parcel.writeFloat(amount)
        parcel.writeString(amountStr)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaymentItem> {
        override fun createFromParcel(parcel: Parcel): PaymentItem {
            return PaymentItem(parcel)
        }

        override fun newArray(size: Int): Array<PaymentItem?> {
            return arrayOfNulls(size)
        }
    }
}
package com.tokopedia.purchase_platform.features.promo.data.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class CouponListRecommendationRequest(
        @SerializedName("promo")
        var promoRequest: PromoRequest = PromoRequest()
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable(PromoRequest::class.java.classLoader)
            ?: PromoRequest()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(promoRequest, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CouponListRecommendationRequest> {
        override fun createFromParcel(parcel: Parcel): CouponListRecommendationRequest {
            return CouponListRecommendationRequest(parcel)
        }

        override fun newArray(size: Int): Array<CouponListRecommendationRequest?> {
            return arrayOfNulls(size)
        }
    }

}

data class PromoRequest(
        @SerializedName("codes")
        var codes: ArrayList<String> = ArrayList(),
        @SerializedName("skip_apply")
        var skipApply: Int = 1,
        @SerializedName("is_suggested")
        var isSuggested: Int = 1,
        @SerializedName("cart_type")
        var cartType: String = "default", // ocs & default
        @SerializedName("state")
        var state: String = "", // cart & checkout & occ
        @SerializedName("orders")
        var orders: List<Order> = emptyList()
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.createStringArrayList() ?: ArrayList(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.createTypedArrayList(Order) ?: emptyList()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(codes)
        parcel.writeInt(skipApply)
        parcel.writeInt(isSuggested)
        parcel.writeString(cartType)
        parcel.writeString(state)
        parcel.writeTypedList(orders)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PromoRequest> {
        override fun createFromParcel(parcel: Parcel): PromoRequest {
            return PromoRequest(parcel)
        }

        override fun newArray(size: Int): Array<PromoRequest?> {
            return arrayOfNulls(size)
        }
    }
}

data class Order(
        @SerializedName("shop_id")
        var shopId: Long = 0,
        @SerializedName("unique_id")
        var uniqueId: String = "",
        @SerializedName("product_details")
        var product_details: List<ProductDetail> = emptyList(),
        @SerializedName("codes")
        var codes: ArrayList<String> = ArrayList(),
        @SerializedName("is_checked")
        var isChecked: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString() ?: "",
            parcel.createTypedArrayList(ProductDetail) ?: emptyList(),
            parcel.createStringArrayList() ?: ArrayList(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(shopId)
        parcel.writeString(uniqueId)
        parcel.writeTypedList(product_details)
        parcel.writeStringList(codes)
        parcel.writeByte(if (isChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }

}

data class ProductDetail(
        @SerializedName("product_id")
        var productId: Long = 0,
        @SerializedName("quantity")
        var quantity: Int = -1
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(productId)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductDetail> {
        override fun createFromParcel(parcel: Parcel): ProductDetail {
            return ProductDetail(parcel)
        }

        override fun newArray(size: Int): Array<ProductDetail?> {
            return arrayOfNulls(size)
        }
    }

}
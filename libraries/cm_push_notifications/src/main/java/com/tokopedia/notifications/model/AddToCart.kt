package com.tokopedia.notifications.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

data class AddToCart(
        @Expose @SerializedName(CMConstant.PayloadKeys.PRODUCT_ID) var productId: Int? = 0,
        @Expose @SerializedName(CMConstant.PayloadKeys.PRODUCT_NAME) var productName: String? = "",
        @Expose @SerializedName(CMConstant.PayloadKeys.PRODUCT_BRAND) var productBrand: String? = "",
        @Expose @SerializedName(CMConstant.PayloadKeys.PRODUCT_PRICE) var productPrice: Double? = 0.0,
        @Expose @SerializedName(CMConstant.PayloadKeys.PRODUCT_VARIANT) var productVariant: String? = "",
        @Expose @SerializedName(CMConstant.PayloadKeys.PRODUCT_QUANTITY) var productQuantity: String? = "",
        @Expose @SerializedName(CMConstant.PayloadKeys.ATC_SHOP_ID) var shopId: Int? = 0,
        @Expose @SerializedName(CMConstant.PayloadKeys.SHOP_NAME) var shopName: String? = "",
        @Expose @SerializedName(CMConstant.PayloadKeys.SHOP_TYPE) var shopType: String? = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(productId?: 0)
        parcel.writeString(productName)
        parcel.writeString(productBrand)
        parcel.writeDouble(productPrice?: 0.0)
        parcel.writeString(productVariant)
        parcel.writeString(productQuantity)
        parcel.writeInt(shopId?: 0)
        parcel.writeString(shopName)
        parcel.writeString(shopType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AddToCart> {
        override fun createFromParcel(parcel: Parcel): AddToCart {
            return AddToCart(parcel)
        }

        override fun newArray(size: Int): Array<AddToCart?> {
            return arrayOfNulls(size)
        }
    }

}
package com.tokopedia.logisticcart.shipping.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-09-30.
 */

data class Product(
        @SerializedName("product_id")
        var productId: Long = 0,

        @SerializedName("is_free_shipping")
        var isFreeShipping: Boolean = false,

        @SerializedName("is_free_shipping_tc")
        var isFreeShippingTc: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(productId)
        parcel.writeByte(if (isFreeShipping) 1 else 0)
        parcel.writeByte(if (isFreeShippingTc) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
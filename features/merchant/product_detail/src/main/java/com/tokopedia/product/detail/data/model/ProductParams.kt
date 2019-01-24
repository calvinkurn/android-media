package com.tokopedia.product.detail.data.model

import android.os.Parcel
import android.os.Parcelable

data class ProductParams(var productId: String? = null,
                         var shopDomain: String? = null,
                         var productName: String? = null,
                         var productPrice: String? = null,
                         var productImage: String? = null): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productId)
        parcel.writeString(shopDomain)
        parcel.writeString(productName)
        parcel.writeString(productPrice)
        parcel.writeString(productImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductParams> {
        override fun createFromParcel(parcel: Parcel): ProductParams {
            return ProductParams(parcel)
        }

        override fun newArray(size: Int): Array<ProductParams?> {
            return arrayOfNulls(size)
        }
    }


}
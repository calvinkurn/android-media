package com.tokopedia.merchantvoucher.common.gql.data.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-11-14.
 */

data class CartItemDataVoucher(
        @SerializedName("product_id")
        var productId: Int = 0,

        @SerializedName("product_name")
        var productName: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(productId)
        parcel.writeString(productName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItemDataVoucher> {
        override fun createFromParcel(parcel: Parcel): CartItemDataVoucher {
            return CartItemDataVoucher(parcel)
        }

        override fun newArray(size: Int): Array<CartItemDataVoucher?> {
            return arrayOfNulls(size)
        }
    }
}
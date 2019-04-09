package com.tokopedia.promocheckout.common.data.entity.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 19/03/19.
 */

data class ProductDetail(
        @SerializedName("product_id")
        var productId: Int? = 0,

        @SerializedName("quantity")
        var quantity: Int? = 0
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readValue(Int::class.java.classLoader) as? Int) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeValue(productId)
                parcel.writeValue(quantity)
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
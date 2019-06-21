package com.tokopedia.promocheckout.common.data.entity.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 19/03/19.
 */

data class Order(
        @SerializedName("shop_id")
        var shopId: Int? = 0,

        @SerializedName("unique_id")
        var uniqueId: String? = null,

        @SerializedName("product_details")
        var productDetails: ArrayList<ProductDetail>? = null,

        @SerializedName("codes")
        var codes: ArrayList<String>? = null,

        @SerializedName("shipping_id")
        var shippingId: Int? = null,

        @SerializedName("sp_id")
        var spId: Int? = null,

        @SerializedName("is_insurance_price")
        var isInsurancePrice: Int? = null
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readString(),
                arrayListOf<ProductDetail>().apply {
                        parcel.readList(this, ProductDetail::class.java.classLoader)
                },
                arrayListOf<String>().apply {
                        parcel.readList(this, String::class.java.classLoader)
                },
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readValue(Int::class.java.classLoader) as? Int,
                parcel.readValue(Int::class.java.classLoader) as? Int) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeValue(shopId)
                parcel.writeString(uniqueId)
                parcel.writeList(productDetails)
                parcel.writeList(codes)
                parcel.writeValue(shippingId)
                parcel.writeValue(spId)
                parcel.writeValue(isInsurancePrice)
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
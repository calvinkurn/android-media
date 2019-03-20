package com.tokopedia.promocheckout.common.data.entity.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 19/03/19.
 */

data class CheckPromoFirstStepParam(
        @SerializedName("codes")
        var codes: ArrayList<String>? = null,

        @SerializedName("cart_type")
        var cartType: String? = null,

        @SerializedName("skip_apply")
        var skipApply: Int? = 0,

        @SerializedName("is_suggested")
        var isSuggested: Int? = 0,

        @SerializedName("orders")
        var orders: ArrayList<Order>? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            arrayListOf<String>().apply {
                parcel.readList(this, String::class.java.classLoader)
            },
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            arrayListOf<Order>().apply {
                parcel.readList(this, Order::class.java.classLoader)
            }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(codes)
        parcel.writeString(cartType)
        parcel.writeValue(skipApply)
        parcel.writeValue(isSuggested)
        parcel.writeList(orders)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckPromoFirstStepParam> {
        override fun createFromParcel(parcel: Parcel): CheckPromoFirstStepParam {
            return CheckPromoFirstStepParam(parcel)
        }

        override fun newArray(size: Int): Array<CheckPromoFirstStepParam?> {
            return arrayOfNulls(size)
        }
    }
}
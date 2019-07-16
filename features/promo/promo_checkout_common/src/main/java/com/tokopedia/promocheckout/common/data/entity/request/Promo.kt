package com.tokopedia.promocheckout.common.data.entity.request

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.transactiondata.entity.request.TokopediaCornerData

/**
 * Created by Irfan Khoirul on 19/03/19.
 */

data class Promo(
        @SerializedName("codes")
        var codes: ArrayList<String>? = null,

        @SerializedName("current_apply_code")
        var currentApplyCode: CurrentApplyCode? = null,

        @SerializedName("cart_type")
        var cartType: String? = null,

        @SerializedName("skip_apply")
        var skipApply: Int? = 0,

        @SerializedName("is_suggested")
        var isSuggested: Int? = 0,

        @SerializedName("orders")
        var orders: ArrayList<Order>? = null,

        @SerializedName("is_trade_in")
        var isTradeIn: Int? = 0,

        @SerializedName("state")
        var state: String = "",

        @SerializedName("tokopedia_corner_data")
        var tokopediCornerData: TokopediaCornerData? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            arrayListOf<String>().apply {
                parcel.readList(this, String::class.java.classLoader)
            },
            parcel.readParcelable(CurrentApplyCode::class.java.classLoader),
            parcel.readString(),
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readValue(Int::class.java.classLoader) as? Int,
            arrayListOf<Order>().apply {
                parcel.readList(this, Order::class.java.classLoader)
            },
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString() ?: "",
            parcel.readParcelable(TokopediaCornerData::class.java.classLoader) ?: null
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(codes)
        parcel.writeParcelable(currentApplyCode, flags)
        parcel.writeString(cartType)
        parcel.writeValue(skipApply)
        parcel.writeValue(isSuggested)
        parcel.writeList(orders)
        parcel.writeValue(skipApply)
        parcel.writeString(state)
        parcel.writeParcelable(tokopediCornerData, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Promo> {
        val STATE_CART = "cart"
        val STATE_CHECKOUT = "checkout"

        val CART_TYPE_DEFAULT = "default"
        val CART_TYPE_OCS = "ocs"

        override fun createFromParcel(parcel: Parcel): Promo {
            return Promo(parcel)
        }

        override fun newArray(size: Int): Array<Promo?> {
            return arrayOfNulls(size)
        }
    }
}
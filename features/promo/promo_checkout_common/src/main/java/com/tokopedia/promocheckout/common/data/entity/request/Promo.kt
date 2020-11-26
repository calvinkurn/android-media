package com.tokopedia.promocheckout.common.data.entity.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 19/03/19.
 */
@Parcelize
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

        @SerializedName("is_trade_in_drop_off")
        var isTradeInDropOff: Int? = 0,

        @SerializedName("state")
        var state: String = ""
) : Parcelable
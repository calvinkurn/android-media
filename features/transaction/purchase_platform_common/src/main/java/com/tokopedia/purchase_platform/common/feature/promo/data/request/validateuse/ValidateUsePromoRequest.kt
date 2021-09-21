package com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
@Parcelize
data class ValidateUsePromoRequest(

        @field:SerializedName("codes")
        var codes: MutableList<String?> = mutableListOf(),

        @field:SerializedName("is_suggested")
        var isSuggested: Int = 1,

        @field:SerializedName("is_trade_in")
        var isTradeIn: Int = 0,

        @field:SerializedName("is_trade_in_drop_off")
        var isTradeInDropOff: Int = 0,

        @field:SerializedName("orders")
        var orders: List<OrdersItem?> = listOf(),

        @field:SerializedName("skip_apply")
        var skipApply: Int = 1,

        @field:SerializedName("cart_type")
        var cartType: String = "", // ocs & default & occ

        @field:SerializedName("state")
        var state: String = "" // cart & checkout
) : Parcelable
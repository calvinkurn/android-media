package com.tokopedia.purchase_platform.features.promo.data.request.validate_use

import android.os.Parcelable
import javax.annotation.Generated
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Generated("com.robohorse.robopojogenerator")
@Parcelize
data class ValidateUsePromoRequest(

        @field:SerializedName("codes")
        var codes: MutableList<String?> = mutableListOf(),

        @field:SerializedName("is_suggested")
        var isSuggested: Int = 1,

        @field:SerializedName("is_trade_in")
        var isTradeIn: Int = 1,

        @field:SerializedName("is_trade_in_drop_off")
        var isTradeInDropOff: Int = 1,

        @field:SerializedName("orders")
        var orders: List<OrdersItem?> = listOf(),

        @field:SerializedName("skip_apply")
        var skipApply: Int = 1,

        @field:SerializedName("cart_type")
        var cartType: String = "", // ocs & default

        @field:SerializedName("state")
        var state: String = "" // cart & checkout & occ
) : Parcelable
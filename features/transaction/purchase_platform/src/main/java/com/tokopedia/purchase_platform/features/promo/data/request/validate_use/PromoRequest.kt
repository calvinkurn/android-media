package com.tokopedia.purchase_platform.features.promo.data.request.validate_use

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class PromoRequest(

        @field:SerializedName("codes")
        var codes: List<String?> = listOf(),

        @field:SerializedName("is_suggested")
        var isSuggested: Int = -1,

        @field:SerializedName("orders")
        var orders: List<OrdersItem?> = listOf(),

        @field:SerializedName("skip_apply")
        var skipApply: Int = -1,

        @field:SerializedName("cart_type")
        var cartType: String = "",

        @field:SerializedName("state")
        var state: String = ""
)
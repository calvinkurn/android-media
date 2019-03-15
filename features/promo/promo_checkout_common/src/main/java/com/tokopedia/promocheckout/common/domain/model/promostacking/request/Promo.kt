package com.tokopedia.promocheckout.common.domain.model.promostacking.request

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Promo(

		@field:SerializedName("promo")
	var promo: Promo? = null,

		@field:SerializedName("codes")
	var codes: List<String?>? = null,

		@field:SerializedName("is_suggested")
	var isSuggested: Int? = null,

		@field:SerializedName("orders")
	var orders: List<OrdersItem?>? = null,

		@field:SerializedName("skip_apply")
	var skipApply: Int? = null,

		@field:SerializedName("cart_type")
	var cartType: String? = null
)
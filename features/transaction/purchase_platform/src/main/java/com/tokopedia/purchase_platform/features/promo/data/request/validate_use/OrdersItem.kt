package com.tokopedia.purchase_platform.features.promo.data.request.validate_use

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class OrdersItem(

	@field:SerializedName("shipping_id")
	var shippingId: Int? = -1,

	@field:SerializedName("shop_id")
	var shopId: Int? = -1,

	@field:SerializedName("codes")
	var codes: List<Any?>? = listOf(),

	@field:SerializedName("unique_id")
	var uniqueId: String? = "",

	@field:SerializedName("sp_id")
	var spId: Int? = -1,

	@field:SerializedName("product_details")
	var productDetails: List<ProductDetailsItem?>? = listOf(),

	@SerializedName("is_checked")
	var isChecked: Boolean = false
)
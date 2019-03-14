package com.tokopedia.promocheckout.common.domain.model.promostacking

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class OrdersItem(

	@field:SerializedName("shop_id")
	val shopId: Int? = null,

	@field:SerializedName("codes")
	val codes: List<String?>? = null,

	@field:SerializedName("unique_id")
	val uniqueId: String? = null,

	@field:SerializedName("product_details")
	val productDetails: List<ProductDetailsItem?>? = null
)
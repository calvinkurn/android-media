package com.tokopedia.promocheckout.common.domain.model.promostacking.request

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class ProductDetailsItem(

	@field:SerializedName("quantity")
	var quantity: Int? = null,

	@field:SerializedName("product_id")
	var productId: Int? = null
)
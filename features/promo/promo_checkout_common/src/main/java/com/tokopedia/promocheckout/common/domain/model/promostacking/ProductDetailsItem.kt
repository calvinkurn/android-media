package com.tokopedia.promocheckout.common.domain.model.promostacking

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ProductDetailsItem(

	@field:SerializedName("quantity")
	var quantity: Int? = null,

	@field:SerializedName("product_id")
	var productId: Int? = null
)
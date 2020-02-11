package com.tokopedia.discovery2.data.cpmtopads

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ProductItem(

	@field:SerializedName("price_format")
	val priceFormat: String? = null,

	@field:SerializedName("applinks")
	var applinks: String? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("image_product")
	var imageProduct: ImageProduct? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("button_text")
	var buttonText: String? = ""

)
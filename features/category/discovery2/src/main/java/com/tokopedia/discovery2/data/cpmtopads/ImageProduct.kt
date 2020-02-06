package com.tokopedia.discovery2.data.cpmtopads

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ImageProduct(

	@field:SerializedName("image_url")
	var imageUrl: String? = null,

	@field:SerializedName("image_click_url")
	val imageClickUrl: String? = null
)
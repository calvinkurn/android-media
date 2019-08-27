package com.tokopedia.discovery.categoryrevamp.data.topAds

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ProductAds(

	@field:SerializedName("template")
	val template: List<Any?>? = null,

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("status")
	val status: Status? = null
)
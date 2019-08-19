package com.tokopedia.discovery.categoryrevamp.data.productModel

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Related(

	@field:SerializedName("otherRelated")
	val otherRelated: List<Any?>? = null,

	@field:SerializedName("relatedKeyword")
	val relatedKeyword: String? = null
)
package com.tokopedia.common_category.model.productModel

import com.google.gson.annotations.SerializedName

data class Related(

	@field:SerializedName("otherRelated")
	val otherRelated: List<Any?>? = null,

	@field:SerializedName("relatedKeyword")
	val relatedKeyword: String? = null
)
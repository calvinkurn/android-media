package com.tokopedia.common_category.model.productModel

import com.google.gson.annotations.SerializedName

data class Suggestion(

	@field:SerializedName("insteadCount")
	val insteadCount: Int? = null,

	@field:SerializedName("currentKeyword")
	val currentKeyword: String? = null,

	@field:SerializedName("suggestion")
	val suggestion: String? = null,

	@field:SerializedName("suggestionCount")
	val suggestionCount: Int? = null,

	@field:SerializedName("instead")
	val instead: String? = null,

	@field:SerializedName("suggestionTextQuery")
	val suggestionTextQuery: String? = null,

	@field:SerializedName("suggestionText")
	val suggestionText: String? = null
)
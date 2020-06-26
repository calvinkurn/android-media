package com.tokopedia.common_category.model.productModel

import com.google.gson.annotations.SerializedName

data class Redirection(

	@field:SerializedName("departmentID")
	val departmentID: Int? = null,

	@field:SerializedName("redirectionURL")
	val redirectionURL: String? = null
)
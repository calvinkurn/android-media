package com.tokopedia.discovery.categoryrevamp.data.productModel

import com.google.gson.annotations.SerializedName

data class Redirection(

	@field:SerializedName("departmentID")
	val departmentID: Int? = null,

	@field:SerializedName("redirectionURL")
	val redirectionURL: String? = null
)
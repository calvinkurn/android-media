package com.tokopedia.common_category.model.productModel

import com.google.gson.annotations.SerializedName

data class SearchProduct(

	@field:SerializedName("totalData")
	var totalData: Int? = null,

	@field:SerializedName("isQuerySafe")
	val isQuerySafe: Boolean? = null,

	@field:SerializedName("additionalParams")
	val additionalParams: String? = null,

	@field:SerializedName("count_text")
	val countText: String? = null,

	@field:SerializedName("related")
	val related: Related? = null,

	@field:SerializedName("catalogs")
	val catalogs: List<Any?>? = null,

	@field:SerializedName("suggestion")
	val suggestion: Suggestion? = null,

	@field:SerializedName("source")
	val source: String? = null,

	@field:SerializedName("redirection")
	val redirection: Redirection? = null,

	@field:SerializedName("products")
	val products: ArrayList<ProductsItem?> = ArrayList(),

	@field:SerializedName("errorMessage")
	val errorMessage: String? = null,

	@field:SerializedName("lite_url")
	val liteUrl: String? = null
)
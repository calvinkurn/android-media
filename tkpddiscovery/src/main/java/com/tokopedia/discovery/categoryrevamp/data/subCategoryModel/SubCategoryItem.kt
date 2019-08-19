package com.tokopedia.discovery.categoryrevamp.data.subCategoryModel

import com.google.gson.annotations.SerializedName

data class SubCategoryItem(

	@field:SerializedName("isForAdult")
	val isForAdult: Int? = null,

	@field:SerializedName("applinks")
	val applinks: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("thumbnailImage")
	val thumbnailImage: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)
package com.tokopedia.discovery.categoryrevamp.data.subCategoryModel

import com.google.gson.annotations.SerializedName

data class CategoryDetailQuery(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("header")
	val header: Header? = null
)
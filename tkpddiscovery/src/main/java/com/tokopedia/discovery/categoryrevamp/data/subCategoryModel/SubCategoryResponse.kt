package com.tokopedia.discovery.categoryrevamp.data.subCategoryModel

import com.google.gson.annotations.SerializedName

data class SubCategoryResponse(

	@field:SerializedName("CategoryDetailQuery")
	val categoryDetailQuery: CategoryDetailQuery? = null
)
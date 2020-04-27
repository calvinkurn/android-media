package com.tokopedia.browse.categoryNavigation.data.model.category

import com.google.gson.annotations.SerializedName

data class CategoryAllList(

	@field:SerializedName("categories")
	val categories: List<CategoriesItem?>? = null
)
package com.tokopedia.tokopoints.view.model.addpointsection

import com.google.gson.annotations.SerializedName

data class SectionsItem(

	@SerializedName("categories")
	val categories: List<CategoriesItem?>? = null,

	@SerializedName("title")
	val title: String? = null
)

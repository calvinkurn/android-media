package com.tokopedia.navigation_common.category.model

import com.google.gson.annotations.SerializedName


data class CategoryConfigModel(
		@field:SerializedName("homeFlag")
		val homeFlag: HomeFlag? = null
)

package com.tokopedia.navigation_common.category.model

import com.google.gson.annotations.SerializedName


data class Response(
		@field:SerializedName("data")
		val data: CategoryConfigModel? = null
)

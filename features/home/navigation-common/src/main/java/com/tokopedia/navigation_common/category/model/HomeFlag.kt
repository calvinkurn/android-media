package com.tokopedia.navigation_common.category.model

import com.google.gson.annotations.SerializedName

data class HomeFlag(
		@field:SerializedName("isRevampBelanja")
		val isRevampBelanja: Boolean = false
)

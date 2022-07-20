package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import com.google.gson.annotations.SerializedName

data class DetailsItem(

	@field:SerializedName("amount")
	val amount: Int = 0,

	@field:SerializedName("section_name")
	val sectionName: String = "",

	@field:SerializedName("description")
	val description: String = "",

	@field:SerializedName("type")
	val type: String = "",

	@field:SerializedName("amount_str")
	val amountStr: String = "",

	@field:SerializedName("points")
	val points: Int = 0,

	@field:SerializedName("points_str")
	val pointsStr: String = ""
)
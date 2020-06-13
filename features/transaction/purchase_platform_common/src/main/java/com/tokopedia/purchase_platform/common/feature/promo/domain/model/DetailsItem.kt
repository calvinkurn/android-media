package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class DetailsItem(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("section_name")
	val sectionName: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("amount_str")
	val amountStr: String? = null,

	@field:SerializedName("points")
	val points: Int? = null,

	@field:SerializedName("points_str")
	val pointsStr: String? = null
)
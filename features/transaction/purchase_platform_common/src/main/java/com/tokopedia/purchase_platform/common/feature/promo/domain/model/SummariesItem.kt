package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class SummariesItem(

        @field:SerializedName("amount")
	val amount: Int? = null,

        @field:SerializedName("section_name")
	val sectionName: String? = null,

        @field:SerializedName("description")
	val description: String? = null,

        @field:SerializedName("details")
	val details: List<DetailsItem?>? = null,

        @field:SerializedName("section_description")
	val sectionDescription: String? = null,

        @field:SerializedName("type")
	val type: String? = null,

        @field:SerializedName("amount_str")
	val amountStr: String? = null
)
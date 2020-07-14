package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class BenefitSummaryInfo(

	@field:SerializedName("final_benefit_amount_str")
	val finalBenefitAmountStr: String = "",

	@field:SerializedName("final_benefit_amount")
	val finalBenefitAmount: Int = 0,

	@field:SerializedName("final_benefit_text")
	val finalBenefitText: String = "",

	@field:SerializedName("summaries")
	val summaries: List<SummariesItem> = emptyList()
)
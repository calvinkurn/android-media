package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class BenefitSummaryInfo(

	@field:SerializedName("final_benefit_amount_str")
	val finalBenefitAmountStr: String? = null,

	@field:SerializedName("final_benefit_amount")
	val finalBenefitAmount: Int? = null,

	@field:SerializedName("final_benefit_text")
	val finalBenefitText: String? = null,

	@field:SerializedName("summaries")
	val summaries: List<SummariesItem?>? = null
)
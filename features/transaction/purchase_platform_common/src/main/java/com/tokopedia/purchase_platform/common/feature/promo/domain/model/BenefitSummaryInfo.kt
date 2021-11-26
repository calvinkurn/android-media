package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

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
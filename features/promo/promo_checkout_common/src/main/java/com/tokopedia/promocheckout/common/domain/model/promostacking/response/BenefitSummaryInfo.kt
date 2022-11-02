package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class BenefitSummaryInfo(

	@field:SerializedName("final_benefit_amount_str")
	val finalBenefitAmountStr: String = "",

	@field:SerializedName("final_benefit_amount")
	val finalBenefitAmount: Long = 0L,

	@field:SerializedName("final_benefit_text")
	val finalBenefitText: String = "",

	@field:SerializedName("summaries")
	val summaries: List<SummariesItem> = ArrayList()
)
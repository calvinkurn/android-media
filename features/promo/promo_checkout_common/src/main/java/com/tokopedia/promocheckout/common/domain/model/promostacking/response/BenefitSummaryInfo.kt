package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class BenefitSummaryInfo(

	@field:SerializedName("final_benefit_amount")
	val finalBenefitAmount: String = "",

	@field:SerializedName("final_benefit_text")
	val finalBenefitText: String = "",

	@field:SerializedName("summaries")
	val summaries: List<SummariesItem> = ArrayList()
)
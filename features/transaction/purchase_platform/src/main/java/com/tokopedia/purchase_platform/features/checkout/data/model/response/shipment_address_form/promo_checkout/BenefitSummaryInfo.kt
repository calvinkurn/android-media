package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout

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
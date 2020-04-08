package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class SummariesItem(

	@field:SerializedName("amount")
	val amount: Int = 0,

	@field:SerializedName("description")
	val description: String = "",

	@field:SerializedName("type")
	val type: String = "",

	@field:SerializedName("amount_str")
	val amountStr: String = "",

	@field:SerializedName("details")
	val details: List<Detail>
)
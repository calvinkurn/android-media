package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class SummariesItem(

	@field:SerializedName("amount")
	val amount: Int = 0,

	@field:SerializedName("description")
	val description: String = "",

	@field:SerializedName("type")
	val type: String = "",

	@field:SerializedName("amount_str")
	val amountStr: String = ""
)
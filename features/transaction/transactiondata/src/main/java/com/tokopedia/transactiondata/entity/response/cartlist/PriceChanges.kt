package com.tokopedia.transactiondata.entity.response.cartlist

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class PriceChanges(

	@field:SerializedName("amount_difference")
	val amountDifference: Int = 0,

	@field:SerializedName("changes_state")
	val changesState: Int = 0,

	@field:SerializedName("original_amount")
	val originalAmount: Int = 0,

	@field:SerializedName("description")
	val description: String = ""
)
package com.tokopedia.purchase_platform.common.data.model.response.validateuse

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class CashbackDetailsItem(

	@field:SerializedName("benefit_type")
	val benefitType: String = "",

	@field:SerializedName("amount_points")
	val amountPoints: Int = 0,

	@field:SerializedName("amount_idr")
	val amountIdr: Int = 0
)
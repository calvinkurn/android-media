package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class BenefitProductDetailsItem(

	@field:SerializedName("cashback_amount_idr")
	val cashbackAmountIdr: Int = 0,

	@field:SerializedName("cashback_amount")
	val cashbackAmount: Int = 0,

	@field:SerializedName("discount_amount")
	val discountAmount: Int = 0,

	@field:SerializedName("product_id")
	val productId: Int = 0,

	@field:SerializedName("is_bebas_ongkir")
	val isBebasOngkir: Boolean = false
)
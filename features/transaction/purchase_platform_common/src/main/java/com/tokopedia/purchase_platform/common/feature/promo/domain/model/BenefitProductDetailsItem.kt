package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class BenefitProductDetailsItem(

	@field:SerializedName("cashback_amount")
	val cashbackAmount: Int? = null,

	@field:SerializedName("discount_amount")
	val discountAmount: Int? = null,

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("is_bebas_ongkir")
	val isBebasOngkir: Boolean? = null
)
package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class BenefitDetailsItem(

		@field:SerializedName("code")
	val code: String? = null,

		@field:SerializedName("unique_id")
	val uniqueId: String? = null,

		@field:SerializedName("cashback_amount")
	val cashbackAmount: Int? = null,

		@field:SerializedName("promo_type")
	val promoType: PromoType? = null,

		@field:SerializedName("discount_amount")
	val discountAmount: Int? = null,

		@field:SerializedName("benefit_product_details")
	val benefitProductDetails: List<BenefitProductDetailsItem?>? = null,

		@field:SerializedName("type")
	val type: String? = null
)
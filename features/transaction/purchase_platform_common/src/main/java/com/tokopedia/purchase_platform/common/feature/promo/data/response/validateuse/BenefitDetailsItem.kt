package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class BenefitDetailsItem(

	@field:SerializedName("code")
	val code: String = "",

	@field:SerializedName("unique_id")
	val uniqueId: String = "",

	@field:SerializedName("cashback_amount")
	val cashbackAmount: Int = 0,

	@field:SerializedName("promo_type")
	val promoType: PromoType = PromoType(),

	@field:SerializedName("discount_amount")
	val discountAmount: Int = 0,

	@field:SerializedName("cashback_details")
	val cashbackDetails: List<CashbackDetailsItem> = emptyList(),

	@field:SerializedName("discount_details")
	val discountDetails: List<DiscountDetailsItem> = emptyList(),

	@field:SerializedName("benefit_product_details")
	val benefitProductDetails: List<BenefitProductDetailsItem> = emptyList(),

	@field:SerializedName("type")
	val type: String = "",

	@field:SerializedName("order_id")
	val orderId: Int = 0
)
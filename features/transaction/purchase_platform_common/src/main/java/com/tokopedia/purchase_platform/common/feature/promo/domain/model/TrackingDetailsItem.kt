package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName
import javax.annotation.Generated

@Generated("com.robohorse.robopojogenerator")
data class TrackingDetailsItem(

	@field:SerializedName("promo_details_tracking")
	val promoDetailsTracking: String? = null,

	@field:SerializedName("product_id")
	val productId: Long? = null,

	@field:SerializedName("promo_codes_tracking")
	val promoCodesTracking: String? = null
)
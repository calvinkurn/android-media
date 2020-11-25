package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class TrackingDetailsItem(

	@field:SerializedName("promo_details_tracking")
	val promoDetailsTracking: String? = null,

	@field:SerializedName("product_id")
	val productId: Int? = null,

	@field:SerializedName("promo_codes_tracking")
	val promoCodesTracking: String? = null
)
package com.tokopedia.purchase_platform.features.promo.data.response.validate_use

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class TrackingDetailsItem(

	@field:SerializedName("promo_details_tracking")
	val promoDetailsTracking: String = "",

	@field:SerializedName("product_id")
	val productId: Int = 0,

	@field:SerializedName("promo_codes_tracking")
	val promoCodesTracking: String = ""
)
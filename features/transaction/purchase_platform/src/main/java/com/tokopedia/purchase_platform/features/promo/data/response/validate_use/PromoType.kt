package com.tokopedia.purchase_platform.features.promo.data.response.validate_use

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class PromoType(

	@field:SerializedName("is_exclusive_shipping")
	val isExclusiveShipping: Boolean = false,

	@field:SerializedName("is_bebas_ongkir")
	val isBebasOngkir: Boolean = false
)
package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class PromoType(

	@field:SerializedName("is_exclusive_shipping")
	val isExclusiveShipping: Boolean? = null,

	@field:SerializedName("is_bebas_ongkir")
	val isBebasOngkir: Boolean? = null
)
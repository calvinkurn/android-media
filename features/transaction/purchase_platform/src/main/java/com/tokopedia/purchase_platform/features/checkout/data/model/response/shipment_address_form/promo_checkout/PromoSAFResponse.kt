package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.LastApply

@Generated("com.robohorse.robopojogenerator")
data class PromoSAFResponse(

	@field:SerializedName("last_apply")
	val lastApply: LastApply? = null,

	@field:SerializedName("error_default")
	val errorDefault: ErrorDefault? = null
)
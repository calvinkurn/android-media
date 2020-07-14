package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ConversionRate(

	@field:SerializedName("points_coefficient")
	val pointsCoefficient: Int? = null,

	@field:SerializedName("rate")
	val rate: Int? = null,

	@field:SerializedName("external_currency_coefficient")
	val externalCurrencyCoefficient: Int? = null
)
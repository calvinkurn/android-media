package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ConversionRate(

	@field:SerializedName("points_coefficient")
	val pointsCoefficient: Int = 0,

	@field:SerializedName("rate")
	val rate: Int = 0,

	@field:SerializedName("external_currency_coefficient")
	val externalCurrencyCoefficient: Int = 0
)
package com.tokopedia.purchase_platform.features.promo.data.response.validate_use

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class TokopointsDetail(

	@field:SerializedName("conversion_rate")
	val conversionRate: ConversionRate = ConversionRate()
)
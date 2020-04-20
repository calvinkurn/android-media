package com.tokopedia.purchase_platform.common.data.model.response.validateuse

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.data.model.response.validateuse.ConversionRate

@Generated("com.robohorse.robopojogenerator")
data class TokopointsDetail(

	@field:SerializedName("conversion_rate")
	val conversionRate: ConversionRate = ConversionRate()
)
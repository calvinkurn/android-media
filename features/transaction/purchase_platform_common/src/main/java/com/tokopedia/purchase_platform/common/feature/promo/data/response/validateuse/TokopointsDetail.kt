package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import com.google.gson.annotations.SerializedName

data class TokopointsDetail(

    @field:SerializedName("conversion_rate")
    val conversionRate: ConversionRate = ConversionRate()
)

package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 08/08/18.
 */
data class RatesGqlResponse(
    @SerializedName("ratesV3")
    @Expose
    val ratesData: RatesData = RatesData()
)

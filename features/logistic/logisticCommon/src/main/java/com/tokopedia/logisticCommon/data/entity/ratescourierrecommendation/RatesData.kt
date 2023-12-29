package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

data class RatesData(
    @SerializedName("ratesv3")
    val ratesDetailData: RatesDetailData = RatesDetailData()
)

package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 05/03/19.
 */
data class InfoRatesDetailData(
    @SerializedName("blackbox_info")
    val blackboxInfo: BlackboxInfoRatesDetailData = BlackboxInfoRatesDetailData()
)

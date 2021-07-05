package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-11-13.
 */

data class RatesApiGqlResponse(
        @SerializedName("ratesV3Api")
        val ratesData: RatesData
)
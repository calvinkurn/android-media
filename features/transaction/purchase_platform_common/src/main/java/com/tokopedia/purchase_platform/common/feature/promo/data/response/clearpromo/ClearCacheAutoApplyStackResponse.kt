package com.tokopedia.purchase_platform.common.feature.promo.data.response.clearpromo

import com.google.gson.annotations.SerializedName

data class ClearCacheAutoApplyStackResponse(
    @SerializedName("clearCacheAutoApplyStack")
    var successData: SuccessData = SuccessData()
)

data class SuccessData(
    @SerializedName("Success")
    var success: Boolean = false,

    @SerializedName("ticker_message")
    var tickerMessage: String = "",

    @SerializedName("default_empty_promo_message")
    var defaultEmptyPromoMessage: String = ""
)

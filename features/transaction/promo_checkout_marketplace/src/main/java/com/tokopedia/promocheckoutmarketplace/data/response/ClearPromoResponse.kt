package com.tokopedia.promocheckoutmarketplace.data.response

import com.google.gson.annotations.SerializedName

data class ClearPromoResponse(
    @SerializedName("clearCacheAutoApplyStack")
    val successData: SuccessData = SuccessData()
)

data class SuccessData(
    @SerializedName("Success")
    val success: Boolean = false,

    @SerializedName("ticker_message")
    val tickerMessage: String = "",

    @SerializedName("default_empty_promo_message")
    val defaultEmptyPromoMessage: String = ""
)

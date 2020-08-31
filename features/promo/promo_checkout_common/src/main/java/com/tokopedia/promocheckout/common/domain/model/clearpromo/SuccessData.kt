package com.tokopedia.promocheckout.common.domain.model.clearpromo

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 28/03/19.
 */

data class SuccessData(
        @SerializedName("Success")
        var success: Boolean = false,

        @SerializedName("ticker_message")
        var tickerMessage: String = "",

        @SerializedName("default_empty_promo_message")
        var defaultEmptyPromoMessage: String = ""
)
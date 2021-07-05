package com.tokopedia.digital_checkout.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 14/01/21
 */

data class CancelVoucherData(
        @SerializedName("Success")
        @Expose
        val success: Boolean = false,

        @SerializedName("ticker_message")
        @Expose
        val tickerMessage: String = "",

        @SerializedName("default_empty_promo_message")
        @Expose
        val defaultEmptyPromoMessage: String = "",

        @SerializedName("error")
        @Expose
        val error: String = ""
) {
    data class Response(
            @SerializedName("clearCacheAutoApplyStack")
            @Expose
            val response: CancelVoucherData
    )
}
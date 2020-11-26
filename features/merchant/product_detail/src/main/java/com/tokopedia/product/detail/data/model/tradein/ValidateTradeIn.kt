package com.tokopedia.product.detail.data.model.tradein

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 20/07/20
 */
data class ValidateTradeIn(
        @SerializedName("isEligible")
        @Expose
        var isEligible: Boolean = false,
        @SerializedName("isDiagnosed")
        @Expose
        var isDiagnosed: Boolean = false,
        @SerializedName("useKyc")
        @Expose
        var useKyc: Boolean = false,
        @SerializedName("usedPrice")
        @Expose
        var usedPrice: String = "",
        @SerializedName("remainingPrice")
        @Expose
        var remainingPrice: String = "",
        @SerializedName("message")
        @Expose
        var message: String = "",
        @SerializedName("widgetString")
        @Expose
        var widgetString: String = ""
)
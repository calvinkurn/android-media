package com.tokopedia.product.detail.data.model


import com.google.gson.annotations.SerializedName
import com.tokopedia.common_tradein.model.ValidateTradeInResponse

data class TradeinResponse(
        @SerializedName("validateTradeInPDP")
        val validateTradeInPDP: ValidateTradeInResponse = ValidateTradeInResponse()
)
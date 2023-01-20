package com.tokopedia.checkout.data.model.request.saveshipmentstate

import com.google.gson.annotations.SerializedName
import com.tokopedia.checkout.data.model.request.common.RatesFeature

data class ShipmentStateShippingInfoData(
        @SerializedName("shipping_id")
        var shippingId: Long = 0,
        @SerializedName("sp_id")
        var spId: Long = 0,
        @SerializedName("rates_feature")
        var ratesFeature: RatesFeature? = null
)

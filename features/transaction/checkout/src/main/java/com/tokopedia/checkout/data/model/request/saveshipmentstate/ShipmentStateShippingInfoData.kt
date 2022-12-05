package com.tokopedia.checkout.data.model.request.saveshipmentstate

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.checkout.data.model.request.common.RatesFeature

data class ShipmentStateShippingInfoData(
        @SuppressLint("Invalid Data Type")
        @SerializedName("shipping_id")
        var shippingId: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("sp_id")
        var spId: Int = 0,
        @SerializedName("rates_feature")
        var ratesFeature: RatesFeature? = null
)

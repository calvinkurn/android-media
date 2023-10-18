package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 25/10/21.
 */
data class CrossSellProductPriceTier(
    @SerializedName("min")
    val min: Int = 0,

    @SerializedName("max")
    val max: Int = 0
)

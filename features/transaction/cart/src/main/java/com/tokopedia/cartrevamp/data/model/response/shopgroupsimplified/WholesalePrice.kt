package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class WholesalePrice(
    @SerializedName("qty_min")
    val qtyMin: Int = 0,
    @SerializedName("qty_max")
    val qtyMax: Int = 0,
    @SerializedName("prd_prc")
    val prdPrc: Double = 0.0
)

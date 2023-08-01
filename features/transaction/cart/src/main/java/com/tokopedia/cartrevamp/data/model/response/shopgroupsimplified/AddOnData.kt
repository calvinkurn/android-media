package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class AddOnData(
    @SerializedName("addon_id")
    val addonId: String = "",
    @SerializedName("unique_id")
    val uniqueId: String = "",
    @SerializedName("status")
    val status: Int = -1,
    @SerializedName("type")
    val type: Int = -1,
    @SerializedName("price")
    val price: Double = 0.0
)

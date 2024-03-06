package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class LabelData(
    @SerializedName("timer")
    val timer: Timer = Timer(),
    @SerializedName("asset_label")
    val assetLabel: AssetLabel = AssetLabel()
)

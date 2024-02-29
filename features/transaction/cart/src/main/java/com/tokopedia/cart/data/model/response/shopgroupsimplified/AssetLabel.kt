package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class AssetLabel(
    @SerializedName("image_asset")
    val imageAsset: ImageAsset = ImageAsset(),
    @SerializedName("text_asset")
    val textAsset: TextAsset = TextAsset()
)

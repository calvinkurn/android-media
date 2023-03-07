package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LuckyEggFloatingEntity(
    @Expose @SerializedName("applink")
    var applink: String = "",
    @Expose
    @SerializedName("tokenClaimCustomText")
    var tokenClaimCustomText: String = "",
        @Expose
    @SerializedName("tokenAsset")
    var tokenAsset: LuckyEggTokenAssetEntity = LuckyEggTokenAssetEntity(),
)

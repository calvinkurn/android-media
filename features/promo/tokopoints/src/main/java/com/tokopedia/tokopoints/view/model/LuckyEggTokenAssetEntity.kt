package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LuckyEggTokenAssetEntity(
    @Expose
    @SerializedName("floatingImgUrl")
    var floatingImgUrl: String = ""
)

package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopInfoBadgeLevel {
    @SerializedName("level")
    @Expose
    var level: Long = 0

    @SerializedName("set")
    @Expose
    var set: Long = 0
}
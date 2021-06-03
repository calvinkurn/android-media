package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopInfoClosedInfo {
    @SerializedName("note")
    @Expose
    var note: String? = null

    @SerializedName("reason")
    @Expose
    var reason: String? = null

    @SerializedName("until")
    @Expose
    var until: String? = null
}
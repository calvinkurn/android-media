package com.tokopedia.cartrevamp.data.model.response.promo

import com.google.gson.annotations.SerializedName

data class CartPromoTicker(
    @SerializedName("enable")
    val enable: Boolean = false,

    @SerializedName("text")
    val text: String = "",

    @SerializedName("icon_url")
    val iconUrl: String = ""
)

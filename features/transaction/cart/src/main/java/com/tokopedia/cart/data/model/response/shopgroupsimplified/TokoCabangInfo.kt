package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

class TokoCabangInfo(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("badge_url")
        val badgeUrl: String = ""
)

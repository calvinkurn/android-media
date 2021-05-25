package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class FreeShipping(
        @SerializedName("badge_url")
        val badgeUrl: String = "",
        @SerializedName("eligible")
        val eligible: Boolean = false
)
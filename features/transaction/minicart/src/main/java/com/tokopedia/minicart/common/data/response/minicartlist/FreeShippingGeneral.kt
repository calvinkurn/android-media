package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class FreeShippingGeneral(
        @SerializedName("badge_url")
        val badgeUrl: String = "",
        @SerializedName("bo_type")
        val boType: Int = 0,
        @SerializedName("bo_name")
        val boName: String = "",
)
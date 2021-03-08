package com.tokopedia.purchase_platform.common.feature.fulfillment.response

import com.google.gson.annotations.SerializedName

class TokoCabangInfo(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("badge_url")
        val badgeUrl: String = ""
)

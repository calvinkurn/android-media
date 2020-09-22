package com.tokopedia.purchase_platform.common.feature.button

import com.google.gson.annotations.SerializedName

data class ABTestButtonResponse(
        @SerializedName("enable")
        val enable: Boolean = false
)
package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AutoExtend(
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("tkpd_product_id")
        @Expose
        val tkpdProductId: Int = 0
)
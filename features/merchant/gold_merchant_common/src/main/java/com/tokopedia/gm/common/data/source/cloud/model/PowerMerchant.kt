package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PowerMerchant(
        @SerializedName("auto_extend")
        @Expose
        val autoExtend: AutoExtend = AutoExtend(),
        @SerializedName("expired_time")
        @Expose
        val expiredTime: String = "",
        @SerializedName("shop_popup")
        @Expose
        val shopPopup: Boolean = false,
        @SerializedName("status")
        @Expose
        val status: String = ""
)
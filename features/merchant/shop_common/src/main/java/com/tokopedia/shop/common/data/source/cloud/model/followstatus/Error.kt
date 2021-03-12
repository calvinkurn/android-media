package com.tokopedia.shop.common.data.source.cloud.model.followstatus

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Error(
        @SerializedName("message")
        @Expose
        val message: String?
)
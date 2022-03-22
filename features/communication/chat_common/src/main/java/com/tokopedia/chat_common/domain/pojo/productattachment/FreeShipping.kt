package com.tokopedia.chat_common.domain.pojo.productattachment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FreeShipping (
        @SerializedName("is_active")
        @Expose
        val isActive: Boolean = false,
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = ""
)
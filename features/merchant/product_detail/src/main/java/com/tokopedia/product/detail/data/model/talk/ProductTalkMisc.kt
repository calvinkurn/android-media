package com.tokopedia.product.detail.data.model.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Reputation(
        @SerializedName("percentage")
        @Expose
        val percentage: String = ""
)

data class CreateTime(
        @SerializedName("format_app_3")
        @Expose
        val formatted: String = ""
)
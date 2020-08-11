package com.tokopedia.mediauploader.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SourcePolicy(
        @Expose @SerializedName("source_type") val sourceType: String = "",
        @Expose @SerializedName("host") val host: String = "",
        @Expose @SerializedName("timeout") val timeOut: Int = 60,
        @Expose @SerializedName("image_policy") val imagePolicy: Policy = Policy()
)
package com.tokopedia.mediauploader.common.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploaderPolicy(
    @Expose @SerializedName("source_policy") val sourcePolicy: SourcePolicy = SourcePolicy()
)
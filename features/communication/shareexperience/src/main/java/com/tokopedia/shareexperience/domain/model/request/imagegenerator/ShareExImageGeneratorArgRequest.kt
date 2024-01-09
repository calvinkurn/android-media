package com.tokopedia.shareexperience.domain.model.request.imagegenerator

import com.google.gson.annotations.SerializedName

data class ShareExImageGeneratorArgRequest(
    @SerializedName("key")
    val key: String,
    @SerializedName("value")
    val value: String
)

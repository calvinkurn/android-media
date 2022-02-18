package com.tokopedia.analytics.mapper.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EmbraceConfig {
    @SerializedName("allowed_moments")
    @Expose
    val allowedMoments: List<String> = arrayListOf()
}
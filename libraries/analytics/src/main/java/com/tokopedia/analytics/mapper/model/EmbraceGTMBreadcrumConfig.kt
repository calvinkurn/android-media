package com.tokopedia.analytics.mapper.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EmbraceGTMBreadcrumConfig {
    @SerializedName("allowed_category")
    @Expose
    val allowed_category: List<String> = arrayListOf()
}
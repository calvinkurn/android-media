package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GMFeaturedLabel (
    @SerializedName("title")
    @Expose
    var title: String? = null,
    @SerializedName("color")
    @Expose
    var color: String? = null
)
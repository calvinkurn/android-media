package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EmptyMessage(
    @Expose
    @SerializedName("title")
    var title: String = "",
    @Expose
    @SerializedName("subTitle")
    var subTitle: String = ""
)

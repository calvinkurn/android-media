package com.tokopedia.similarsearch.getsimilarproducts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class LabelGroups(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("position")
        @Expose
        val position: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",
)

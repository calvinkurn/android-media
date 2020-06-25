package com.tokopedia.discovery2.data.cpmtopads

import com.google.gson.annotations.SerializedName

data class Image(

        @SerializedName("full_url")
        val fullUrl: String? = "",

        @SerializedName("full_ecs")
        val fullEcs: String? = ""
)
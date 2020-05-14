package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class Properties(
        @SerializedName("columns")
        val columns: String?
)
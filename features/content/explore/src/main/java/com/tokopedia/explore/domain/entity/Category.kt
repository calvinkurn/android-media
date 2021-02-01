package com.tokopedia.explore.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Category(
        @SerializedName("id")
        @Expose
        var id: String = "",

        @SerializedName("name")
        @Expose
        var name: String = ""
)
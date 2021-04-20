package com.tokopedia.hotel.search.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Sort(
        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("displayName")
        @Expose
        var displayName: String = ""
)
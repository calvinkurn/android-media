package com.tokopedia.hotel.search.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DisplayInfo(
        @SerializedName("filter")
        @Expose
        val filter: Filter = Filter(),

        @SerializedName("sort")
        @Expose
        val sort: List<Sort> = listOf()
)
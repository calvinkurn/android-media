package com.tokopedia.hotel.search_map.data.model.params

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ParamGuest(
        @SerializedName("adult")
        @Expose
        var adult: Int = 0,

        @SerializedName("childAge")
        @Expose
        var childAge: List<Int> = listOf()
)
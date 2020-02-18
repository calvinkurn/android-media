package com.tokopedia.autocomplete.initialstate.popularsearch

import com.google.gson.annotations.SerializedName
import com.tokopedia.autocomplete.initialstate.InitialStateItem

data class PopularSearchResponse (
        @SerializedName("process_time")
        var processTime: Double = 0.0,

        @SerializedName("data")
        var data: List<InitialStateItem> = listOf()
)
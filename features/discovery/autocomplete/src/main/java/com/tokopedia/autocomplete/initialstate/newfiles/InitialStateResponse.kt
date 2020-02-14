package com.tokopedia.autocomplete.initialstate.newfiles

import com.google.gson.annotations.SerializedName

data class InitialStateResponse (
    @SerializedName("process_time")
    var processTime: Double = 0.0,

    @SerializedName("data")
    var data: List<InitialStateData> = listOf()
)
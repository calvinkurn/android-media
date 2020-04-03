package com.tokopedia.autocomplete.initialstate

import com.google.gson.annotations.SerializedName

data class InitialStateResponse (
    @SerializedName("process_time")
    val processTime: Double = 0.0,

    @SerializedName("data")
    val data: List<InitialStateData> = listOf()
)
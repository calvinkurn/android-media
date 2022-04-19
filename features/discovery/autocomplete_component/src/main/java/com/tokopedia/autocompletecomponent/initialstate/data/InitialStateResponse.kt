package com.tokopedia.autocompletecomponent.initialstate.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData

data class InitialStateResponse (
    @SerializedName("process_time")
    val processTime: Double = 0.0,

    @SerializedName("data")
    val data: List<InitialStateData> = listOf()
)
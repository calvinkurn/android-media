package com.tokopedia.autocomplete.initialstate.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.autocomplete.initialstate.InitialStateData

data class InitialStateUniverse (
    @SerializedName("data")
    val data: List<InitialStateData> = listOf()
)
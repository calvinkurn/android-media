package com.tokopedia.autocomplete.initialstate

import com.google.gson.annotations.SerializedName

data class InitialStateUniverse (
    @SerializedName("data")
    val data: List<InitialStateData> = listOf()
)
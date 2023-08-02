package com.tokopedia.autocompletecomponent.initialstate.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData

data class InitialStateUniverse (
    @SerializedName("data")
    val data: List<InitialStateData> = listOf(),

    @SerializedName("is_mps")
    val isMps: Boolean = false,
)

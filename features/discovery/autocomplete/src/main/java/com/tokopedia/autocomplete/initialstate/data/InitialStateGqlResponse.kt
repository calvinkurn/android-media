package com.tokopedia.autocomplete.initialstate.data

import com.google.gson.annotations.SerializedName

data class InitialStateGqlResponse (
    @SerializedName("universe_initial_state")
    val initialStateUniverse: InitialStateUniverse = InitialStateUniverse()
)
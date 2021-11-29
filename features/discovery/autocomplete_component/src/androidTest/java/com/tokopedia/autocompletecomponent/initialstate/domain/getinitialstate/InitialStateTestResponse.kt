package com.tokopedia.autocompletecomponent.initialstate.domain.getinitialstate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse

data class InitialStateTestResponse(
        @SerializedName("universe_initial_state_test")
        @Expose
        val initialStateUniverse: InitialStateUniverse,
)

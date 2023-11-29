package com.tokopedia.autocompletecomponent.unify.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuggestionUnifyFlag(
    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("value")
    @Expose
    val value: String = "",

    @SerializedName("enable")
    @Expose
    val enable: Boolean = false,
)

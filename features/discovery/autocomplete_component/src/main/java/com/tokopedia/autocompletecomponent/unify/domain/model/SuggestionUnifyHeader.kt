package com.tokopedia.autocompletecomponent.unify.domain.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SuggestionUnifyHeader(
    @SerializedName("message")
    @Expose
    val message: String = "",

    @SerializedName("status_code")
    @Expose
    val statusCode: Int = 0,

    @SerializedName("time_process")
    @Expose
    val timeProcess: Double = 0.0
)

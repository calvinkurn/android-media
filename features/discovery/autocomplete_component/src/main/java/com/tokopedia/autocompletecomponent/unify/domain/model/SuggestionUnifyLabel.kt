package com.tokopedia.autocompletecomponent.unify.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuggestionUnifyLabel(
    @SerializedName("bg_color")
    @Expose
    val bgColor: String = "",

    @SerializedName("text")
    @Expose
    val text: String = "",

    @SerializedName("text_color")
    @Expose
    val textColor: String = "",

    @SerializedName("action")
    @Expose
    val action: String = ""
)

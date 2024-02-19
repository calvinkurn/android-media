package com.tokopedia.autocompletecomponent.unify.domain.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class SuggestionUnifyCta(
    @SerializedName("action")
    @Expose
    val action: String = "",

    @SerializedName("image_url")
    @Expose
    val imageUrl: String = ""
)

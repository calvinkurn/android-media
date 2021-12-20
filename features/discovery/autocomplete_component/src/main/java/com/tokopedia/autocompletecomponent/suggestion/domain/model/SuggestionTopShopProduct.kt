package com.tokopedia.autocompletecomponent.suggestion.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SuggestionTopShopProduct(
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = ""
)
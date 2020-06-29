package com.tokopedia.autocomplete.suggestion

import com.google.gson.annotations.SerializedName

data class SuggestionTopShopProduct(
        @SerializedName("image_url")
        val imageUrl: String = ""
)
package com.tokopedia.tokopedianow.recipelist.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeFilterSearchResponse(
    @SerializedName("searchable")
    val searchable: Int,
    @SerializedName("placeholder")
    val placeholder: String
)
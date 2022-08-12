package com.tokopedia.tokopedianow.recipedetail.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeMediaResponse(
    @SerializedName("url")
    val url: String,
    @SerializedName("type")
    val type: String
)
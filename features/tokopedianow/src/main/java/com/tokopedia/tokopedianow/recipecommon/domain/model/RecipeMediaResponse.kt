package com.tokopedia.tokopedianow.recipecommon.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeMediaResponse(
    @SerializedName("url")
    val url: String,
    @SerializedName("type")
    val type: String
)
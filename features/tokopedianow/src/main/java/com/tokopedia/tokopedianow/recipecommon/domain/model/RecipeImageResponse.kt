package com.tokopedia.tokopedianow.recipecommon.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeImageResponse(
    @SerializedName("urlOriginal")
    val urlOriginal: String,
    @SerializedName("urlThumbnail")
    val urlThumbnail: String
)
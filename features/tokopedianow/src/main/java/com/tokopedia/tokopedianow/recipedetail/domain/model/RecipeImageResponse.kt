package com.tokopedia.tokopedianow.recipedetail.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeImageResponse(
    @SerializedName("urlOriginal")
    val urlOriginal: String,
    @SerializedName("urlThumbnail")
    val urlThumbnail: String,
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("filePath")
    val filePath: String
)
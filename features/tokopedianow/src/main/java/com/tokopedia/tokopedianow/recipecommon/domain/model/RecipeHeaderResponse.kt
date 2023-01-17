package com.tokopedia.tokopedianow.recipecommon.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeHeaderResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("processTime")
    val processTime: Float,
    @SerializedName("message")
    val message: String,
    @SerializedName("statusCode")
    val statusCode: String
)
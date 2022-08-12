package com.tokopedia.tokopedianow.recipedetail.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeIngredientResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("quantity")
    val quantity: Float,
    @SerializedName("unit")
    val unit: String,
    @SerializedName("image")
    val image: RecipeImageResponse
)
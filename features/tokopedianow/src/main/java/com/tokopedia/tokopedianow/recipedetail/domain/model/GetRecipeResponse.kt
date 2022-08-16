package com.tokopedia.tokopedianow.recipedetail.domain.model

import com.google.gson.annotations.SerializedName

data class GetRecipeResponse(
    @SerializedName("header")
    val header: RecipeHeaderResponse,
    @SerializedName("data")
    val data: RecipeResponse
)
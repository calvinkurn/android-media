package com.tokopedia.tokopedianow.recipedetail.domain.model

import com.google.gson.annotations.SerializedName

data class TokoNowGetRecipe(
    @SerializedName("TokonowGetRecipe")
    val response: GetRecipeResponse
)
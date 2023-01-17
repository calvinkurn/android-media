package com.tokopedia.tokopedianow.recipelist.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.tokopedianow.recipecommon.domain.model.RecipeResponse

data class RecipeListDataResponse(
    @SerializedName("recipes")
    val recipes: List<RecipeResponse>
)
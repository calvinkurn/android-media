package com.tokopedia.tokopedianow.recipedetail.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.tokopedianow.recipecommon.domain.model.RecipeHeaderResponse
import com.tokopedia.tokopedianow.recipecommon.domain.model.RecipeResponse

data class GetRecipeResponse(
    @SerializedName("header")
    val header: RecipeHeaderResponse,
    @SerializedName("data")
    val data: RecipeResponse
)
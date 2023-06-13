package com.tokopedia.tokopedianow.recipelist.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.tokopedianow.recipecommon.domain.model.RecipeHeaderResponse

data class GetRecipeListResponse(
    @SerializedName("header")
    val header: RecipeHeaderResponse,
    @SerializedName("metadata")
    val metadata: RecipeMetaDataResponse,
    @SerializedName("data")
    val data: RecipeListDataResponse
)
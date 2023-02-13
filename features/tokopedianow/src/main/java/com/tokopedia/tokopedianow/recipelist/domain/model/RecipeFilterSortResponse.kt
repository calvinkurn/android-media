package com.tokopedia.tokopedianow.recipelist.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.tokopedianow.recipecommon.domain.model.RecipeHeaderResponse

data class RecipeFilterSortResponse(
    @SerializedName("header")
    val header: RecipeHeaderResponse,
    @SerializedName("data")
    val data: RecipeFilterSortDataResponse
)
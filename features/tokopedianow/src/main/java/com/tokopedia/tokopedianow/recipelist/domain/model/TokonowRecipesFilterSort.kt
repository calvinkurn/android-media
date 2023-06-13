package com.tokopedia.tokopedianow.recipelist.domain.model

import com.google.gson.annotations.SerializedName

data class TokonowRecipesFilterSort(
    @SerializedName("TokonowRecipesFilterSort")
    val response: RecipeFilterSortResponse
)


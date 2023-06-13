package com.tokopedia.tokopedianow.recipelist.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeFilterSortDataResponse(
    @SerializedName("filter")
    val filter: List<RecipeFilterResponse>,
    @SerializedName("sort")
    val sort: List<RecipeSortResponse>
)
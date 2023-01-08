package com.tokopedia.tokopedianow.recipelist.domain.model

import com.google.gson.annotations.SerializedName

data class TokoNowGetRecipes(
    @SerializedName("TokonowGetRecipes")
    val response: GetRecipeListResponse
)

package com.tokopedia.tokopedianow.recipedetail.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header

data class GetRecipeResponse(
    @SerializedName("header")
    val header: Header,
    @SerializedName("data")
    val data: RecipeResponse
)
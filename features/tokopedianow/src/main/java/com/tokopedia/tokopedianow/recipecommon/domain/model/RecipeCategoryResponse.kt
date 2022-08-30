package com.tokopedia.tokopedianow.recipecommon.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeCategoryResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)
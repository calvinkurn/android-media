package com.tokopedia.tokopedianow.recipelist.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeMetaDataResponse(
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("total")
    val total: Int
)
package com.tokopedia.tokopedianow.recipelist.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeFilterResponse(
    @SerializedName("title")
    val title: String,
    @SerializedName("chipName")
    val chipName: String,
    @SerializedName("templateName")
    val templateName: String,
    @SerializedName("subTitle")
    val subTitle: String,
    @SerializedName("search")
    val search: RecipeFilterSearchResponse,
    @SerializedName("filterAttributeDetail")
    val filterAttributeDetail: String,
    @SerializedName("isNew")
    val isNew: Boolean,
    @SerializedName("options")
    val options: List<RecipeFilterOptionResponse>
)
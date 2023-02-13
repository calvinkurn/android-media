package com.tokopedia.tokopedianow.recipelist.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeFilterOptionChildResponse(
    @SerializedName("key")
    val key: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("inputType")
    val inputType: String,
    @SerializedName("totalData")
    val totalData: String,
    @SerializedName("isPopular")
    val isPopular: Boolean,
    @SerializedName("child")
    val child: RecipeFilterOptionChildResponse?
)
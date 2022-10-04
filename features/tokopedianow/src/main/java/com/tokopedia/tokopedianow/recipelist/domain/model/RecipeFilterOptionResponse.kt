package com.tokopedia.tokopedianow.recipelist.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeFilterOptionResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("key")
    val key: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("inputType")
    val inputType: String,
    @SerializedName("totalData")
    val totalData: String,
    @SerializedName("valMax")
    val valMax: String,
    @SerializedName("valMin")
    val valMin: String,
    @SerializedName("hexColor")
    val hexColor: String,
    @SerializedName("child")
    val child: RecipeFilterOptionChildResponse,
    @SerializedName("isPopular")
    val isPopular: Boolean,
    @SerializedName("isNew")
    val isNew: Boolean,
    @SerializedName("Description")
    val description: String
)
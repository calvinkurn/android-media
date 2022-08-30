package com.tokopedia.tokopedianow.recipecommon.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("instruction")
    val instruction: String,
    @SerializedName("portion")
    val portion: Int,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("createdTime")
    val createdTime: String,
    @SerializedName("updatedTime")
    val updatedTime: String,
    @SerializedName("publishedTime")
    val publishedTime: String,
    @SerializedName("category")
    val category: RecipeCategoryResponse,
    @SerializedName("tags")
    val tags: List<RecipeTagResponse>,
    @SerializedName("ingredients")
    val ingredients: List<RecipeIngredientResponse>,
    @SerializedName("images")
    val images: List<RecipeImageResponse>,
    @SerializedName("videos")
    val videos: List<RecipeVideoResponse>,
    @SerializedName("medias")
    val medias: List<RecipeMediaResponse>,
    @SerializedName("products")
    val products: List<RecipeProductResponse>,
    @SerializedName("isBookmarked")
    val isBookmarked: Boolean
)
package com.tokopedia.product.manage.item.main.base.data.source.cloud.model.categoryrecommendationdata

import com.google.gson.annotations.SerializedName

data class CategoryRecommendationData (
        @SerializedName("getJarvisRecommendation")
        val getJarvisRecommendation: GetJarvisRecommendation = GetJarvisRecommendation()
)

data class GetJarvisRecommendation(
        @SerializedName("categories")
        val categories: List<Category> = mutableListOf()
)

data class Category(
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("name")
        val name: String = ""
)
package com.tokopedia.product.addedit.detail.domain.model

import com.google.gson.annotations.SerializedName

data class GetCategoryRecommendationResponse(
    @SerializedName("getJarvisRecommendation")
    val categoryRecommendationDataModel: GetCategoryRecommendationDataModel?
)

data class GetCategoryRecommendationDataModel(
    @SerializedName("categories")
    val categories: List<CategoryItemModel>?
)

data class CategoryItemModel(
    @SerializedName("id")
    var id: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("confidence_score")
    var confidenceScore: String?,
    @SerializedName("precision")
    var precision: String?,
)

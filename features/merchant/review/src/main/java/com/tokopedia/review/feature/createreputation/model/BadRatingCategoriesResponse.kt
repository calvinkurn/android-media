package com.tokopedia.review.feature.createreputation.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BadRatingCategoriesResponse(
    @SerializedName("productrevGetBadRatingCategory")
    @Expose
    val productrevGetBadRatingCategory: ProductrevGetBadRatingCategory = ProductrevGetBadRatingCategory()
)

data class ProductrevGetBadRatingCategory(
    @SerializedName("list")
    @Expose
    val list: List<BadRatingCategory> = listOf()
)

data class BadRatingCategory(
    @SerializedName("badRatingCategoryID")
    @Expose
    val id: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("isTextFocused")
    @Expose
    val shouldRequestFocus: Boolean = false
)
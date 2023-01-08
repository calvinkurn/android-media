package com.tokopedia.tokopedianow.recipecommon.domain.model

import com.google.gson.annotations.SerializedName

data class RecipeProductResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("detail")
    val detail: ProductDetailResponse,
    @SerializedName("similarProducts")
    val similarProducts: List<RecipeProductResponse>?
) {

    data class ProductDetailResponse(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("imageUrl")
        val imageUrl: String,
        @SerializedName("fmtPrice")
        val fmtPrice: String,
        @SerializedName("appUrl")
        val appUrl: String,
        @SerializedName("slashedPrice")
        val slashedPrice: String,
        @SerializedName("discountPercentage")
        val discountPercentage: Float,
        @SerializedName("parentProductID")
        val parentProductID: String,
        @SerializedName("minOrder")
        val minOrder: Int,
        @SerializedName("stock")
        val stock: Int,
        @SerializedName("categoryName")
        val categoryName: String,
        @SerializedName("countReview")
        val countReview: String,
        @SerializedName("rating")
        val rating: Int,
        @SerializedName("ratingAverage")
        val ratingAverage: String,
        @SerializedName("shopID")
        val shopID: String,
        @SerializedName("maxOrder")
        val maxOrder: Int,
        @SerializedName("categoryID")
        val categoryID: String,
        @SerializedName("redirectLink")
        val redirectLink: String,
        @SerializedName("labelGroups")
        val labelGroups: List<LabelGroupResponse>,
        @SerializedName("labelGroupVariants")
        val labelGroupVariants: List<LabelGroupVariantResponse>
    )

    data class LabelGroupResponse(
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("position")
        val position: String,
        @SerializedName("url")
        val url: String
    )

    data class LabelGroupVariantResponse(
        @SerializedName("title")
        val title: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("typeVariant")
        val typeVariant: String,
        @SerializedName("hexColor")
        val hexColor: String
    )
}
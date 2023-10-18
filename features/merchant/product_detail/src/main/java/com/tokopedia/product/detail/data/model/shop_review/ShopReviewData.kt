package com.tokopedia.product.detail.data.model.shop_review

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.data.model.datamodel.review_list.ProductShopReviewUiModel

/**
 * Created by yovi.putra on 16/02/23"
 * Project name: android-tokopedia-core
 **/

data class ShopReviewData(
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("applink")
    @Expose
    val appLink: String = "",
    @SerializedName("applinkTitle")
    @Expose
    val appLinkTitle: String = "",
    @SerializedName("data")
    @Expose
    val data: List<Review> = emptyList()
) {

    data class Review(
        @SerializedName("userImage")
        @Expose
        val userImage: String = "",
        @SerializedName("userName")
        @Expose
        val userName: String = "",
        @SerializedName("userTitle")
        @Expose
        val userTitle: String = "",
        @SerializedName("userSubtitle")
        @Expose
        val userSubtitle: String = "",
        @SerializedName("reviewText")
        @Expose
        val reviewText: String = "",
        @SerializedName("applink")
        @Expose
        val appLink: String = "",
        @SerializedName("reviewID")
        @Expose
        val reviewID: String = ""
    )
}

internal fun ShopReviewData.Review.asUiModel() = ProductShopReviewUiModel.Review(
    userImage = userImage,
    userName = userName,
    userTitle = userTitle,
    userSubtitle = userSubtitle,
    reviewText = reviewText,
    appLink = appLink,
    id = reviewID
)

internal fun List<ShopReviewData.Review>.asUiModel() = map {
    it.asUiModel()
}
internal fun ShopReviewData.asUiModel() = ProductShopReviewUiModel(
    title = title,
    appLink = appLink,
    appLinkTitle = appLinkTitle,
    reviews = data.asUiModel()
)

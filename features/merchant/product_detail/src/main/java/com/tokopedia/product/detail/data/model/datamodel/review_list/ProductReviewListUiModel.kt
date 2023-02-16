package com.tokopedia.product.detail.data.model.datamodel.review_list

/**
 * Created by yovi.putra on 16/02/23"
 * Project name: android-tokopedia-core
 **/

data class ProductReviewListUiModel(
    val title: String = "",
    val appLink: String = "",
    val appLinkTitle: String = "",
    val reviews: List<Review> = emptyList()
) {
    data class Review(
        val userImage: String = "",
        val userName: String = "",
        val userTitle: String = "",
        val userSubtitle: String = "",
        val reviewText: String = "",
        val appLink: String = ""
    )
}

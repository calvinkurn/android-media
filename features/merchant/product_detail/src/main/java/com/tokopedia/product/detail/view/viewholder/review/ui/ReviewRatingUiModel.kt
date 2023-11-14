package com.tokopedia.product.detail.view.viewholder.review.ui

import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by yovi.putra on 09/11/23"
 * Project name: android-tokopedia-core
 **/

data class ReviewRatingUiModel(
    val ratingScore: String = String.EMPTY,
    val totalRating: String = String.EMPTY,
    val totalReviewAndImage: String = String.EMPTY,
    val show: Boolean = false,
    val keywords: List<Keyword> = listOf()
) {

    data class Keyword(
        val text: String,
        val filter: String
    )
}

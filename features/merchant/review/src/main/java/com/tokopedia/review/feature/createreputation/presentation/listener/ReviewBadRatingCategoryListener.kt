package com.tokopedia.review.feature.createreputation.presentation.listener

interface ReviewBadRatingCategoryListener {
    fun onBadRatingCategoryClicked(title: String, isSelected: Boolean, badRatingCategoryId: Int)
    fun onImpressBadRatingCategory(title: String)
}
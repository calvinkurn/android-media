package com.tokopedia.review.feature.createreputation.presentation.listener

interface ReviewBadRatingCategoryListener {
    fun onBadRatingCategoryClicked(title: String, isSelected: Boolean, badRatingCategoryId: Int, shouldRequestFocus: Boolean)
    fun onImpressBadRatingCategory(title: String)
}
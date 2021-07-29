package com.tokopedia.review.feature.reading.presentation.listener

import com.tokopedia.review.feature.reading.data.ProductReview

interface ReadReviewAttachedImagesListener {
    fun onAttachedImagesClicked(productReview: ProductReview, positionClicked: Int, shopId: String)
}
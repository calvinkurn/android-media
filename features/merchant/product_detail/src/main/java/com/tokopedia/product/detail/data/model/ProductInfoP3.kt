package com.tokopedia.product.detail.data.model

import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.product.detail.data.model.estimasiongkir.RatesEstimationModel
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.talk.ProductTalkQuery

data class ProductInfoP3(
        var rateEstimation: RatesEstimationModel? = null,
        var isWishlisted: Boolean = false,
        var imageReviews: List<ImageReviewItem> = listOf(),
        var helpfulReviews: List<Review> = listOf(),
        var latestTalk: ProductTalkQuery = ProductTalkQuery()
)
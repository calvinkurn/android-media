package com.tokopedia.product.detail.data.model

import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpful

/**
 * Created by Yehezkiel on 27/07/20
 */
data class ProductInfoP2Other(
        var imageReviews: List<ImageReviewItem>? = null,
        var helpfulReviews: List<Review>? = null,
        var discussionMostHelpful: DiscussionMostHelpful? = null
)
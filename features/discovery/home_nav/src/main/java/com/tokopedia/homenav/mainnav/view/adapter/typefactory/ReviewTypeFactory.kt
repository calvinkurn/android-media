package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import com.tokopedia.homenav.mainnav.view.datamodel.review.EmptyStateReviewDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ReviewModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.OtherReviewModel

interface ReviewTypeFactory {
    fun type(reviewModel: ReviewModel): Int
    fun type(otherReviewModel: OtherReviewModel): Int
    fun type(emptyStateReviewDataModel: EmptyStateReviewDataModel): Int
}

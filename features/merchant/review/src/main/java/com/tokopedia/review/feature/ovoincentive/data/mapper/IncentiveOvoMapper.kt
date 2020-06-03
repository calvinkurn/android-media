package com.tokopedia.review.feature.ovoincentive.data.mapper

import com.tokopedia.review.feature.createreputation.model.ProductRevIncentiveOvo
import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain


object IncentiveOvoMapper {

    fun mapIncentiveOvoReviewtoIncentiveOvoInbox(incentiveOvoReview: ProductRevIncentiveOvo): ProductRevIncentiveOvoDomain {
        val productRevIncentiveOvoDomain = ProductRevIncentiveOvoDomain()
        productRevIncentiveOvoDomain.productrevIncentiveOvo.apply {
            title = incentiveOvoReview.productrevIncentiveOvo.title
            subtitle = incentiveOvoReview.productrevIncentiveOvo.subtitle
            numberedList = incentiveOvoReview.productrevIncentiveOvo.numberedList
            description = incentiveOvoReview.productrevIncentiveOvo.description
            ctaText = incentiveOvoReview.productrevIncentiveOvo.ctaText
            ticker.title = incentiveOvoReview.productrevIncentiveOvo.ticker.title
            ticker.subtitle = incentiveOvoReview.productrevIncentiveOvo.ticker.subtitle
        }
        return productRevIncentiveOvoDomain
    }
}
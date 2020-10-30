package com.tokopedia.review.feature.inbox.buyerreview.data.mapper

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ProductRevIncentiveOvoResponse

object IncentiveOvoMapper {

    fun mapIncentiveOvoReviewtoIncentiveOvoInbox(productrevIncentiveOvo: ProductRevIncentiveOvoResponse): com.tokopedia.review.feature.inbox.buyerreview.domain.model.ProductRevIncentiveOvoDomain {
        val productRevIncentiveOvoDomain = com.tokopedia.review.feature.inbox.buyerreview.domain.model.ProductRevIncentiveOvoDomain()
        productRevIncentiveOvoDomain.productrevIncentiveOvo?.apply {
            title = productrevIncentiveOvo.title
            subtitle = productrevIncentiveOvo.subtitle
            numberedList = productrevIncentiveOvo.numberedList
            description = productrevIncentiveOvo.description
            ctaText = productrevIncentiveOvo.ctaText
            ticker.title = productrevIncentiveOvo.ticker.title
            ticker.subtitle = productrevIncentiveOvo.ticker.subtitle
        }
        return productRevIncentiveOvoDomain
    }
}
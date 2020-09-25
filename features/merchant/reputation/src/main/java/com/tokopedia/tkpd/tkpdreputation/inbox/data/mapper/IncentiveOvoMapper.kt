package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper

import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ProductRevIncentiveOvoResponse
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ProductRevIncentiveOvoDomain

object IncentiveOvoMapper {

    fun mapIncentiveOvoReviewtoIncentiveOvoInbox(productrevIncentiveOvo: ProductRevIncentiveOvoResponse): ProductRevIncentiveOvoDomain {
        val productRevIncentiveOvoDomain = ProductRevIncentiveOvoDomain()
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
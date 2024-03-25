package com.tokopedia.homenav.mainnav.view.datamodel.review

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.ReviewTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

class EmptyStateReviewDataModel: ReviewNavVisitable, ImpressHolder() {
    override fun type(factory: ReviewTypeFactory): Int {
        return factory.type(this)
    }
}

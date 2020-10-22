package com.tokopedia.review.feature.inbox.buyerreview.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox.InboxReputationTypeFactory

class InboxReputationOvoIncentiveViewModel(
        val productRevIncentiveOvoDomain: com.tokopedia.review.feature.inbox.buyerreview.domain.model.ProductRevIncentiveOvoDomain
) : Visitable<InboxReputationTypeFactory> {

    override fun type(typeFactory: InboxReputationTypeFactory): Int {
        return typeFactory.type(this)
    }
}
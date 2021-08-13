package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox.InboxReputationTypeFactory

class SellerMigrationReviewModel: Visitable<InboxReputationTypeFactory> {
    override fun type(typeFactory: InboxReputationTypeFactory): Int {
        return typeFactory.type(this)
    }
}
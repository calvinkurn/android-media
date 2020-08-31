package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox.InboxReputationTypeFactory

class SellerMigrationReviewModel: Visitable<InboxReputationTypeFactory> {
    override fun type(typeFactory: InboxReputationTypeFactory): Int {
        return typeFactory.type(this)
    }
}
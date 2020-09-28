package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ProductRevIncentiveOvoDomain
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox.InboxReputationTypeFactory

class InboxReputationOvoIncentiveViewModel(
        val productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain
) : Visitable<InboxReputationTypeFactory> {

    override fun type(typeFactory: InboxReputationTypeFactory): Int {
        return typeFactory.type(this)
    }
}
package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox.InboxReputationTypeFactory

/**
 * @author by nisie on 9/13/17.
 */
class EmptySearchModel constructor() : Visitable<InboxReputationTypeFactory?> {
    var title: String? = null
    var buttonText: String? = null
    var buttonListener: View.OnClickListener? = null
    public override fun type(inboxReputationTypeFactory: InboxReputationTypeFactory): Int {
        return inboxReputationTypeFactory.type(this)
    }
}
package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailHeaderUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailItemUiModel

/**
 * @author by nisie on 8/19/17.
 */
interface InboxReputationDetailTypeFactory {
    fun type(model: InboxReputationDetailHeaderUiModel): Int
    fun type(model: InboxReputationDetailItemUiModel): Int
    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}
package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.ReputationAdapter.ReputationListener
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.LoadingInboxReputationDetailViewholder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.inboxdetail.InboxReputationDetailHeaderViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.inboxdetail.InboxReputationDetailItemViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationDetailFragment
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailHeaderUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailItemUiModel

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailTypeFactoryImpl constructor(inboxReputationDetail: InboxReputationDetailFragment) :
    BaseAdapterTypeFactory(), InboxReputationDetailTypeFactory {
    private val viewListener: InboxReputationDetail.View
    var reputationListener: ReputationListener
    public override fun type(model: InboxReputationDetailHeaderUiModel?): Int {
        return InboxReputationDetailHeaderViewHolder.Companion.LAYOUT
    }

    public override fun type(model: InboxReputationDetailItemUiModel?): Int {
        return InboxReputationDetailItemViewHolder.Companion.LAYOUT
    }

    public override fun type(viewModel: LoadingModel): Int {
        return LoadingInboxReputationDetailViewholder.Companion.LAYOUT
    }

    public override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        if (type == InboxReputationDetailHeaderViewHolder.Companion.LAYOUT) viewHolder =
            InboxReputationDetailHeaderViewHolder(
                view,
                reputationListener
            ) else if (type == InboxReputationDetailItemViewHolder.Companion.LAYOUT) viewHolder =
            InboxReputationDetailItemViewHolder(
                view,
                viewListener
            ) else if (type == LoadingInboxReputationDetailViewholder.Companion.LAYOUT) viewHolder =
            LoadingInboxReputationDetailViewholder(view) else viewHolder =
            super.createViewHolder(view, type)
        return viewHolder
    }

    init {
        viewListener = inboxReputationDetail
        reputationListener = inboxReputationDetail
    }
}
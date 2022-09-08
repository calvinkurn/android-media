package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.ReputationAdapter.ReputationListener
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.LoadingInboxReputationDetailViewholder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.inboxdetail.InboxReputationDetailHeaderViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.inboxdetail.InboxReputationDetailItemViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailHeaderUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailItemUiModel

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailTypeFactoryImpl constructor(
    private val viewListener: InboxReputationDetail.View,
    private val reputationListener: ReputationListener,
    private val reviewMediaThumbnailRecycledViewPool: RecyclerView.RecycledViewPool
) : BaseAdapterTypeFactory(), InboxReputationDetailTypeFactory {

    override fun type(model: InboxReputationDetailHeaderUiModel): Int {
        return InboxReputationDetailHeaderViewHolder.LAYOUT
    }

    override fun type(model: InboxReputationDetailItemUiModel): Int {
        return InboxReputationDetailItemViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return LoadingInboxReputationDetailViewholder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            InboxReputationDetailHeaderViewHolder.LAYOUT -> InboxReputationDetailHeaderViewHolder(
                view,
                reputationListener
            )
            InboxReputationDetailItemViewHolder.LAYOUT -> InboxReputationDetailItemViewHolder(
                view,
                reviewMediaThumbnailRecycledViewPool,
                viewListener
            )
            LoadingInboxReputationDetailViewholder.LAYOUT -> LoadingInboxReputationDetailViewholder(
                view
            )
            else -> super.createViewHolder(view, viewType)
        }
    }
}
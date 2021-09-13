package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.EmptyReputationSearchViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.InboxReputationViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.LoadingInboxReputationViewholder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.SellerMigrationReviewViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.SellerMigrationReviewViewHolder.Companion.LAYOUT
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.SellerMigrationReviewViewHolder.SellerMigrationReviewClickListener
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.EmptySearchModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.SellerMigrationReviewModel

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationTypeFactoryImpl constructor(
    private val context: Context?,
    private val viewListener: InboxReputation.View,
    private val sellerMigrationReviewClickListener: SellerMigrationReviewClickListener
) : BaseAdapterTypeFactory(), InboxReputationTypeFactory {
    public override fun type(viewModel: InboxReputationItemUiModel?): Int {
        return InboxReputationViewHolder.Companion.LAYOUT
    }

    public override fun type(viewModel: EmptySearchModel?): Int {
        return EmptyReputationSearchViewHolder.Companion.LAYOUT
    }

    public override fun type(model: SellerMigrationReviewModel?): Int {
        return LAYOUT
    }

    public override fun type(viewModel: LoadingModel): Int {
        return LoadingInboxReputationViewholder.Companion.LAYOUT
    }

    public override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        if (type == InboxReputationViewHolder.Companion.LAYOUT) viewHolder =
            InboxReputationViewHolder(
                context, view, viewListener
            ) else if (type == EmptyReputationSearchViewHolder.Companion.LAYOUT) {
            viewHolder = EmptyReputationSearchViewHolder(view)
        } else if (type == LoadingInboxReputationViewholder.Companion.LAYOUT) {
            viewHolder = LoadingInboxReputationViewholder(view)
        } else if (type == LAYOUT) {
            viewHolder = SellerMigrationReviewViewHolder(view, sellerMigrationReviewClickListener)
        } else viewHolder = super.createViewHolder(view, type)
        return viewHolder
    }
}
package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.EmptyReputationSearchViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.InboxReputationViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.LoadingInboxReputationViewholder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.SellerMigrationReviewViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.SellerMigrationReviewViewHolder.SellerMigrationReviewClickListener
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.EmptySearchModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.SellerMigrationReviewModel

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationTypeFactoryImpl constructor(
    private val viewListener: InboxReputation.View,
    private val sellerMigrationReviewClickListener: SellerMigrationReviewClickListener
) : BaseAdapterTypeFactory(), InboxReputationTypeFactory {

    override fun type(viewModel: InboxReputationItemUiModel): Int {
        return InboxReputationViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptySearchModel): Int {
        return EmptyReputationSearchViewHolder.LAYOUT
    }

    override fun type(model: SellerMigrationReviewModel): Int {
        return SellerMigrationReviewViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return LoadingInboxReputationViewholder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            InboxReputationViewHolder.LAYOUT -> InboxReputationViewHolder(
                view, viewListener
            )
            EmptyReputationSearchViewHolder.LAYOUT -> {
                EmptyReputationSearchViewHolder(view)
            }
            LoadingInboxReputationViewholder.LAYOUT -> {
                LoadingInboxReputationViewholder(view)
            }
            SellerMigrationReviewViewHolder.LAYOUT -> {
                SellerMigrationReviewViewHolder(view, sellerMigrationReviewClickListener)
            }
            else -> super.createViewHolder(view, viewType)
        }
    }
}
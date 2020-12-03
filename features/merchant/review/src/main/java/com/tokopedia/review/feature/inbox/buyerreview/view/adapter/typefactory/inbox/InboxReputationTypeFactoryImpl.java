package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox;

import android.content.Context;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.EmptyReputationSearchViewHolder;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.InboxReputationViewHolder;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.LoadingInboxReputationViewholder;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.SellerMigrationReviewViewHolder;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.EmptySearchModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.SellerMigrationReviewModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationTypeFactoryImpl extends BaseAdapterTypeFactory
        implements InboxReputationTypeFactory {

    private final InboxReputation.View viewListener;
    private final SellerMigrationReviewViewHolder.SellerMigrationReviewClickListener sellerMigrationReviewClickListener;
    private Context context;

    public InboxReputationTypeFactoryImpl(
            Context context,
            InboxReputation.View viewListener,
            SellerMigrationReviewViewHolder.SellerMigrationReviewClickListener sellerMigrationReviewClickListener
    ) {
        this.viewListener = viewListener;
        this.sellerMigrationReviewClickListener = sellerMigrationReviewClickListener;
        this.context = context;
    }

    @Override
    public int type(InboxReputationItemUiModel viewModel) {
        return InboxReputationViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptySearchModel viewModel) {
        return EmptyReputationSearchViewHolder.LAYOUT;
    }

    @Override
    public int type(SellerMigrationReviewModel model) {
        return SellerMigrationReviewViewHolder.Companion.getLAYOUT();
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingInboxReputationViewholder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;

        if (type == InboxReputationViewHolder.LAYOUT)
            viewHolder = new InboxReputationViewHolder(context, view, viewListener);
        else if (type == EmptyReputationSearchViewHolder.LAYOUT) {
            viewHolder = new EmptyReputationSearchViewHolder(view);
        } else if (type == LoadingInboxReputationViewholder.LAYOUT) {
            viewHolder = new LoadingInboxReputationViewholder(view);
        } else if (type == SellerMigrationReviewViewHolder.Companion.getLAYOUT()) {
            viewHolder = new SellerMigrationReviewViewHolder(view, sellerMigrationReviewClickListener);
        } else
            viewHolder = super.createViewHolder(view, type);
        return viewHolder;
    }
}

package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox;

import android.content.Context;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.EmptyReputationSearchViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.InboxReputationOvoIncentiveViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.InboxReputationViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.LoadingInboxReputationViewholder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.SellerMigrationReviewViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.EmptySearchModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationItemViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationOvoIncentiveViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.SellerMigrationReviewModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationTypeFactoryImpl extends BaseAdapterTypeFactory
        implements InboxReputationTypeFactory {

    private final com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation.View viewListener;
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
    public int type(InboxReputationItemViewModel viewModel) {
        return InboxReputationViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptySearchModel viewModel) {
        return EmptyReputationSearchViewHolder.LAYOUT;
    }

    @Override
    public int type(InboxReputationOvoIncentiveViewModel viewModel) {
        return InboxReputationOvoIncentiveViewHolder.Companion.getLAYOUT();
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
        } else if (type == InboxReputationOvoIncentiveViewHolder.Companion.getLAYOUT()) {
            viewHolder = new InboxReputationOvoIncentiveViewHolder(view, viewListener);
        } else if (type == SellerMigrationReviewViewHolder.Companion.getLAYOUT()) {
            viewHolder = new SellerMigrationReviewViewHolder(view, sellerMigrationReviewClickListener);
        } else
            viewHolder = super.createViewHolder(view, type);
        return viewHolder;
    }
}

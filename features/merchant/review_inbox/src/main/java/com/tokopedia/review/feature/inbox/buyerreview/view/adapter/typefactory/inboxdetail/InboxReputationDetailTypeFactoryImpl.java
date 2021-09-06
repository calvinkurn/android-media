package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.ReputationAdapter;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.LoadingInboxReputationDetailViewholder;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.inboxdetail.InboxReputationDetailHeaderViewHolder;
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder.inboxdetail.InboxReputationDetailItemViewHolder;
import com.tokopedia.review.feature.inbox.buyerreview.view.fragment.InboxReputationDetailFragment;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailHeaderUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailItemUiModel;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailTypeFactoryImpl extends BaseAdapterTypeFactory
        implements InboxReputationDetailTypeFactory {

    private final InboxReputationDetail.View viewListener;
    ReputationAdapter.ReputationListener reputationListener;

    public InboxReputationDetailTypeFactoryImpl(InboxReputationDetailFragment inboxReputationDetail) {
        this.viewListener = inboxReputationDetail;
        this.reputationListener = (ReputationAdapter.ReputationListener) inboxReputationDetail;
    }

    @Override
    public int type(InboxReputationDetailHeaderUiModel model) {
        return InboxReputationDetailHeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(InboxReputationDetailItemUiModel model) {
        return InboxReputationDetailItemViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingInboxReputationDetailViewholder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;

        if (type == InboxReputationDetailHeaderViewHolder.LAYOUT)
            viewHolder = new InboxReputationDetailHeaderViewHolder(view, reputationListener);
        else if (type == InboxReputationDetailItemViewHolder.LAYOUT)
            viewHolder = new InboxReputationDetailItemViewHolder(view, viewListener);
        else if (type == LoadingInboxReputationDetailViewholder.LAYOUT)
            viewHolder = new LoadingInboxReputationDetailViewholder(view);
        else
            viewHolder = super.createViewHolder(view, type);
        return viewHolder;
    }
}

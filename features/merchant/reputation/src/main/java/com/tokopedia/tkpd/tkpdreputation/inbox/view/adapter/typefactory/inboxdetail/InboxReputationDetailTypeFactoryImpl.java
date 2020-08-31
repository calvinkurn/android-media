package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inboxdetail;

import android.view.View;

import androidx.fragment.app.FragmentManager;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ProductRevIncentiveOvoDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.ReputationAdapter;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.LoadingInboxReputationDetailViewholder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.inboxdetail.InboxReputationDetailHeaderViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder.inboxdetail.InboxReputationDetailItemViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.fragment.InboxReputationDetailFragment;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailItemViewModel;

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
    public int type(InboxReputationDetailHeaderViewModel model) {
        return InboxReputationDetailHeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(InboxReputationDetailItemViewModel model) {
        return InboxReputationDetailItemViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingInboxReputationDetailViewholder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type, ProductRevIncentiveOvoDomain productRevIncentiveOvoDomain, FragmentManager fragmentManager) {
        AbstractViewHolder viewHolder;

        if (type == InboxReputationDetailHeaderViewHolder.LAYOUT)
            viewHolder = new InboxReputationDetailHeaderViewHolder(view, reputationListener, productRevIncentiveOvoDomain, fragmentManager);
        else if (type == InboxReputationDetailItemViewHolder.LAYOUT)
            viewHolder = new InboxReputationDetailItemViewHolder(view, viewListener);
        else if (type == LoadingInboxReputationDetailViewholder.LAYOUT)
            viewHolder = new LoadingInboxReputationDetailViewholder(view);
        else
            viewHolder = super.createViewHolder(view, type);
        return viewHolder;
    }
}

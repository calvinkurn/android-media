package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inboxdetail;

import android.view.View;

import androidx.fragment.app.FragmentManager;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ProductRevIncentiveOvoDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailHeaderViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.InboxReputationDetailItemViewModel;

/**
 * @author by nisie on 8/19/17.
 */

public interface InboxReputationDetailTypeFactory {

    int type(InboxReputationDetailHeaderViewModel model);

    int type(InboxReputationDetailItemViewModel model);

    AbstractViewHolder createViewHolder(View view, int viewType, ProductRevIncentiveOvoDomain productRevIncentiveOvoDomain, FragmentManager fragmentManager);
}

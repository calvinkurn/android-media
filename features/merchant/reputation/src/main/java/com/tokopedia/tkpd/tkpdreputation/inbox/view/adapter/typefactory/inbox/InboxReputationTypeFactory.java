package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inbox;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.EmptySearchModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationItemViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationOvoIncentiveViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.SellerMigrationReviewModel;

/**
 * @author by nisie on 8/19/17.
 */

public interface InboxReputationTypeFactory {

    int type(InboxReputationItemViewModel viewModel);

    int type(EmptySearchModel viewModel);

    int type(InboxReputationOvoIncentiveViewModel viewModel);

    int type(SellerMigrationReviewModel model);

    AbstractViewHolder createViewHolder(View view, int viewType);

}

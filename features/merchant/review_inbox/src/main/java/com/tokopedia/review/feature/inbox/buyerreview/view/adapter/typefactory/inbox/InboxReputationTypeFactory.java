package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inbox;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.EmptySearchModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel;
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.SellerMigrationReviewModel;

/**
 * @author by nisie on 8/19/17.
 */

public interface InboxReputationTypeFactory {

    int type(InboxReputationItemUiModel viewModel);

    int type(EmptySearchModel viewModel);

    int type(SellerMigrationReviewModel model);

    AbstractViewHolder createViewHolder(View view, int viewType);

}

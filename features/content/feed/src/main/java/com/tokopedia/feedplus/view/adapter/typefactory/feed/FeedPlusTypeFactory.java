package com.tokopedia.feedplus.view.adapter.typefactory.feed;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedplus.view.viewmodel.EmptyFeedBeforeLoginModel;
import com.tokopedia.feedplus.view.viewmodel.RetryModel;
import com.tokopedia.feedplus.view.viewmodel.kol.ContentProductViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.KolRecommendationViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.PollViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.ProductCommunicationViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.WhitelistViewModel;
import com.tokopedia.feedplus.view.viewmodel.product.ActivityCardViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlusTypeFactory {

    int type(ActivityCardViewModel viewModel);

    int type(KolPostViewModel kolPostViewModel);

    int type(KolRecommendationViewModel kolRecommendationViewModel);

    int type(ContentProductViewModel contentProductViewModel);

    int type(ProductCommunicationViewModel productCommunicationViewModel);

    int type(PollViewModel pollViewModel);

    int type(EmptyFeedBeforeLoginModel emptyFeedBeforeLoginModel);

    int type(RetryModel retryModel);

    int type(WhitelistViewModel whitelistViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}

package com.tokopedia.feedplus.view.adapter.typefactory.feed;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.feedplus.view.viewmodel.EmptyFeedBeforeLoginModel;
import com.tokopedia.feedplus.view.viewmodel.FavoriteCtaViewModel;
import com.tokopedia.feedplus.view.viewmodel.RetryModel;
import com.tokopedia.feedplus.view.viewmodel.blog.BlogViewModel;
import com.tokopedia.feedplus.view.viewmodel.inspiration.InspirationViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.ContentProductViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.KolRecommendationViewModel;
import com.tokopedia.feedplus.view.viewmodel.officialstore.OfficialStoreBrandsViewModel;
import com.tokopedia.feedplus.view.viewmodel.officialstore.OfficialStoreCampaignViewModel;
import com.tokopedia.feedplus.view.viewmodel.product.ActivityCardViewModel;
import com.tokopedia.feedplus.view.viewmodel.product.AddFeedModel;
import com.tokopedia.feedplus.view.viewmodel.promo.PromoCardViewModel;
import com.tokopedia.feedplus.view.viewmodel.promo.PromotedProductViewModel;
import com.tokopedia.feedplus.view.viewmodel.promo.PromotedShopViewModel;
import com.tokopedia.feedplus.view.viewmodel.recentview.RecentViewViewModel;
import com.tokopedia.feedplus.view.viewmodel.topads.FeedTopAdsViewModel;
import com.tokopedia.feedplus.view.viewmodel.toppicks.ToppicksViewModel;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;

/**
 * @author by nisie on 5/15/17.
 */

public interface FeedPlusTypeFactory extends KolPostTypeFactory {

    int type(ActivityCardViewModel viewModel);

    int type(PromotedShopViewModel viewModel);

    int type(PromoCardViewModel promoCardViewModel);

    int type(OfficialStoreBrandsViewModel brandsViewModel);

    int type(OfficialStoreCampaignViewModel officialStoreViewModel);

    int type(InspirationViewModel inspirationViewModel);

    int type(BlogViewModel viewModel);

    int type(PromotedProductViewModel promotedProductViewModel);

    int type(AddFeedModel addFeedModel);

    int type(RecentViewViewModel recentViewViewModel);

    int type(EmptyModel emptyModel);

    int type(ToppicksViewModel toppicksViewModel);

    int type(KolPostViewModel kolPostViewModel);

    int type(KolRecommendationViewModel kolRecommendationViewModel);

    int type(FeedTopAdsViewModel feedTopAdsViewModel);

    int type(FavoriteCtaViewModel favoriteCtaViewModel);

    int type(ContentProductViewModel contentProductViewModel);

    int type(EmptyFeedBeforeLoginModel emptyFeedBeforeLoginModel);

    int type(RetryModel retryModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}

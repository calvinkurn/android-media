package com.tokopedia.tkpd.beranda.presentation.view.adapter.factory;

import android.view.View;

import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BannerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.BrandsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategoryItemViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.CategorySectionViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.SellViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TickerViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.SaldoViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.TopPicksViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.EmptyTopAdsModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.EmptyTopAdsProductModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.FavoriteCtaViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.blog.BlogViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.inspiration.InspirationViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolRecommendationViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol.KolViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreBrandsViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore.OfficialStoreCampaignViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.ActivityCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.product.AddFeedModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromoCardViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromotedProductViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.promo.PromotedShopViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.recentview.RecentViewViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.topads.FeedTopAdsViewModel;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.toppicks.ToppicksViewModel;

/**
 * @author by errysuprayogi on 11/27/17.
 */

public interface HomeTypeFactory {

    int type(BannerViewModel bannerViewModel);

    int type(TickerViewModel tickerViewModel);

    int type(TopPicksViewModel topPicksViewModel);

    int type(BrandsViewModel brandsViewModel);

    int type(DigitalsViewModel digitalsViewModel);

    int type(CategorySectionViewModel categorySectionViewModel);

    int type(CategoryItemViewModel categoryItemViewModel);

    int type(SellViewModel sellViewModel);

    int type(SaldoViewModel saldoViewModel);

    int type(HeaderViewModel headerViewModel);

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

    int type(EmptyTopAdsModel emptyModel);

    int type(EmptyTopAdsProductModel emptyModel);

    int type(ToppicksViewModel toppicksViewModel);

    int type(KolViewModel kolViewModel);

    int type(KolRecommendationViewModel kolRecommendationViewModel);

    int type(FeedTopAdsViewModel feedTopAdsViewModel);

    int type(FavoriteCtaViewModel favoriteCtaViewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);

}

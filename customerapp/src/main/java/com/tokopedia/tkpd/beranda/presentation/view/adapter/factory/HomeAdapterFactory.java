package com.tokopedia.tkpd.beranda.presentation.view.adapter.factory;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.model.EmptyModel;
import com.tokopedia.core.base.adapter.model.RetryModel;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.core.base.adapter.viewholders.LoadingViewholder;
import com.tokopedia.tkpd.beranda.listener.HomeCategoryListener;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.BannerViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.BrandsViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.CategoryItemViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.CategorySectionViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.DigitalsViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.SellViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.HeaderViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.SaldoViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.TickerViewHolder;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewholder.TopPicksViewHolder;
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
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.blog.ImageBlogViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.blog.VideoBlogViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.emptytopads.EmptyTopAdsProductViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.emptytopads.EmptyTopAdsViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.favoritecta.FavoriteCtaViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.inspiration.InspirationViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol.KolRecommendationViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.kol.KolViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.officialstore.OfficialStoreBrandsViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.officialstore.OfficialStoreCampaignViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard.ActivityCardViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard.AddFeedViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard.EmptyFeedViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.productcard.RetryViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.promo.PromoViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.promo.PromotedProductViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.promo.PromotedShopViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.recentview.RecentViewViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.topads.FeedTopadsViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.viewholder.toppicks.ToppicksViewHolder;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
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
 * @author by errysuprayogi on 11/28/17.
 */

public class HomeAdapterFactory extends BaseAdapterTypeFactory implements HomeTypeFactory {

    private final HomeCategoryListener listener;
    private FeedPlus.View feedListener;
    private final FragmentManager fragmentManager;

    public HomeAdapterFactory(FragmentManager fragmentManager, HomeCategoryListener listener,
                              FeedPlus.View feedListener) {
        this.fragmentManager = fragmentManager;
        this.listener = listener;
        this.feedListener = feedListener;
    }

    @Override
    public int type(BannerViewModel bannerViewModel) {
        return BannerViewHolder.LAYOUT;
    }

    @Override
    public int type(TickerViewModel tickerViewModel) {
        return TickerViewHolder.LAYOUT;
    }

    @Override
    public int type(TopPicksViewModel topPicksViewModel) {
        return TopPicksViewHolder.LAYOUT;
    }

    @Override
    public int type(BrandsViewModel brandsViewModel) {
        return BrandsViewHolder.LAYOUT;
    }

    @Override
    public int type(DigitalsViewModel digitalsViewModel) {
        return DigitalsViewHolder.LAYOUT;
    }

    @Override
    public int type(CategorySectionViewModel categorySectionViewModel) {
        return CategorySectionViewHolder.LAYOUT;
    }

    @Override
    public int type(SellViewModel sellViewModel) {
        return SellViewHolder.LAYOUT;
    }

    @Override
    public int type(CategoryItemViewModel categoryItemViewModel) {
        return CategoryItemViewHolder.LAYOUT;
    }

    @Override
    public int type(SaldoViewModel saldoViewModel) {
        return SaldoViewHolder.LAYOUT;
    }

    @Override
    public int type(HeaderViewModel headerViewModel) {
        return HeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(ActivityCardViewModel activityCardViewModel) {
        return ActivityCardViewHolder.LAYOUT;
    }

    @Override
    public int type(PromotedShopViewModel viewModel) {
        return PromotedShopViewHolder.LAYOUT;
    }

    @Override
    public int type(PromoCardViewModel viewModel) {
        return PromoViewHolder.LAYOUT;
    }

    @Override
    public int type(OfficialStoreBrandsViewModel brandsViewModel) {
        return OfficialStoreBrandsViewHolder.LAYOUT;
    }

    @Override
    public int type(OfficialStoreCampaignViewModel officialStoreViewModel) {
        return OfficialStoreCampaignViewHolder.LAYOUT;
    }

    @Override
    public int type(InspirationViewModel inspirationViewModel) {
        return InspirationViewHolder.LAYOUT;
    }

    @Override
    public int type(BlogViewModel viewModel) {
        if (viewModel.getVideoUrl().equals(""))
            return ImageBlogViewHolder.LAYOUT;
        else
            return VideoBlogViewHolder.LAYOUT;
    }

    @Override
    public int type(PromotedProductViewModel promotedProductViewModel) {
        return PromotedProductViewHolder.LAYOUT;
    }

    @Override
    public int type(AddFeedModel addFeedModel) {
        return AddFeedViewHolder.LAYOUT;
    }

    @Override
    public int type(RecentViewViewModel recentViewViewModel) {
        return RecentViewViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyModel emptyModel) {
        return EmptyFeedViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyTopAdsModel emptyModel) {
        return EmptyTopAdsViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyTopAdsProductModel emptyModel) {
        return EmptyTopAdsProductViewHolder.LAYOUT;
    }

    @Override
    public int type(ToppicksViewModel toppicksViewModel) {
        return ToppicksViewHolder.LAYOUT;
    }

    @Override
    public int type(KolViewModel kolViewModel) {
        return KolViewHolder.LAYOUT;
    }

    @Override
    public int type(KolRecommendationViewModel kolRecommendationViewModel) {
        return KolRecommendationViewHolder.LAYOUT;
    }

    @Override
    public int type(FeedTopAdsViewModel feedTopAdsViewModel) {
        return FeedTopadsViewHolder.LAYOUT;
    }

    @Override
    public int type(FavoriteCtaViewModel favoriteCtaViewModel) {
        return FavoriteCtaViewHolder.LAYOUT;
    }

    @Override
    public int type(RetryModel retryModel) {
        return RetryViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {
        AbstractViewHolder viewHolder;
        if (type == BannerViewHolder.LAYOUT)
            viewHolder = new BannerViewHolder(view, listener);
        else if (type == TickerViewHolder.LAYOUT)
            viewHolder = new TickerViewHolder(view, listener);
        else if (type == TopPicksViewHolder.LAYOUT)
            viewHolder = new TopPicksViewHolder(view, listener);
        else if (type == BrandsViewHolder.LAYOUT)
            viewHolder = new BrandsViewHolder(view, listener);
        else if (type == DigitalsViewHolder.LAYOUT)
            viewHolder = new DigitalsViewHolder(fragmentManager, view, listener);
        else if (type == CategorySectionViewHolder.LAYOUT)
            viewHolder = new CategorySectionViewHolder(view, listener);
        else if (type == CategoryItemViewHolder.LAYOUT)
            viewHolder = new CategoryItemViewHolder(view, listener);
        else if (type == SellViewHolder.LAYOUT)
            viewHolder = new SellViewHolder(view, listener);
        else if (type == SaldoViewHolder.LAYOUT)
            viewHolder = new SaldoViewHolder(view, listener);
        else if(type == HeaderViewHolder.LAYOUT)
            viewHolder = new HeaderViewHolder(view, listener);
        else if (type == EmptyFeedViewHolder.LAYOUT)
            viewHolder = new EmptyFeedViewHolder(view, feedListener);
        else if (type == RetryViewHolder.LAYOUT)
            viewHolder = new RetryViewHolder(view, feedListener);
        else if (type == AddFeedViewHolder.LAYOUT)
            viewHolder = new AddFeedViewHolder(view, feedListener);
        else if (type == ActivityCardViewHolder.LAYOUT)
            viewHolder = new ActivityCardViewHolder(view, feedListener);
        else if (type == PromotedShopViewHolder.LAYOUT)
            viewHolder = new PromotedShopViewHolder(view, feedListener);
        else if (type == PromoViewHolder.LAYOUT)
            viewHolder = new PromoViewHolder(view, feedListener);
        else if (type == OfficialStoreCampaignViewHolder.LAYOUT)
            viewHolder = new OfficialStoreCampaignViewHolder(view, feedListener);
        else if (type == OfficialStoreBrandsViewHolder.LAYOUT)
            viewHolder = new OfficialStoreBrandsViewHolder(view, feedListener);
        else if (type == InspirationViewHolder.LAYOUT)
            viewHolder = new InspirationViewHolder(view, feedListener);
        else if (type == ImageBlogViewHolder.LAYOUT)
            viewHolder = new ImageBlogViewHolder(view, feedListener);
        else if (type == VideoBlogViewHolder.LAYOUT)
            viewHolder = new VideoBlogViewHolder(view, feedListener);
        else if (type == PromotedProductViewHolder.LAYOUT)
            viewHolder = new PromotedProductViewHolder(view, feedListener);
        else if (type == RecentViewViewHolder.LAYOUT)
            viewHolder = new RecentViewViewHolder(view, feedListener);
        else if (type == EmptyTopAdsViewHolder.LAYOUT)
            viewHolder = new LoadingViewholder(view);
        else if (type == EmptyTopAdsProductViewHolder.LAYOUT)
            viewHolder = new LoadingViewholder(view);
        else if (type == ToppicksViewHolder.LAYOUT)
            viewHolder = new LoadingViewholder(view);
        else if (type == KolViewHolder.LAYOUT)
            viewHolder = new LoadingViewholder(view);
        else if (type == KolRecommendationViewHolder.LAYOUT)
            viewHolder = new LoadingViewholder(view);
        else if (type == FeedTopadsViewHolder.LAYOUT)
            viewHolder = new LoadingViewholder(view);
        else if (type == FavoriteCtaViewHolder.LAYOUT)
            viewHolder = new FavoriteCtaViewHolder(view, feedListener);
        else viewHolder = super.createViewHolder(view, type);

        return viewHolder;
    }
}

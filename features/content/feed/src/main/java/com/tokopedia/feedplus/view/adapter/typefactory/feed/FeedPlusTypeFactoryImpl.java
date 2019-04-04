package com.tokopedia.feedplus.view.adapter.typefactory.feed;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory;
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation
        .FeedRecommendationViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter;
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder;
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter;
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel;
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel;
import com.tokopedia.feedcomponent.view.widget.CardTitleView;
import com.tokopedia.feedplus.view.adapter.viewholder.EmptyFeedBeforeLoginViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.favoritecta.FavoriteCtaViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.inspiration.InspirationViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.kol.ContentProductViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.kol.KolRecommendationViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.kol.PollViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.kol.ProductCommunicationViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.kol.WhitelistViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.officialstore.OfficialStoreBrandsViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.officialstore.OfficialStoreCampaignViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.productcard.ActivityCardViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.productcard.AddFeedViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.productcard.EmptyFeedViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.productcard.RetryViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.promo.PromotedProductViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.promo.PromotedShopViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.topads.FeedTopadsViewHolder;
import com.tokopedia.feedplus.view.analytics.FeedAnalytics;
import com.tokopedia.feedplus.view.fragment.FeedPlusFragment;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.EmptyFeedBeforeLoginModel;
import com.tokopedia.feedplus.view.viewmodel.FavoriteCtaViewModel;
import com.tokopedia.feedplus.view.viewmodel.RetryModel;
import com.tokopedia.feedplus.view.viewmodel.inspiration.InspirationViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.ContentProductViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.KolRecommendationViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.PollViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.ProductCommunicationViewModel;
import com.tokopedia.feedplus.view.viewmodel.kol.WhitelistViewModel;
import com.tokopedia.feedplus.view.viewmodel.officialstore.OfficialStoreBrandsViewModel;
import com.tokopedia.feedplus.view.viewmodel.officialstore.OfficialStoreCampaignViewModel;
import com.tokopedia.feedplus.view.viewmodel.product.ActivityCardViewModel;
import com.tokopedia.feedplus.view.viewmodel.product.AddFeedModel;
import com.tokopedia.feedplus.view.viewmodel.promo.PromotedProductViewModel;
import com.tokopedia.feedplus.view.viewmodel.promo.PromotedShopViewModel;
import com.tokopedia.feedplus.view.viewmodel.topads.FeedTopAdsViewModel;
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.EmptyKolPostViewHolder;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.ExploreViewHolder;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder;
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostYoutubeViewHolder;
import com.tokopedia.kol.feature.post.view.listener.KolPostListener;
import com.tokopedia.kol.feature.post.view.viewmodel.EmptyKolPostViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.EntryPointViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.ExploreViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel;
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostYoutubeViewModel;
import com.tokopedia.topads.sdk.listener.TopAdsInfoClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;

import org.jetbrains.annotations.NotNull;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusTypeFactoryImpl extends BaseAdapterTypeFactory
        implements FeedPlusTypeFactory, KolPostTypeFactory, DynamicFeedTypeFactory {

    private final FeedPlus.View viewListener;
    private final TopAdsItemClickListener topAdsItemClickListener;
    private final TopAdsInfoClickListener topAdsInfoClickListener;
    private final FeedPlus.View.Kol kolViewListener;
    private final FeedPlus.View.Polling pollingViewListener;
    private final KolPostListener.View.ViewHolder kolPostListener;
    private final DynamicPostViewHolder.DynamicPostListener dynamicPostListener;
    private final BannerAdapter.BannerItemListener bannerListener;
    private final TopadsShopViewHolder.TopadsShopListener topadsShopListener;
    private final RecommendationCardAdapter.RecommendationCardListener recommendationCardListener;
    private final CardTitleView.CardTitleListener cardTitleListener;
    private final ImagePostViewHolder.ImagePostListener imagePostListener;
    private final YoutubeViewHolder.YoutubePostListener youtubePostListener;
    private final PollAdapter.PollOptionListener pollOptionListener;
    private final GridPostAdapter.GridItemListener gridItemListener;
    private final VideoViewHolder.VideoViewListener videoViewListener;
    private final FeedAnalytics analytics;

    public FeedPlusTypeFactoryImpl(FeedPlusFragment context, FeedAnalytics analytics) {
        this.viewListener = context;
        this.topAdsItemClickListener = context;
        this.topAdsInfoClickListener = context;
        this.kolViewListener = context;
        this.kolPostListener = context;
        this.pollingViewListener = context;
        this.dynamicPostListener = context;
        this.bannerListener = context;
        this.topadsShopListener = context;
        this.recommendationCardListener = context;
        this.cardTitleListener = context;
        this.imagePostListener = context;
        this.youtubePostListener = context;
        this.pollOptionListener = context;
        this.gridItemListener = context;
        this.videoViewListener = context;
        this.analytics = analytics;
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
    public int type(PromotedProductViewModel promotedProductViewModel) {
        return PromotedProductViewHolder.LAYOUT;
    }

    @Override
    public int type(AddFeedModel addFeedModel) {
        return AddFeedViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyModel emptyModel) {
        return EmptyFeedViewHolder.LAYOUT;
    }

    @Override
    public int type(KolPostViewModel kolViewModel) {
        return KolPostViewHolder.LAYOUT;
    }

    @Override
    public int type(KolPostYoutubeViewModel kolPostYoutubeViewModel) {
        return KolPostYoutubeViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyKolPostViewModel emptyKolPostViewModel) {
        return EmptyKolPostViewHolder.LAYOUT;
    }

    @Override
    public int type(ExploreViewModel exploreViewModel) {
        return ExploreViewHolder.LAYOUT;
    }

    @Override
    public int type(EntryPointViewModel entryPointViewModel) {
        throw new IllegalStateException(this.getClass().getSimpleName() + " doesn't support "
                + EntryPointViewModel.class.getSimpleName());
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
    public int type(ContentProductViewModel contentProductViewModel) {
        return ContentProductViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductCommunicationViewModel productCommunicationViewModel) {
        return ProductCommunicationViewHolder.LAYOUT;
    }

    @Override
    public int type(PollViewModel pollViewModel) {
        return PollViewHolder.LAYOUT;
    }

    @Override
    public int type(WhitelistViewModel whitelistViewModel) {
        return WhitelistViewHolder.LAYOUT;
    }

    @Override
    public int type(EmptyFeedBeforeLoginModel emptyFeedBeforeLoginModel) {
        return EmptyFeedBeforeLoginViewHolder.LAYOUT;
    }

    @Override
    public int type(RetryModel retryModel) {
        return RetryViewHolder.LAYOUT;
    }

    @Override
    public int type(@NotNull DynamicPostViewModel dynamicPostViewModel) {
        return DynamicPostViewHolder.Companion.getLAYOUT();
    }

    @Override
    public int type(@NotNull FeedRecommendationViewModel feedRecommendationViewModel) {
        return FeedRecommendationViewHolder.Companion.getLAYOUT();
    }

    @Override
    public int type(@NotNull BannerViewModel bannerViewModel) {
        return BannerViewHolder.Companion.getLAYOUT();
    }

    @Override
    public int type(@NotNull TopadsShopViewModel topadsShopViewModel) {
        return TopadsShopViewHolder.Companion.getLAYOUT();
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == EmptyFeedViewHolder.LAYOUT)
            viewHolder = new EmptyFeedViewHolder(view, viewListener);
        else if (type == RetryViewHolder.LAYOUT)
            viewHolder = new RetryViewHolder(view, viewListener);
        else if (type == AddFeedViewHolder.LAYOUT)
            viewHolder = new AddFeedViewHolder(view, viewListener);
        else if (type == ActivityCardViewHolder.LAYOUT)
            viewHolder = new ActivityCardViewHolder(view, viewListener);
        else if (type == PromotedShopViewHolder.LAYOUT)
            viewHolder = new PromotedShopViewHolder(view, viewListener);
        else if (type == OfficialStoreCampaignViewHolder.LAYOUT)
            viewHolder = new OfficialStoreCampaignViewHolder(view, viewListener);
        else if (type == OfficialStoreBrandsViewHolder.LAYOUT)
            viewHolder = new OfficialStoreBrandsViewHolder(view, viewListener);
        else if (type == InspirationViewHolder.LAYOUT)
            viewHolder = new InspirationViewHolder(view, viewListener);
        else if (type == PromotedProductViewHolder.LAYOUT)
            viewHolder = new PromotedProductViewHolder(view, viewListener);
        else if (type == KolRecommendationViewHolder.LAYOUT)
            viewHolder = new KolRecommendationViewHolder(view, kolViewListener, analytics);
        else if (type == FeedTopadsViewHolder.LAYOUT)
            viewHolder = new FeedTopadsViewHolder(view,
                    topAdsItemClickListener,
                    topAdsInfoClickListener);
        else if (type == FavoriteCtaViewHolder.LAYOUT)
            viewHolder = new FavoriteCtaViewHolder(view, viewListener);
        else if (type == ContentProductViewHolder.LAYOUT)
            viewHolder = new ContentProductViewHolder(view, viewListener);
        else if (type == ProductCommunicationViewHolder.LAYOUT)
            viewHolder = new ProductCommunicationViewHolder(view, viewListener, analytics);
        else if (type == PollViewHolder.LAYOUT)
            viewHolder = new PollViewHolder(view, kolViewListener, pollingViewListener);
        else if (type == EmptyFeedBeforeLoginViewHolder.LAYOUT)
            viewHolder = new EmptyFeedBeforeLoginViewHolder(view, viewListener);
        else if (type == KolPostViewHolder.LAYOUT)
            viewHolder = new KolPostViewHolder(view, kolPostListener, KolPostViewHolder.Type.FEED);
        else if (type == KolPostYoutubeViewHolder.LAYOUT)
            viewHolder = new KolPostYoutubeViewHolder(view, kolPostListener, KolPostYoutubeViewHolder.Type.FEED);
        else if (type == EmptyKolPostViewHolder.LAYOUT)
            viewHolder = new EmptyKolPostViewHolder(view);
        else if (type == ExploreViewHolder.LAYOUT)
            viewHolder = new ExploreViewHolder(view, kolPostListener);
        else if (type == WhitelistViewHolder.LAYOUT)
            viewHolder = new WhitelistViewHolder(view, viewListener);
        else if (type == DynamicPostViewHolder.Companion.getLAYOUT()) {
            viewHolder = new DynamicPostViewHolder(
                    view,
                    dynamicPostListener,
                    cardTitleListener,
                    imagePostListener,
                    youtubePostListener,
                    pollOptionListener,
                    gridItemListener,
                    videoViewListener
            );
        }
        else if (type == FeedRecommendationViewHolder.Companion.getLAYOUT()) {
            viewHolder = new FeedRecommendationViewHolder(view, recommendationCardListener, cardTitleListener);
        }
        else if (type == BannerViewHolder.Companion.getLAYOUT()) {
            viewHolder = new BannerViewHolder(view, bannerListener, cardTitleListener);
        }
        else if (type == TopadsShopViewHolder.Companion.getLAYOUT()) {
            viewHolder = new TopadsShopViewHolder(view, topadsShopListener, cardTitleListener);
        }
        else
            viewHolder = super.createViewHolder(view, type);
        return viewHolder;
    }

    @Override
    public void setType(KolPostViewHolder.Type type) {

    }
}

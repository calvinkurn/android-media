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
import com.tokopedia.feedplus.view.adapter.viewholder.kol.WhitelistViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.productcard.EmptyFeedViewHolder;
import com.tokopedia.feedplus.view.adapter.viewholder.productcard.RetryViewHolder;
import com.tokopedia.feedplus.view.analytics.FeedAnalytics;
import com.tokopedia.feedplus.view.fragment.FeedPlusFragment;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.view.viewmodel.EmptyFeedBeforeLoginModel;
import com.tokopedia.feedplus.view.viewmodel.RetryModel;
import com.tokopedia.feedplus.view.viewmodel.kol.WhitelistViewModel;
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
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusTypeFactoryImpl extends BaseAdapterTypeFactory
        implements FeedPlusTypeFactory, KolPostTypeFactory, DynamicFeedTypeFactory {

    private final FeedPlus.View viewListener;
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
    private final UserSessionInterface userSession;

    public FeedPlusTypeFactoryImpl(FeedPlusFragment context, FeedAnalytics analytics,
                                   UserSessionInterface userSession) {
        this.viewListener = context;
        this.kolPostListener = context;
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
        this.userSession = userSession;
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
                    videoViewListener,
                    userSession
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

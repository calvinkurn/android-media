package com.tokopedia.feedplus.view.adapter.typefactory.feed

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostNewViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.FeedRecommendationViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.shimmer.ShimmerViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsBannerViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsHeadlineViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TopAdsBannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.viewmodel.shimmer.ShimmerUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopUiModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.feedplus.view.adapter.viewholder.EmptyFeedBeforeLoginViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.carouselplaycard.CarouselPlayCardViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.onboarding.OnboardingViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.productcard.EmptyFeedViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.productcard.RetryViewHolder
import com.tokopedia.feedplus.view.fragment.FeedPlusFragment
import com.tokopedia.feedplus.view.viewmodel.EmptyFeedBeforeLoginModel
import com.tokopedia.feedplus.view.viewmodel.RetryModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import com.tokopedia.interest_pick_common.view.adapter.InterestPickAdapter
import com.tokopedia.kolcommon.view.listener.KolPostViewHolderListener
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by nisie on 5/15/17.
 */

class FeedPlusTypeFactoryImpl(
        context: FeedPlusFragment,
        private val userSession: UserSessionInterface,
        private val interestPickItemListener: InterestPickAdapter.InterestPickItemListener,
        private val playWidgetCoordinator: PlayWidgetCoordinator
) : BaseAdapterTypeFactory(), FeedPlusTypeFactory, DynamicFeedTypeFactory {

    private val kolPostListener: KolPostViewHolderListener
    private val dynamicPostListener: DynamicPostViewHolder.DynamicPostListener
    private val bannerListener: BannerAdapter.BannerItemListener
    private val topadsShopListener: TopadsShopViewHolder.TopadsShopListener
    private val recommendationCardListener: RecommendationCardAdapter.RecommendationCardListener
    private val cardTitleListener: CardTitleView.CardTitleListener
    private val imagePostListener: ImagePostViewHolder.ImagePostListener
    private val youtubePostListener: YoutubeViewHolder.YoutubePostListener
    private val pollOptionListener: PollAdapter.PollOptionListener
    private val gridItemListener: GridPostAdapter.GridItemListener
    private val videoViewListener: VideoViewHolder.VideoViewListener
    private val feedMultipleImageViewListener: FeedMultipleImageView.FeedMultipleImageViewListener
    private val highlightListener: HighlightAdapter.HighlightListener
    private val emptyFeedBeforeLoginListener: EmptyFeedBeforeLoginViewHolder.EmptyFeedBeforeLoginListener
    private val retryViewHolderListener: RetryViewHolder.RetryViewHolderListener
    private val emptyFeedViewHolderListener: EmptyFeedViewHolder.EmptyFeedListener
    private val topAdsBannerListener: TopAdsBannerViewHolder.TopAdsBannerListener
    private val topAdsHeadlineListener: TopAdsHeadlineViewHolder.TopAdsHeadlineListener

    init {
        this.kolPostListener = context
        this.dynamicPostListener = context
        this.bannerListener = context
        this.topadsShopListener = context
        this.recommendationCardListener = context
        this.cardTitleListener = context
        this.imagePostListener = context
        this.youtubePostListener = context
        this.pollOptionListener = context
        this.gridItemListener = context
        this.videoViewListener = context
        this.feedMultipleImageViewListener = context
        this.highlightListener = context
        this.emptyFeedBeforeLoginListener = context
        this.retryViewHolderListener = context
        this.emptyFeedViewHolderListener = context
        this.topAdsBannerListener = context
        this.topAdsHeadlineListener = context
    }

    override fun type(emptyModel: EmptyModel): Int {
        return EmptyFeedViewHolder.LAYOUT
    }

    override fun type(emptyFeedBeforeLoginModel: EmptyFeedBeforeLoginModel): Int {
        return EmptyFeedBeforeLoginViewHolder.LAYOUT
    }

    override fun type(retryModel: RetryModel): Int {
        return RetryViewHolder.LAYOUT
    }

    override fun type(dynamicPostViewModel: DynamicPostViewModel): Int {
        return DynamicPostViewHolder.LAYOUT
    }

    override fun type(feedRecommendationViewModel: FeedRecommendationViewModel): Int {
        return FeedRecommendationViewHolder.LAYOUT
    }

    override fun type(bannerViewModel: BannerViewModel): Int {
        return BannerViewHolder.LAYOUT
    }

    override fun type(topadsShopUiModel: TopadsShopUiModel): Int {
        return TopadsShopViewHolder.LAYOUT
    }

    override fun type(topadsHeadlineUiModel: TopadsHeadlineUiModel): Int {
        return TopAdsHeadlineViewHolder.LAYOUT
    }

    override fun type(highlightViewModel: HighlightViewModel): Int {
        return HighlightViewHolder.LAYOUT
    }

    override fun type(topAdsBannerViewmodel: TopAdsBannerViewModel): Int {
        return TopAdsBannerViewHolder.LAYOUT
    }

    override fun type(onboardingViewModel: OnboardingViewModel): Int {
        return OnboardingViewHolder.LAYOUT
    }

    override fun type(carouselPlayCardViewModel: CarouselPlayCardViewModel): Int {
        return CarouselPlayCardViewHolder.LAYOUT
    }

    override fun type(shimmerUiModel: ShimmerUiModel): Int {
        return ShimmerViewHolder.LAYOUT
    }

    override fun type(dynamicPostUiModel: DynamicPostUiModel): Int {
        return DynamicPostNewViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {

        val viewHolder: AbstractViewHolder<*>

        when (type) {
            EmptyFeedViewHolder.LAYOUT -> viewHolder = EmptyFeedViewHolder(view, emptyFeedViewHolderListener)
            RetryViewHolder.LAYOUT -> viewHolder = RetryViewHolder(view, retryViewHolderListener)
            EmptyFeedBeforeLoginViewHolder.LAYOUT -> viewHolder = EmptyFeedBeforeLoginViewHolder(view, emptyFeedBeforeLoginListener)
            DynamicPostViewHolder.LAYOUT -> {
                viewHolder = DynamicPostViewHolder(
                        view,
                        dynamicPostListener,
                        cardTitleListener,
                        imagePostListener,
                        youtubePostListener,
                        pollOptionListener,
                        gridItemListener,
                        videoViewListener,
                        feedMultipleImageViewListener,
                        userSession
                )
            }
            FeedRecommendationViewHolder.LAYOUT -> {
                viewHolder = FeedRecommendationViewHolder(view, recommendationCardListener, cardTitleListener)
            }
            BannerViewHolder.LAYOUT -> {
                viewHolder = BannerViewHolder(view, bannerListener, cardTitleListener)
            }
            TopadsShopViewHolder.LAYOUT -> {
                viewHolder = TopadsShopViewHolder(view, topadsShopListener, cardTitleListener)
            }
            TopAdsHeadlineViewHolder.LAYOUT -> {
                viewHolder = TopAdsHeadlineViewHolder(view, userSession, topAdsHeadlineListener)
            }
            OnboardingViewHolder.LAYOUT -> {
                viewHolder = OnboardingViewHolder(view, userSession, interestPickItemListener)
            }
            TopAdsBannerViewHolder.LAYOUT -> {
                viewHolder = TopAdsBannerViewHolder(view, topAdsBannerListener, cardTitleListener)
            }
            CarouselPlayCardViewHolder.LAYOUT -> {
                viewHolder = CarouselPlayCardViewHolder(
                        PlayWidgetViewHolder(
                                itemView = view,
                                coordinator = playWidgetCoordinator
                        )
                )
            }
            ShimmerViewHolder.LAYOUT -> {
                viewHolder = ShimmerViewHolder(view)
            }
            DynamicPostNewViewHolder.LAYOUT -> {
                viewHolder = DynamicPostNewViewHolder(view, userSession, dynamicPostListener,videoViewListener,gridItemListener)
            }
            HighlightViewHolder.LAYOUT -> {
                viewHolder = HighlightViewHolder(view, highlightListener, cardTitleListener) as AbstractViewHolder<Visitable<*>>
            }
            else -> viewHolder = super.createViewHolder(view, type)
        }
        return viewHolder
    }
}

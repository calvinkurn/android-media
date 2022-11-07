package com.tokopedia.feed_shop.shop.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsBannerViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsHeadlineV2ViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopAdsHeadlineViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TopAdsBannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.shimmer.ShimmerUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadLineV2Model
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopUiModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.feed_shop.shop.view.adapter.holder.EmptyFeedShopSellerMigrationViewHolder
import com.tokopedia.feed_shop.shop.view.adapter.holder.EmptyFeedShopViewHolder
import com.tokopedia.feed_shop.shop.view.adapter.holder.WhitelistViewHolder
import com.tokopedia.feed_shop.shop.view.contract.FeedShopContract
import com.tokopedia.feed_shop.shop.view.model.EmptyFeedShopSellerMigrationUiModel
import com.tokopedia.feed_shop.shop.view.model.EmptyFeedShopUiModel
import com.tokopedia.feed_shop.shop.view.model.WhitelistUiModel
import com.tokopedia.feedcomponent.view.viewmodel.shoprecommendation.ShopRecomWidgetModel
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by yfsx on 08/05/19.
 */
class FeedShopFactoryImpl(private val mainView: FeedShopContract.View,
                          private val dynamicPostListener: DynamicPostViewHolder.DynamicPostListener,
                          private val bannerListener: BannerAdapter.BannerItemListener,
                          private val topadsShopListener: TopadsShopViewHolder.TopadsShopListener,
                          private val recommendationCardListener: RecommendationCardAdapter.RecommendationCardListener,
                          private val cardTitleListener: CardTitleView.CardTitleListener,
                          private val imagePostListener: ImagePostViewHolder.ImagePostListener,
                          private val youtubePostListener: YoutubeViewHolder.YoutubePostListener,
                          private val pollOptionListener: PollAdapter.PollOptionListener,
                          private val gridItemListener: GridPostAdapter.GridItemListener,
                          private val videoViewListener: VideoViewHolder.VideoViewListener,
                          private val feedMultipleImageViewListener: FeedMultipleImageView.FeedMultipleImageViewListener,
                          private val highlightListener: HighlightAdapter.HighlightListener,
                          private val topAdsBannerListener: TopAdsBannerViewHolder.TopAdsBannerListener,
                          private val userSession: UserSessionInterface) :
        BaseAdapterTypeFactory(), DynamicFeedTypeFactory, FeedShopTypeFactory {

    override fun type(dynamicPostViewModel: DynamicPostViewModel): Int {
        return DynamicPostViewHolder.LAYOUT
    }

    override fun type(topadsShopUiModel: TopadsShopUiModel): Int {
        return TopadsShopViewHolder.LAYOUT
    }

    override fun type(topadsHeadlineUiModel: TopadsHeadlineUiModel): Int {
        return TopAdsHeadlineViewHolder.LAYOUT
    }

    override fun type(topadsHeadlineUiModel: TopadsHeadLineV2Model): Int {
        return TopAdsHeadlineV2ViewHolder.LAYOUT
    }

    override fun type(whitelistUiModel: WhitelistUiModel): Int {
        return WhitelistViewHolder.LAYOUT
    }

    override fun type(emptyFeedShopUiModel: EmptyFeedShopUiModel): Int {
        return EmptyFeedShopViewHolder.LAYOUT
    }

    override fun type(emptyFeedShopSellerMigrationUiModel: EmptyFeedShopSellerMigrationUiModel): Int {
        return EmptyFeedShopSellerMigrationViewHolder.LAYOUT
    }

    override fun type(topAdsBannerViewmodel: TopAdsBannerViewModel): Int {
        return TopAdsBannerViewHolder.LAYOUT
    }

    override fun type(carouselPlayCardViewModel: CarouselPlayCardViewModel): Int {
        return 0
    }

    override fun type(shopRecomWidgetModel: ShopRecomWidgetModel): Int {
        return 0
    }

    override fun type(shimmerUiModel: ShimmerUiModel): Int {
        return 0
    }

    override fun type(dynamicPostUiModel: DynamicPostUiModel): Int {
        return 0
    }

    @Suppress("UNCHECKED_CAST")
    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<Visitable<*>> {
        return when (type) {
            DynamicPostViewHolder.LAYOUT ->
                DynamicPostViewHolder(parent,
                        dynamicPostListener,
                        cardTitleListener,
                        imagePostListener,
                        youtubePostListener,
                        pollOptionListener,
                        gridItemListener,
                        videoViewListener,
                        feedMultipleImageViewListener,
                        userSession) as AbstractViewHolder<Visitable<*>>
            TopadsShopViewHolder.LAYOUT ->
                TopadsShopViewHolder(parent, topadsShopListener, cardTitleListener) as AbstractViewHolder<Visitable<*>>
            TopAdsHeadlineViewHolder.LAYOUT ->
                TopAdsHeadlineViewHolder(parent, userSession) as AbstractViewHolder<Visitable<*>>
            TopAdsHeadlineV2ViewHolder.LAYOUT ->
                TopAdsHeadlineV2ViewHolder(
                        parent,
                        userSession,
                        dynamicPostListener = dynamicPostListener,
                        videoViewListener = videoViewListener,
                        gridItemListener = gridItemListener,
                        imagePostListener = imagePostListener
                ) as AbstractViewHolder<Visitable<*>>
            WhitelistViewHolder.LAYOUT ->
                WhitelistViewHolder(parent, mainView) as AbstractViewHolder<Visitable<*>>
            EmptyFeedShopViewHolder.LAYOUT ->
                EmptyFeedShopViewHolder(parent, mainView) as AbstractViewHolder<Visitable<*>>
            EmptyFeedShopSellerMigrationViewHolder.LAYOUT ->
                EmptyFeedShopSellerMigrationViewHolder(parent, mainView) as AbstractViewHolder<Visitable<*>>
            TopAdsBannerViewHolder.LAYOUT ->
                TopAdsBannerViewHolder(parent, topAdsBannerListener, cardTitleListener) as AbstractViewHolder<Visitable<*>>
            else -> super.createViewHolder(parent, type)

        }
    }
}

package com.tokopedia.feedplus.view.adapter.typefactory.feed

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.shoprecom.ShopRecomWidgetCarousel
import com.tokopedia.feedcomponent.shoprecom.callback.ShopRecomWidgetCallback
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostNewViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.shimmer.ShimmerViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.*
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TopAdsBannerModel
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostModel
import com.tokopedia.feedcomponent.view.viewmodel.shimmer.ShimmerUiModel
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomWidgetModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadLineV2Model
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopUiModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.feedplus.view.adapter.viewholder.carouselplaycard.CarouselPlayCardViewHolder
import com.tokopedia.feedplus.view.fragment.FeedPlusFragment
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by nisie on 5/15/17.
 */

class FeedPlusTypeFactoryImpl(
        private val context: FeedPlusFragment,
        private val userSession: UserSessionInterface,
        private val playWidgetCoordinator: PlayWidgetCoordinator
) : BaseAdapterTypeFactory(), FeedPlusTypeFactory, DynamicFeedTypeFactory {

    private val dynamicPostListener: DynamicPostViewHolder.DynamicPostListener
    private val topadsShopListener: TopadsShopViewHolder.TopadsShopListener
    private val cardTitleListener: CardTitleView.CardTitleListener
    private val imagePostListener: ImagePostViewHolder.ImagePostListener
    private val youtubePostListener: YoutubeViewHolder.YoutubePostListener
    private val pollOptionListener: PollAdapter.PollOptionListener
    private val gridItemListener: GridPostAdapter.GridItemListener
    private val videoViewListener: VideoViewHolder.VideoViewListener
    private val feedMultipleImageViewListener: FeedMultipleImageView.FeedMultipleImageViewListener
    private val topAdsBannerListener: TopAdsBannerViewHolder.TopAdsBannerListener
    private val topAdsHeadlineListener: TopAdsHeadlineListener
    private val shopRecomCallback: ShopRecomWidgetCallback

    init {
        this.dynamicPostListener = context
        this.topadsShopListener = context
        this.cardTitleListener = context
        this.imagePostListener = context
        this.youtubePostListener = context
        this.pollOptionListener = context
        this.gridItemListener = context
        this.videoViewListener = context
        this.feedMultipleImageViewListener = context
        this.topAdsBannerListener = context
        this.topAdsHeadlineListener = context
        this.shopRecomCallback = context
    }

    override fun type(dynamicPostModel: DynamicPostModel): Int {
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

    override fun type(topAdsBannerViewmodel: TopAdsBannerModel): Int {
        return TopAdsBannerViewHolder.LAYOUT
    }

    override fun type(carouselPlayCardModel: CarouselPlayCardModel): Int {
        return CarouselPlayCardViewHolder.LAYOUT
    }

    override fun type(shopRecomWidgetModel: ShopRecomWidgetModel): Int {
        return ShopRecomWidgetCarousel.LAYOUT
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
            TopadsShopViewHolder.LAYOUT -> {
                viewHolder = TopadsShopViewHolder(view, topadsShopListener, cardTitleListener)
            }
            TopAdsHeadlineViewHolder.LAYOUT -> {
                viewHolder = TopAdsHeadlineViewHolder(view, userSession, topAdsHeadlineListener)
            }
            TopAdsHeadlineV2ViewHolder.LAYOUT -> {
                viewHolder = TopAdsHeadlineV2ViewHolder(view, userSession, topAdsHeadlineListener,
                    dynamicPostListener,
                    videoViewListener,
                    gridItemListener,
                    imagePostListener)
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
            ShopRecomWidgetCarousel.LAYOUT -> {
                viewHolder = ShopRecomWidgetCarousel(view, shopRecomCallback, context)
            }
            ShimmerViewHolder.LAYOUT -> {
                viewHolder = ShimmerViewHolder(view)
            }
            DynamicPostNewViewHolder.LAYOUT -> {
                viewHolder = DynamicPostNewViewHolder(
                    view,
                    userSession,
                    dynamicPostListener,
                    videoViewListener,
                    gridItemListener,
                    imagePostListener
                )
            }
            else -> viewHolder = super.createViewHolder(view, type)
        }
        return viewHolder
    }
}

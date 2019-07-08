package com.tokopedia.shop.feed.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.FeedRecommendationViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.shop.feed.view.adapter.holder.EmptyFeedShopViewHolder
import com.tokopedia.shop.feed.view.adapter.holder.WhitelistViewHolder
import com.tokopedia.shop.feed.view.contract.FeedShopContract
import com.tokopedia.shop.feed.view.model.EmptyFeedShopViewModel
import com.tokopedia.shop.feed.view.model.WhitelistViewModel
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
                          private val userSession : UserSessionInterface):
        BaseAdapterTypeFactory(), DynamicFeedTypeFactory, FeedShopTypeFactory {

    override fun type(dynamicPostViewModel: DynamicPostViewModel): Int {
        return DynamicPostViewHolder.LAYOUT
    }

    override fun type(feedRecommendationViewModel: FeedRecommendationViewModel): Int {
        return FeedRecommendationViewHolder.LAYOUT
    }

    override fun type(bannerViewModel: BannerViewModel): Int {
        return BannerViewHolder.LAYOUT
    }

    override fun type(topadsShopViewModel: TopadsShopViewModel): Int {
        return TopadsShopViewHolder.LAYOUT
    }

    override fun type(whitelistViewModel: WhitelistViewModel): Int {
        return WhitelistViewHolder.LAYOUT
    }

    override fun type(emptyFeedShopViewModel: EmptyFeedShopViewModel): Int {
        return EmptyFeedShopViewHolder.LAYOUT
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
                        userSession) as AbstractViewHolder< Visitable<*>>
            FeedRecommendationViewHolder.LAYOUT ->
                FeedRecommendationViewHolder(parent, recommendationCardListener, cardTitleListener) as AbstractViewHolder<Visitable<*>>
            BannerViewHolder.LAYOUT ->
                BannerViewHolder(parent, bannerListener, cardTitleListener) as AbstractViewHolder<Visitable<*>>
            TopadsShopViewHolder.LAYOUT ->
                TopadsShopViewHolder(parent, topadsShopListener, cardTitleListener) as AbstractViewHolder<Visitable<*>>
            WhitelistViewHolder.LAYOUT ->
                WhitelistViewHolder(parent, mainView) as AbstractViewHolder<Visitable<*>>
            EmptyFeedShopViewHolder.LAYOUT ->
                EmptyFeedShopViewHolder(parent, mainView) as AbstractViewHolder<Visitable<*>>
            else -> super.createViewHolder(parent, type)

        }
    }
}
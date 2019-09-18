package com.tokopedia.feedplus.view.adapter.typefactory.feed

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.adapter.post.DynamicFeedTypeFactory
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.banner.BannerViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.highlight.HighlightViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.video.VideoViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.FeedRecommendationViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.recommendation.RecommendationCardAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.topads.TopadsShopViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.feedcomponent.view.widget.FeedMultipleImageView
import com.tokopedia.feedplus.view.adapter.viewholder.EmptyFeedBeforeLoginViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.kol.WhitelistViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.onboarding.OnboardingViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.productcard.EmptyFeedViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.productcard.RetryViewHolder
import com.tokopedia.feedplus.view.fragment.FeedPlusFragment
import com.tokopedia.feedplus.view.listener.FeedPlus
import com.tokopedia.feedplus.view.viewmodel.EmptyFeedBeforeLoginModel
import com.tokopedia.feedplus.view.viewmodel.RetryModel
import com.tokopedia.feedplus.view.viewmodel.kol.WhitelistViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingDataViewModel
import com.tokopedia.feedplus.view.viewmodel.onboarding.OnboardingViewModel
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory
import com.tokopedia.kol.feature.post.view.adapter.viewholder.EmptyKolPostViewHolder
import com.tokopedia.kol.feature.post.view.adapter.viewholder.ExploreViewHolder
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostYoutubeViewHolder
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.post.view.viewmodel.EmptyKolPostViewModel
import com.tokopedia.kol.feature.post.view.viewmodel.EntryPointViewModel
import com.tokopedia.kol.feature.post.view.viewmodel.ExploreViewModel
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostYoutubeViewModel
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by nisie on 5/15/17.
 */

class FeedPlusTypeFactoryImpl(context: FeedPlusFragment,
                              private val userSession: UserSessionInterface) :
        BaseAdapterTypeFactory(), FeedPlusTypeFactory, KolPostTypeFactory, DynamicFeedTypeFactory {

    private val viewListener: FeedPlus.View
    private val kolPostListener: KolPostListener.View.ViewHolder
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

    init {
        this.viewListener = context
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
    }

    override fun type(emptyModel: EmptyModel): Int {
        return EmptyFeedViewHolder.LAYOUT
    }

    override fun type(kolPostViewModel: KolPostViewModel): Int {
        return KolPostViewHolder.LAYOUT
    }

    override fun type(kolPostYoutubeViewModel: KolPostYoutubeViewModel): Int {
        return KolPostYoutubeViewHolder.LAYOUT
    }

    override fun type(emptyKolPostViewModel: EmptyKolPostViewModel): Int {
        return EmptyKolPostViewHolder.LAYOUT
    }

    override fun type(exploreViewModel: ExploreViewModel): Int {
        return ExploreViewHolder.LAYOUT
    }

    override fun type(entryPointViewModel: EntryPointViewModel): Int {
        throw IllegalStateException(this.javaClass.simpleName + " doesn't support "
                + EntryPointViewModel::class.java.simpleName)
    }

    override fun type(whitelistViewModel: WhitelistViewModel): Int {
        return WhitelistViewHolder.LAYOUT
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

    override fun type(topadsShopViewModel: TopadsShopViewModel): Int {
        return TopadsShopViewHolder.LAYOUT
    }

    override fun type(highlightViewModel: HighlightViewModel): Int {
        return HighlightViewHolder.LAYOUT
    }

    override fun type(onboardingViewModel: OnboardingViewModel): Int {
        return OnboardingViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {

        val viewHolder: AbstractViewHolder<*>

        if (type == EmptyFeedViewHolder.LAYOUT)
            viewHolder = EmptyFeedViewHolder(view, viewListener)
        else if (type == RetryViewHolder.LAYOUT)
            viewHolder = RetryViewHolder(view, viewListener)
        else if (type == EmptyFeedBeforeLoginViewHolder.LAYOUT)
            viewHolder = EmptyFeedBeforeLoginViewHolder(view, viewListener)
        else if (type == KolPostViewHolder.LAYOUT)
            viewHolder = KolPostViewHolder(view, kolPostListener, KolPostViewHolder.Type.FEED)
        else if (type == KolPostYoutubeViewHolder.LAYOUT)
            viewHolder = KolPostYoutubeViewHolder(view, kolPostListener, KolPostYoutubeViewHolder.Type.FEED)
        else if (type == EmptyKolPostViewHolder.LAYOUT)
            viewHolder = EmptyKolPostViewHolder(view)
        else if (type == ExploreViewHolder.LAYOUT)
            viewHolder = ExploreViewHolder(view, kolPostListener)
        else if (type == WhitelistViewHolder.LAYOUT)
            viewHolder = WhitelistViewHolder(view, viewListener)
        else if (type == DynamicPostViewHolder.LAYOUT) {
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
        } else if (type == FeedRecommendationViewHolder.LAYOUT) {
            viewHolder = FeedRecommendationViewHolder(view, recommendationCardListener, cardTitleListener)
        } else if (type == BannerViewHolder.LAYOUT) {
            viewHolder = BannerViewHolder(view, bannerListener, cardTitleListener)
        } else if (type == TopadsShopViewHolder.LAYOUT) {
            viewHolder = TopadsShopViewHolder(view, topadsShopListener, cardTitleListener)
        } else if (type == OnboardingViewHolder.LAYOUT){
            viewHolder = OnboardingViewHolder(view)
        } else
            viewHolder = super.createViewHolder(view, type)
        return viewHolder
    }

    override fun setType(type: KolPostViewHolder.Type) {

    }
}

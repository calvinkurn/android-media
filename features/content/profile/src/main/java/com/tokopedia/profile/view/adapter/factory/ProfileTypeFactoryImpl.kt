package com.tokopedia.profile.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.HideViewHolder
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
import com.tokopedia.kol.feature.post.view.adapter.typefactory.KolPostTypeFactory
import com.tokopedia.kol.feature.post.view.adapter.viewholder.EmptyKolPostViewHolder
import com.tokopedia.kol.feature.post.view.adapter.viewholder.ExploreViewHolder
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostViewHolder
import com.tokopedia.kol.feature.post.view.adapter.viewholder.KolPostYoutubeViewHolder
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.post.view.viewmodel.*
import com.tokopedia.profile.view.adapter.viewholder.*
import com.tokopedia.profile.view.listener.ProfileEmptyContract
import com.tokopedia.profile.view.viewmodel.*
import com.tokopedia.user.session.UserSessionInterface

/**
 * @author by milhamj on 9/20/18.
 */
class ProfileTypeFactoryImpl(private val viewListener : ProfileEmptyContract.View,
                             private val kolPostViewListener : KolPostListener.View.ViewHolder,
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
                             private val onEmptyItemClickedListener: EmptyAffiliateViewHolder.OnEmptyItemClickedListener,
                             private val onOtherProfilePostItemClick: ((applink: String, authorId:String) -> Unit),
                             private val userSession : UserSessionInterface)

    : BaseAdapterTypeFactory(), ProfileTypeFactory, KolPostTypeFactory, DynamicFeedTypeFactory {

    override fun type(viewModel: ProfileHeaderViewModel): Int {
        return ProfileHeaderViewHolder.LAYOUT
    }

    override fun type(viewModel: ProfileEmptyViewModel): Int {
        return ProfileEmptyViewHolder.LAYOUT
    }

    override fun type(kolPostViewModel: KolPostViewModel): Int {
        return KolPostViewHolder.LAYOUT
    }

    override fun type(emptyKolPostViewModel: EmptyKolPostViewModel): Int {
        return EmptyKolPostViewHolder.LAYOUT
    }

    override fun type(kolPostYoutubeViewModel: KolPostYoutubeViewModel): Int {
        return KolPostYoutubeViewHolder.LAYOUT
    }

    override fun type(exploreViewModel: ExploreViewModel): Int {
        return ExploreViewHolder.LAYOUT
    }

    override fun type(entryPointViewModel: EntryPointViewModel?): Int {
        return 0
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

    override fun type(emptyAffiliateViewModel: EmptyAffiliateViewModel): Int {
        return EmptyAffiliateViewHolder.LAYOUT
    }

    override fun setType(type: KolPostViewHolder.Type?) {
    }

    override fun type(noPostCardViewModel: NoPostCardViewModel): Int {
        return NoPostCardViewHolder.LAYOUT
    }

    override fun type(otherRelatedProfileViewModel: OtherRelatedProfileViewModel): Int {
        return OtherRelatedProfileViewHolder.LAYOUT
    }

    override fun type(titleViewModel: TitleViewModel): Int {
        return OtherPostTitleViewHolder.LAYOUT
    }

    @Suppress("UNCHECKED_CAST")
    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<Visitable<*>> {
        return when(type) {
            ProfileHeaderViewHolder.LAYOUT ->
                ProfileHeaderViewHolder(parent, viewListener) as AbstractViewHolder<Visitable<*>>
            ProfileEmptyViewHolder.LAYOUT ->
                ProfileEmptyViewHolder(parent) as AbstractViewHolder<Visitable<*>>
            KolPostViewHolder.LAYOUT ->
                    KolPostViewHolder(parent,
                            kolPostViewListener,
                            KolPostViewHolder.Type.PROFILE
                    ) as AbstractViewHolder<Visitable<*>>
            KolPostYoutubeViewHolder.LAYOUT ->
                KolPostYoutubeViewHolder(parent,
                        kolPostViewListener,
                        KolPostYoutubeViewHolder.Type.PROFILE
                ) as AbstractViewHolder<Visitable<*>>
            EmptyKolPostViewHolder.LAYOUT ->
                EmptyKolPostViewHolder(parent) as AbstractViewHolder<Visitable<*>>
            ExploreViewHolder.LAYOUT ->
                ExploreViewHolder(parent, kolPostViewListener) as AbstractViewHolder<Visitable<*>>
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
            EmptyAffiliateViewHolder.LAYOUT ->
                EmptyAffiliateViewHolder(parent, onEmptyItemClickedListener) as AbstractViewHolder< Visitable<*>>
            NoPostCardViewHolder.LAYOUT ->
                NoPostCardViewHolder(parent) as AbstractViewHolder<Visitable<*>>
            OtherRelatedProfileViewHolder.LAYOUT ->
                OtherRelatedProfileViewHolder(parent, onOtherProfilePostItemClick) as AbstractViewHolder<Visitable<*>>
            OtherPostTitleViewHolder.LAYOUT ->
                OtherPostTitleViewHolder(parent) as AbstractViewHolder<Visitable<*>>
            else -> super.createViewHolder(parent, type)
        }
    }
}
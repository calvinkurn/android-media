package com.tokopedia.home.beranda.presentation.view.adapter.factory

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.HomeFeedsListener
import com.tokopedia.home.beranda.listener.HomeInspirationListener
import com.tokopedia.home.beranda.listener.HomeReviewListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeolocationPromptViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.RetryModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.*
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.default_home_dc.ErrorPromptViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.mix_top.MixTopBannerViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business.NewBusinessViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.pdpview.listener.FlashSaleCardListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.EmptyBlankViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.GeolocationPromptViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.OvoViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationFeedViewHolder
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedViewModel
import java.util.*

/**
 * @author by errysuprayogi on 11/28/17.
 */

class HomeAdapterFactory(private val fragmentManager: FragmentManager, private val listener: HomeCategoryListener,
                         private val inspirationListener: HomeInspirationListener,
                         private val homeFeedsListener: HomeFeedsListener,
                         private val countDownListener: CountDownView.CountDownListener,
                         private val homeReviewListener: HomeReviewListener,
                         private val parentRecycledViewPool: RecyclerView.RecycledViewPool,
                         private val popularKeywordListener: PopularKeywordViewHolder.PopularKeywordListener) : BaseAdapterTypeFactory(), HomeTypeFactory {

    private val productLayout = HashSet(
            listOf(
                    DynamicHomeChannel.Channels.LAYOUT_3_IMAGE,
                    DynamicHomeChannel.Channels.LAYOUT_SPRINT,
                    DynamicHomeChannel.Channels.LAYOUT_ORGANIC)
    )

    private val bannerLayout = HashSet(
            listOf(
                    DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL,
                    DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC)
    )

    override fun type(inspirationHeaderViewModel: InspirationHeaderViewModel): Int {
        return InspirationHeaderViewHolder.LAYOUT
    }

    override fun type(bannerViewModel: BannerViewModel): Int {
        return BannerViewHolder.LAYOUT
    }

    override fun type(tickerViewModel: TickerViewModel): Int {
        return TickerViewHolder.LAYOUT
    }

    override fun type(businessUnitWidgetDataModel: NewBusinessUnitWidgetDataModel): Int {
        return NewBusinessViewHolder.LAYOUT
    }

    override fun type(useCaseIconSectionViewModel: UseCaseIconSectionViewModel): Int {
        return UseCaseIconSectionViewHolder.LAYOUT
    }

    override fun type(dynamicIconSectionViewModel: DynamicIconSectionViewModel): Int {
        return if(dynamicIconSectionViewModel.dynamicIconWrap) DynamicIconTwoRowsSectionViewHolder.LAYOUT else DynamicIconSectionViewHolder.LAYOUT
    }

    override fun type(topAdsDynamicChannelModel: TopAdsDynamicChannelModel): Int {
        return TopAdsDynamicChannelViewHolder.LAYOUT
    }

    override fun type(sellViewModel: SellViewModel): Int {
        return SellViewHolder.LAYOUT
    }

    override fun type(headerViewModel: HeaderViewModel): Int {
        return if (headerViewModel.isUserLogin)
            OvoViewHolder.LAYOUT
        else OvoViewHolder.NON_LOGIN_LAYOUT
    }

    override fun type(homeRecommendationFeedViewModel: HomeRecommendationFeedViewModel): Int {
        return HomeRecommendationFeedViewHolder.LAYOUT
    }

    override fun type(topAdsViewModel: TopAdsViewModel): Int {
        return TopAdsViewHolder.LAYOUT
    }

    override fun type(geolocationPromptViewModel: GeolocationPromptViewModel): Int {
        return GeolocationPromptViewHolder.LAYOUT
    }

    override fun type(dynamicChannelViewModel: DynamicChannelViewModel): Int {
        val layout = dynamicChannelViewModel.channel?.layout?:""
        return getDynamicChannelLayoutFromType(layout)
    }

    override fun type(spotlightViewModel: SpotlightViewModel): Int {
        return SpotlightViewHolder.LAYOUT
    }

    fun type(retryModel: RetryModel): Int {
        return RetryViewHolder.LAYOUT
    }

    override fun type(reviewViewModel: ReviewViewModel): Int {
        return ReviewViewHolder.LAYOUT
    }


    override fun type(playCard: PlayCardViewModel): Int {
        return PlayCardViewHolder.LAYOUT
    }

    override fun type(homeLoadingMoreModel: HomeLoadingMoreModel): Int {
        return HomeLoadingMoreViewHolder.LAYOUT
    }

    override fun type(homeRetryModel: HomeRetryModel): Int {
        return RetryViewHolder.LAYOUT
    }

    override fun type(popularKeywordListViewModel: PopularKeywordListViewModel): Int {
        return PopularKeywordViewHolder.LAYOUT
    }

    private fun getDynamicChannelLayoutFromType(layout: String): Int {
        /**
         * Layout registered as sprint sale viewholder
         * refer to item layout {@link com.tokopedia.home.R.layout#layout_sprint_product_item}
         */
        if (productLayout.contains(layout)) {
            return ProductOrganicChannelViewHolder.LAYOUT
        }
        else if(DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO.contains(layout)){
            /**
             * Layout registered as sprint sale
             * refer to dynamic channel sprint layout {@link com.tokopedia.home.R.layout#layout_sprint_product_item
             */
            return DynamicChannelSprintViewHolder.LAYOUT
        } else if (bannerLayout.contains(layout)) {
            /**
             * Layout registered as sprint sale viewholder
             * refer to banner organic layout {@link com.tokopedia.home.R.layout#home_banner_item}
             * refer to banner organic carousel layout {@link com.tokopedia.home.R.layout#home_banner_item_carousel}
             */
            return BannerOrganicViewHolder.LAYOUT
        }

        return when (layout) {
            /**
             * refer to 3 and 6 image item layout {@link com.tokopedia.home.R.layout#layout_lego_item}
             */
            DynamicHomeChannel.Channels.LAYOUT_6_IMAGE,
            DynamicHomeChannel.Channels.LAYOUT_LEGO_4_IMAGE,
            DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE -> DynamicLegoBannerViewHolder.LAYOUT

            /**
             * refer to 1 grid item layout {@link com.tokopedia.home.R.layout#home_dc_deals}
             * used by deals widget to show 1 product item
             */
            DynamicHomeChannel.Channels.LAYOUT_LEGO_1_IMAGE -> DealsViewHolder.LAYOUT

            /**
             * refer to sprint product item layout {@link com.tokopedia.home.R.layout#layout_sprint_product_item}
             * no further development for this viewholder
             * backend possibly return this layout for android version  >= 2.19
             */
            DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL -> SprintSaleCarouselViewHolder.LAYOUT

            /**
             * refer to gif banner layout com.tokopedia.home.R.layout#banner_image
             */
            DynamicHomeChannel.Channels.LAYOUT_BANNER_GIF -> BannerImageViewHolder.LAYOUT

            /**
             * refer to popular keyword layout com.tokopedia.home.R.layout#layout_popular_image
             */
            DynamicHomeChannel.Channels.LAYOUT_POPULAR_KEYWORD -> PopularKeywordViewHolder.LAYOUT

            /**
             * refer to mix left carousel com.tokopedia.home.R.layout#home_dc_mix_left
             */
            DynamicHomeChannel.Channels.LAYOUT_MIX_LEFT -> MixLeftViewHolder.LAYOUT

            /**
             * refer to gif banner layout com.tokopedia.home.R.layout#home_dc_default_error_prompt
             */
            DynamicHomeChannel.Channels.LAYOUT_DEFAULT_ERROR -> ErrorPromptViewHolder.LAYOUT

            /**
             * refer to recommendation list carousel com.tokopedia.home.R.layout#home_dc_list_carousel
             */
            DynamicHomeChannel.Channels.LAYOUT_LIST_CAROUSEL -> RecommendationListCarouselViewHolder.LAYOUT
            /**
             * refer to mix top carousel com.tokopedia.home.R.layout#home_mix_top_banner
             */
            DynamicHomeChannel.Channels.LAYOUT_MIX_TOP -> MixTopBannerViewHolder.LAYOUT
            else -> EmptyBlankViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        when (type) {
            DynamicChannelSprintViewHolder.LAYOUT -> viewHolder = DynamicChannelSprintViewHolder(view, listener, countDownListener, parentRecycledViewPool)
            ProductOrganicChannelViewHolder.LAYOUT -> viewHolder = ProductOrganicChannelViewHolder(view, listener, countDownListener, parentRecycledViewPool)
            DynamicLegoBannerViewHolder.LAYOUT -> viewHolder = DynamicLegoBannerViewHolder(view, listener, countDownListener, parentRecycledViewPool)
            BannerViewHolder.LAYOUT -> viewHolder = BannerViewHolder(view, listener)
            TickerViewHolder.LAYOUT -> viewHolder = TickerViewHolder(view, listener)
            NewBusinessViewHolder.LAYOUT -> viewHolder = NewBusinessViewHolder(view, listener)
            UseCaseIconSectionViewHolder.LAYOUT -> viewHolder = UseCaseIconSectionViewHolder(view, listener)
            DynamicIconSectionViewHolder.LAYOUT -> viewHolder = DynamicIconSectionViewHolder(view, listener)
            DynamicIconTwoRowsSectionViewHolder.LAYOUT -> viewHolder = DynamicIconTwoRowsSectionViewHolder(view, listener)
            SellViewHolder.LAYOUT -> viewHolder = SellViewHolder(view, listener)
            OvoViewHolder.LAYOUT, OvoViewHolder.NON_LOGIN_LAYOUT -> viewHolder = OvoViewHolder(view, listener)
            RetryViewHolder.LAYOUT -> viewHolder = RetryViewHolder(view, homeFeedsListener)
            TopAdsViewHolder.LAYOUT -> viewHolder = TopAdsViewHolder(view)
            TopAdsDynamicChannelViewHolder.LAYOUT -> viewHolder = TopAdsDynamicChannelViewHolder(view, inspirationListener)
            SprintSaleCarouselViewHolder.LAYOUT -> viewHolder = SprintSaleCarouselViewHolder(view, listener, countDownListener)
            SpotlightViewHolder.LAYOUT -> viewHolder = SpotlightViewHolder(view, listener)
            EmptyBlankViewHolder.LAYOUT -> viewHolder = EmptyBlankViewHolder(view)
            InspirationHeaderViewHolder.LAYOUT -> viewHolder = InspirationHeaderViewHolder(view)
            HomeRecommendationFeedViewHolder.LAYOUT -> viewHolder = HomeRecommendationFeedViewHolder(view, listener)
            GeolocationPromptViewHolder.LAYOUT -> viewHolder = GeolocationPromptViewHolder(view, listener)
            BannerOrganicViewHolder.LAYOUT -> viewHolder = BannerOrganicViewHolder(view, listener, countDownListener, parentRecycledViewPool)
            BannerImageViewHolder.LAYOUT -> viewHolder = BannerImageViewHolder(view, listener, countDownListener)
            ReviewViewHolder.LAYOUT -> viewHolder = ReviewViewHolder(view, homeReviewListener, listener)
            PlayCardViewHolder.LAYOUT -> viewHolder = PlayCardViewHolder(view, listener)
            HomeLoadingMoreViewHolder.LAYOUT -> viewHolder = HomeLoadingMoreViewHolder(view)
            ErrorPromptViewHolder.LAYOUT -> viewHolder = ErrorPromptViewHolder(view, listener, countDownListener)
            PopularKeywordViewHolder.LAYOUT -> viewHolder = PopularKeywordViewHolder(view, listener, popularKeywordListener)
            MixLeftViewHolder.LAYOUT -> viewHolder = MixLeftViewHolder(view, listener, countDownListener, parentRecycledViewPool)
            RecommendationListCarouselViewHolder.LAYOUT -> viewHolder = RecommendationListCarouselViewHolder(view, listener, countDownListener, parentRecycledViewPool)
            MixTopBannerViewHolder.LAYOUT -> viewHolder = MixTopBannerViewHolder(view, listener, countDownListener, parentRecycledViewPool)
            DealsViewHolder.LAYOUT -> viewHolder = DealsViewHolder(view, listener, countDownListener)
            else -> viewHolder = super.createViewHolder(view, type)
        }

        return viewHolder
    }
}

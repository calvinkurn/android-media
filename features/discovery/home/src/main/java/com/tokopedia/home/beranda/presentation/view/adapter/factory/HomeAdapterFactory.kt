package com.tokopedia.home.beranda.presentation.view.adapter.factory

import android.support.v4.app.FragmentManager
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.listener.HomeFeedsListener
import com.tokopedia.home.beranda.listener.HomeInspirationListener
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.*
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business.BusinessUnitViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.EmptyBlankViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.GeolocationPromptViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.HeaderViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationFeedViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HomeRecommendationFeedViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DigitalsViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicChannelViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BusinessUnitViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.BannerViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.DynamicIconSectionViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.GeolocationPromptViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.UseCaseIconSectionViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TopAdsDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.TickerViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SpotlightViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SellViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.SearchPlaceholderViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.InspirationHeaderViewModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.RetryModel

import java.util.Arrays
import java.util.HashSet

/**
 * @author by errysuprayogi on 11/28/17.
 */

class HomeAdapterFactory(private val fragmentManager: FragmentManager, private val listener: HomeCategoryListener,
                         private val inspirationListener: HomeInspirationListener,
                         private val homeFeedsListener: HomeFeedsListener,
                         private val countDownListener: CountDownView.CountDownListener) : BaseAdapterTypeFactory(), HomeTypeFactory {

    private val sprintLayout = HashSet(
            Arrays.asList(
                    DynamicHomeChannel.Channels.LAYOUT_3_IMAGE,
                    DynamicHomeChannel.Channels.LAYOUT_SPRINT,
                    DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO,
                    DynamicHomeChannel.Channels.LAYOUT_ORGANIC
            )
    )

    private val bannerLayout = HashSet(
            Arrays.asList(
                    DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL,
                    DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC
            )
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

    override fun type(searchPlaceholderViewModel: SearchPlaceholderViewModel): Int {
        return SearchPlaceholderViewModel.SEARCH_PLACE_HOLDER
    }

    override fun type(digitalsViewModel: DigitalsViewModel): Int {
        return DigitalsViewHolder.LAYOUT
    }

    override fun type(businessUnitViewModel: BusinessUnitViewModel): Int {
        return BusinessUnitViewHolder.LAYOUT
    }

    override fun type(useCaseIconSectionViewModel: UseCaseIconSectionViewModel): Int {
        return UseCaseIconSectionViewHolder.LAYOUT
    }

    override fun type(dynamicIconSectionViewModel: DynamicIconSectionViewModel): Int {
        return DynamicIconSectionViewHolder.LAYOUT
    }

    override fun type(topAdsDynamicChannelModel: TopAdsDynamicChannelModel): Int {
        return TopAdsDynamicChannelViewHolder.LAYOUT
    }

    override fun type(sellViewModel: SellViewModel): Int {
        return SellViewHolder.LAYOUT
    }

    override fun type(headerViewModel: HeaderViewModel): Int {
        return HeaderViewHolder.LAYOUT
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
        val layout = dynamicChannelViewModel.channel.layout
        return getDynamicChannelLayoutFromType(layout)
    }

    override fun type(spotlightViewModel: SpotlightViewModel): Int {
        return SpotlightViewHolder.LAYOUT
    }

    fun type(retryModel: RetryModel): Int {
        return RetryViewHolder.LAYOUT
    }

    private fun getDynamicChannelLayoutFromType(layout: String): Int {
        /**
         * Layout registered as sprint sale viewholder
         * refer to item layout {@link com.tokopedia.home.R.layout#layout_sprint_product_item}
         */
        if (sprintLayout.contains(layout)) {
            return DynamicChannelSprintViewHolder.LAYOUT_ITEM_SPRINT
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
             * refer to hero product item layout {@link com.tokopedia.home.R.layout#layout_hero_product_item}
             * no further development for this viewholder
             * backend already not supporting this layout
             */
            DynamicHomeChannel.Channels.LAYOUT_HERO -> DynamicChannelHeroViewHolder.LAYOUT

            /**
             * refer to 3 and 6 image item layout {@link com.tokopedia.home.R.layout#layout_lego_item}
             */
            DynamicHomeChannel.Channels.LAYOUT_6_IMAGE -> DynamicLegoBannerViewHolder.LAYOUT_ITEM_LEGO
            DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE -> DynamicLegoBannerViewHolder.LAYOUT_ITEM_LEGO

            /**
             * refer to sprint product item layout {@link com.tokopedia.home.R.layout#layout_sprint_product_item}
             * no further development for this viewholder
             * backend possibly return this layout for android version  >= 2.19
             */
            DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL -> SprintSaleCarouselViewHolder.LAYOUT
            else -> EmptyBlankViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        val viewHolder: AbstractViewHolder<*>
        when (type) {
            DynamicChannelSprintViewHolder.LAYOUT_ITEM_SPRINT -> viewHolder = DynamicChannelSprintViewHolder(view, listener, countDownListener)
            DynamicLegoBannerViewHolder.LAYOUT_ITEM_LEGO -> viewHolder = DynamicLegoBannerViewHolder(view, listener, countDownListener)
            BannerViewHolder.LAYOUT -> viewHolder = BannerViewHolder(view, listener)
            TickerViewHolder.LAYOUT -> viewHolder = TickerViewHolder(view, listener)
            DigitalsViewHolder.LAYOUT -> viewHolder = DigitalsViewHolder(listener, fragmentManager, view)
            BusinessUnitViewHolder.LAYOUT -> viewHolder = BusinessUnitViewHolder(fragmentManager, view)
            UseCaseIconSectionViewHolder.LAYOUT -> viewHolder = UseCaseIconSectionViewHolder(view, listener)
            DynamicIconSectionViewHolder.LAYOUT -> viewHolder = DynamicIconSectionViewHolder(view, listener)
            SellViewHolder.LAYOUT -> viewHolder = SellViewHolder(view, listener)
            HeaderViewHolder.LAYOUT -> viewHolder = HeaderViewHolder(view, listener)
            DynamicChannelHeroViewHolder.LAYOUT -> viewHolder = DynamicChannelHeroViewHolder(view, listener)
            RetryViewHolder.LAYOUT -> viewHolder = RetryViewHolder(view, homeFeedsListener)
            TopAdsViewHolder.LAYOUT -> viewHolder = TopAdsViewHolder(view)
            TopAdsDynamicChannelViewHolder.LAYOUT -> viewHolder = TopAdsDynamicChannelViewHolder(view, inspirationListener)
            SprintSaleCarouselViewHolder.LAYOUT -> viewHolder = SprintSaleCarouselViewHolder(view, listener, countDownListener)
            SpotlightViewHolder.LAYOUT -> viewHolder = SpotlightViewHolder(view, listener)
            EmptyBlankViewHolder.LAYOUT -> viewHolder = EmptyBlankViewHolder(view)
            InspirationHeaderViewHolder.LAYOUT -> viewHolder = InspirationHeaderViewHolder(view)
            HomeRecommendationFeedViewHolder.LAYOUT -> viewHolder = HomeRecommendationFeedViewHolder(view, listener)
            GeolocationPromptViewHolder.LAYOUT -> viewHolder = GeolocationPromptViewHolder(view, listener)
            BannerOrganicViewHolder.LAYOUT -> viewHolder = BannerOrganicViewHolder(view, listener)
            else -> viewHolder = super.createViewHolder(view, type)
        }

        return viewHolder
    }
}

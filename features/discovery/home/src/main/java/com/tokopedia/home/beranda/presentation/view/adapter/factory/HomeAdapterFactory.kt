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
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.inspiration.InspirationViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business.BusinessUnitViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.*
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationViewModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.RetryModel

/**
 * Created by Lukas on 2019-08-20
 */
class HomeAdapterFactory(private val fragmentManager: FragmentManager, private val listener: HomeCategoryListener,
                         private val inspirationListener: HomeInspirationListener,
                         private val homeFeedsListener: HomeFeedsListener,
                         private val countDownListener: CountDownView.CountDownListener) : BaseAdapterTypeFactory(), HomeTypeFactory {

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
        return if (headerViewModel.isUserLogin)
            OvoViewHolder.LAYOUT
        else OvoViewHolder.NON_LOGIN_LAYOUT
    }

    override fun type(inspirationViewModel: InspirationViewModel): Int {
        return InspirationViewHolder.LAYOUT
    }

    override fun type(homeRecommendationFeedViewModel: HomeRecommendationFeedViewModel): Int {
        return HomeRecommendationFeedViewHolder.LAYOUT
    }

    fun type(retryModel: RetryModel): Int {
        return RetryViewHolder.LAYOUT
    }

    override fun type(topAdsViewModel: TopAdsViewModel): Int {
        return TopAdsViewHolder.LAYOUT
    }

    override fun type(geolocationPromptViewModel: GeolocationPromptViewModel): Int {
        return GeolocationPromptViewHolder.LAYOUT
    }

    override fun type(dynamicChannelViewModel: DynamicChannelViewModel): Int {
        return if (DynamicHomeChannel.Channels.LAYOUT_3_IMAGE == dynamicChannelViewModel.channel.layout
                || DynamicHomeChannel.Channels.LAYOUT_SPRINT == dynamicChannelViewModel.channel.layout
                || DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO == dynamicChannelViewModel.channel.layout
                || DynamicHomeChannel.Channels.LAYOUT_ORGANIC == dynamicChannelViewModel.channel.layout) {
            DynamicChannelSprintViewHolder.LAYOUT
        } else if (DynamicHomeChannel.Channels.LAYOUT_HERO == dynamicChannelViewModel.channel.layout) {
            DynamicChannelHeroViewHolder.LAYOUT
        } else if (DynamicHomeChannel.Channels.LAYOUT_6_IMAGE == dynamicChannelViewModel.channel.layout) {
            SixGridChannelViewHolder.LAYOUT
        } else if (DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE == dynamicChannelViewModel.channel.layout) {
            ThreeGridChannelViewHolder.LAYOUT
        } else if (DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL == dynamicChannelViewModel.channel.layout) {
            SprintSaleCarouselViewHolder.LAYOUT
        } else {
            EmptyBlankViewHolder.LAYOUT
        }
    }

    override fun type(spotlightViewModel: SpotlightViewModel): Int {
        return SpotlightViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {

        return when (type) {
            BannerViewHolder.LAYOUT -> BannerViewHolder(view, listener)
            TickerViewHolder.LAYOUT -> TickerViewHolder(view, listener)
            DigitalsViewHolder.LAYOUT -> DigitalsViewHolder(listener, fragmentManager, view)
            BusinessUnitViewHolder.LAYOUT -> BusinessUnitViewHolder(fragmentManager, view)
            UseCaseIconSectionViewHolder.LAYOUT -> UseCaseIconSectionViewHolder(view, listener)
            DynamicIconSectionViewHolder.LAYOUT -> DynamicIconSectionViewHolder(view, listener)
            SellViewHolder.LAYOUT -> SellViewHolder(view, listener)
            OvoViewHolder.LAYOUT, OvoViewHolder.NON_LOGIN_LAYOUT -> OvoViewHolder(view, listener)
            InspirationViewHolder.LAYOUT -> InspirationViewHolder(view, inspirationListener)
            DynamicChannelHeroViewHolder.LAYOUT -> DynamicChannelHeroViewHolder(view, listener)
            DynamicChannelSprintViewHolder.LAYOUT -> DynamicChannelSprintViewHolder(view, listener, countDownListener)
            RetryViewHolder.LAYOUT -> RetryViewHolder(view, homeFeedsListener)
            TopAdsViewHolder.LAYOUT -> TopAdsViewHolder(view)
            TopAdsDynamicChannelViewHolder.LAYOUT -> TopAdsDynamicChannelViewHolder(view, inspirationListener)
            SprintSaleCarouselViewHolder.LAYOUT -> SprintSaleCarouselViewHolder(view, listener, countDownListener)
            SixGridChannelViewHolder.LAYOUT -> SixGridChannelViewHolder(view, listener, countDownListener)
            ThreeGridChannelViewHolder.LAYOUT -> ThreeGridChannelViewHolder(view, listener, countDownListener)
            SpotlightViewHolder.LAYOUT -> SpotlightViewHolder(view, listener)
            EmptyBlankViewHolder.LAYOUT -> EmptyBlankViewHolder(view)
            InspirationHeaderViewHolder.LAYOUT -> InspirationHeaderViewHolder(view)
            HomeRecommendationFeedViewHolder.LAYOUT -> HomeRecommendationFeedViewHolder(view, listener)
            GeolocationPromptViewHolder.LAYOUT -> GeolocationPromptViewHolder(view, listener)
            else -> super.createViewHolder(view, type)
        }
    }
}
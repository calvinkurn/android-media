package com.tokopedia.home.beranda.presentation.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeolocationPromptViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderViewModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedViewModel

/**
 * Created by Lukas on 2019-08-20
 */
interface HomeTypeFactory {

    fun type(inspirationHeaderViewModel: InspirationHeaderViewModel): Int

    fun type(bannerViewModel: BannerViewModel): Int

    fun type(tickerViewModel: TickerViewModel): Int

    fun type(searchPlaceholderViewModel: SearchPlaceholderViewModel): Int

    fun type(digitalViewModel: DigitalsViewModel): Int

    fun type(businessUnitViewModel: BusinessUnitViewModel): Int

    fun type(useCaseIconSectionViewModel: UseCaseIconSectionViewModel): Int

    fun type(dynamicIconSectionViewModel: DynamicIconSectionViewModel): Int

    fun type(sellViewModel: SellViewModel): Int

    fun type(headerViewModel: HeaderViewModel): Int

    fun type(topAdsViewModel: TopAdsViewModel): Int

    fun type(topAdsDynamicChannelModel: TopAdsDynamicChannelModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

    fun type(dynamicChannelViewModel: DynamicChannelViewModel): Int

    fun type(spotlightViewModel: SpotlightViewModel): Int

    fun type(homeRecommendationFeedViewModel: HomeRecommendationFeedViewModel): Int

    fun type(geolocationPromptViewModel: GeolocationPromptViewModel): Int

    fun type(reviewViewModel: ReviewViewModel): Int

    fun type(playCard: PlayCardViewModel): Int

    fun type(homeLoadingMoreModel: HomeLoadingMoreModel): Int

    fun type(homeRetryModel: HomeRetryModel): Int
}
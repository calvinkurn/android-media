package com.tokopedia.home.beranda.presentation.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.*
import com.tokopedia.home.beranda.presentation.view.viewmodel.InspirationViewModel

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

    fun type(inspirationViewModel: InspirationViewModel): Int

    fun type(dynamicChannelViewModel: DynamicChannelViewModel): Int

    fun type(spotlightViewModel: SpotlightViewModel): Int

    fun type(homeRecommendationFeedViewModel: HomeRecommendationFeedViewModel): Int

    fun type(geolocationPromptViewModel: GeolocationPromptViewModel): Int
}
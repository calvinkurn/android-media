package com.tokopedia.home.beranda.presentation.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.GeoLocationPromptDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.HeaderDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel

/**
 * Created by Lukas on 2019-08-20
 */
interface HomeTypeFactory {

    fun type(inspirationHeaderDataModel: InspirationHeaderDataModel): Int

    fun type(homepageBannerDataModel: HomepageBannerDataModel): Int

    fun type(tickerDataModel: TickerDataModel): Int

    fun type(businessUnitWidgetDataModel: NewBusinessUnitWidgetDataModel): Int

    fun type(useCaseIconSectionDataModel: UseCaseIconSectionDataModel): Int

    fun type(dynamicIconSectionDataModel: DynamicIconSectionDataModel): Int

    fun type(sellDataModel: SellDataModel): Int

    fun type(headerDataModel: HeaderDataModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

    fun type(dynamicChannelDataModel: DynamicChannelDataModel): Int

    fun type(spotlightDataModel: SpotlightDataModel): Int

    fun type(homeRecommendationFeedDataModel: HomeRecommendationFeedDataModel): Int

    fun type(geoLocationPromptDataModel: GeoLocationPromptDataModel): Int

    fun type(reviewDataModel: ReviewDataModel): Int

    fun type(playCard: PlayCardDataModel): Int

    fun type(playCard: PlayCarouselCardDataModel): Int

    fun type(homeLoadingMoreModel: HomeLoadingMoreModel): Int

    fun type(homeRetryModel: HomeRetryModel): Int

    fun type(popularKeywordListDataModel: PopularKeywordListDataModel): Int

    fun type(homeTopAdsBannerDataModel: HomeTopAdsBannerDataModel): Int
}
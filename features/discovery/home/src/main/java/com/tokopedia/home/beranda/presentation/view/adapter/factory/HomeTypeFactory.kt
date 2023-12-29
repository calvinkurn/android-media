package com.tokopedia.home.beranda.presentation.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.*
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.DynamicIconSectionDataModel
import com.tokopedia.home.beranda.presentation.view.uimodel.HomeInitialShimmerDataModel
import com.tokopedia.home.beranda.presentation.view.uimodel.HomeRecommendationFeedDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel

/**
 * Created by Lukas on 2019-08-20
 */
interface HomeTypeFactory {

    fun type(inspirationHeaderDataModel: InspirationHeaderDataModel): Int

    fun type(homepageBannerDataModel: HomepageBannerDataModel): Int

    fun type(tickerDataModel: TickerDataModel): Int

    fun type(bestSellerDataModel: BestSellerDataModel): Int

    fun type(businessUnitWidgetDataModel: NewBusinessUnitWidgetDataModel): Int

    fun type(useCaseIconSectionDataModel: UseCaseIconSectionDataModel): Int

    fun type(dynamicIconSectionDataModel: DynamicIconSectionDataModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

    fun type(homeRecommendationFeedDataModel: HomeRecommendationFeedDataModel): Int

    fun type(reviewDataModel: ReviewDataModel): Int

    fun type(homeLoadingMoreModel: HomeLoadingMoreModel): Int

    fun type(homeRetryModel: HomeRetryModel): Int

    fun type(popularKeywordListDataModel: PopularKeywordListDataModel): Int

    fun type(homeTopAdsBannerDataModel: HomeTopAdsBannerDataModel): Int

    fun type(homeTopAdsVerticalBannerDataModel: HomeTopAdsVerticalBannerDataModel): Int

    fun type(dynamicChannelLoadingModel: DynamicChannelLoadingModel): Int

    fun type(dynamicChannelErrorModel: DynamicChannelErrorModel): Int

    fun type(dynamicChannelRetryModel: DynamicChannelRetryModel): Int

    fun type(dataModel: CarouselPlayWidgetDataModel): Int

    fun type(emptyBannerDataModel: EmptyBannerDataModel): Int

    fun type(homeHeaderDataModel: HomeHeaderDataModel): Int

    fun type(homeInitialShimmerDataModel: HomeInitialShimmerDataModel): Int

    fun type(errorStateIconModel: ErrorStateIconModel): Int

    fun type(errorStateChannelOneModel: ErrorStateChannelOneModel): Int

    fun type(errorStateChannelTwoModel: ErrorStateChannelTwoModel): Int

    fun type(errorStateChannelThreeModel: ErrorStateChannelThreeModel): Int

    fun type(shimmeringChannelDataModel: ShimmeringChannelDataModel): Int

    fun type(shimmeringIconDataModel: ShimmeringIconDataModel): Int

    fun type(errorStateAtfModel: ErrorStateAtfModel): Int

    fun type(cmHomeWidgetDataModel: CMHomeWidgetDataModel): Int

    fun type(homePayLaterWidgetDataModel: HomePayLaterWidgetDataModel): Int
}

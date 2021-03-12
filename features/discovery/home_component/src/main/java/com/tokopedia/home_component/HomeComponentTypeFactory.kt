package com.tokopedia.home_component

import com.tokopedia.home_component.visitable.*

/**
 * Created by Devara on 2020-04-28
 */
interface HomeComponentTypeFactory {
    fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int
    fun type(dynamicLegoBannerSixAutoDataModel: DynamicLegoBannerSixAutoDataModel): Int
    fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int
    fun type(reminderWidgetModel: ReminderWidgetModel): Int
    fun type(mixLeftDataModel: MixLeftDataModel): Int
    fun type(mixTopDataModel: MixTopDataModel): Int
    fun type(productHighlightDataModel: ProductHighlightDataModel): Int
    fun type(lego4AutoDataModel: Lego4AutoDataModel): Int
    fun type(featuredShopDataModel: FeaturedShopDataModel): Int
    fun type(categoryNavigationDataModel: CategoryNavigationDataModel): Int
    fun type(bannerDataModel: BannerDataModel): Int
    fun type(dynamicIconComponentDataModel: DynamicIconComponentDataModel): Int
}
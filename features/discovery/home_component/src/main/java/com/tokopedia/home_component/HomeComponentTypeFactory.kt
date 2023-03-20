package com.tokopedia.home_component

import com.tokopedia.home_component.visitable.*

/**
 * Created by Devara on 2020-04-28
 */
interface HomeComponentTypeFactory {
    fun type(categoryWidgetV2DataModel: CategoryWidgetV2DataModel): Int = 0
    fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int
    fun type(dynamicLegoBannerSixAutoDataModel: DynamicLegoBannerSixAutoDataModel): Int
    fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int
    fun type(reminderWidgetModel: ReminderWidgetModel): Int
    fun type(mixLeftDataModel: MixLeftDataModel): Int
    fun type(mixLeftPaddingDataModel: MixLeftPaddingDataModel): Int = 0
    fun type(mixTopDataModel: MixTopDataModel): Int
    fun type(productHighlightDataModel: ProductHighlightDataModel): Int
    fun type(lego4AutoDataModel: Lego4AutoDataModel): Int
    fun type(featuredShopDataModel: FeaturedShopDataModel): Int
    fun type(categoryNavigationDataModel: CategoryNavigationDataModel): Int
    fun type(bannerDataModel: BannerDataModel): Int
    fun type(dynamicIconComponentDataModel: DynamicIconComponentDataModel): Int
    fun type(featuredBrandDataModel: FeaturedBrandDataModel): Int
    fun type(questWidgetModel: QuestWidgetModel): Int
    fun type(campaignWidgetDataModel: CampaignWidgetDataModel): Int = 0
    fun type(merchantVoucherDataModel: MerchantVoucherDataModel): Int = 0
    fun type(specialReleaseDataModel: SpecialReleaseDataModel): Int = 0
    fun type(cueCategoryDataModel: CueCategoryDataModel): Int = 0
    fun type(vpsDataModel: VpsDataModel): Int = 0
    fun type(missionWidgetListDataModel: MissionWidgetListDataModel): Int = 0
    fun type(lego4ProductDataModel: Lego4ProductDataModel): Int = 0
    fun type(bannerRevampDataModel: BannerRevampDataModel): Int = 0
}

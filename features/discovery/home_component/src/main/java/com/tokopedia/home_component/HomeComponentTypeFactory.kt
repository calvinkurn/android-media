package com.tokopedia.home_component

import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.BannerRevampDataModel
import com.tokopedia.home_component.visitable.BestSellerDataModel
import com.tokopedia.home_component.visitable.CampaignWidgetDataModel
import com.tokopedia.home_component.visitable.CategoryWidgetDataModel
import com.tokopedia.home_component.visitable.CategoryWidgetV2DataModel
import com.tokopedia.home_component.visitable.CueCategoryDataModel
import com.tokopedia.home_component.visitable.DealsDataModel
import com.tokopedia.home_component.visitable.DynamicIconComponentDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerSixAutoDataModel
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.home_component.visitable.FlashSaleDataModel
import com.tokopedia.home_component.visitable.Lego4ProductDataModel
import com.tokopedia.home_component.visitable.MerchantVoucherDataModel
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.MixLeftPaddingDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.home_component.visitable.ProductHighlightDataModel
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import com.tokopedia.home_component.visitable.SpecialReleaseDataModel
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel
import com.tokopedia.home_component.visitable.VpsDataModel

/**
 * Created by Devara on 2020-04-28
 */
interface HomeComponentTypeFactory {
    fun type(categoryWidgetDataModel: CategoryWidgetDataModel): Int = 0
    fun type(categoryWidgetV2DataModel: CategoryWidgetV2DataModel): Int = 0
    fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int = 0
    fun type(dynamicLegoBannerSixAutoDataModel: DynamicLegoBannerSixAutoDataModel): Int = 0
    fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int = 0
    fun type(reminderWidgetModel: ReminderWidgetModel): Int = 0
    fun type(mixLeftDataModel: MixLeftDataModel): Int = 0
    fun type(mixLeftPaddingDataModel: MixLeftPaddingDataModel): Int = 0
    fun type(mixTopDataModel: MixTopDataModel): Int = 0
    fun type(productHighlightDataModel: ProductHighlightDataModel): Int = 0
    fun type(featuredShopDataModel: FeaturedShopDataModel): Int = 0
    fun type(bannerDataModel: BannerDataModel): Int = 0
    fun type(dynamicIconComponentDataModel: DynamicIconComponentDataModel): Int = 0
    fun type(campaignWidgetDataModel: CampaignWidgetDataModel): Int = 0
    fun type(merchantVoucherDataModel: MerchantVoucherDataModel): Int = 0
    fun type(specialReleaseDataModel: SpecialReleaseDataModel): Int = 0
    fun type(cueCategoryDataModel: CueCategoryDataModel): Int = 0
    fun type(vpsDataModel: VpsDataModel): Int = 0
    fun type(missionWidgetListDataModel: MissionWidgetListDataModel): Int = 0
    fun type(lego4ProductDataModel: Lego4ProductDataModel): Int = 0
    fun type(bannerRevampDataModel: BannerRevampDataModel): Int = 0
    fun type(todoWidgetListDataModel: TodoWidgetListDataModel): Int = 0
    fun type(dealsDataModel: DealsDataModel): Int = 0
    fun type(flashSaleDataModel: FlashSaleDataModel): Int = 0
    fun type(bestSellerDataModel: BestSellerDataModel): Int = 0
}

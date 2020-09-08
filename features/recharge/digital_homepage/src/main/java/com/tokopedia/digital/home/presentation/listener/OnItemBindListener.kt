package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.common_digital.common.presentation.model.RecommendationItemEntity
import com.tokopedia.digital.home.model.DigitalHomePageBannerModel
import com.tokopedia.digital.home.model.DigitalHomePageCategoryModel
import com.tokopedia.digital.home.model.DigitalHomePageSectionModel
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.ReminderWidgetListener

interface OnItemBindListener {
    // Old Subhomepage
    fun onCategoryItemClicked(element: DigitalHomePageCategoryModel.Submenu?, position: Int)
    fun onBannerItemClicked(element: DigitalHomePageBannerModel.Banner?, position: Int)
    fun onBannerAllItemClicked()
    fun onSectionItemClicked(element: DigitalHomePageSectionModel.Item, position: Int, sectionType: String)
    fun onRecommendationClicked(element: RecommendationItemEntity, position: Int)

    fun onBannerImpressionTrack(banner: DigitalHomePageBannerModel.Banner?, position: Int)
    fun onCategoryImpression(element: DigitalHomePageCategoryModel.Submenu?, position: Int)
    fun onSectionItemImpression(elements: List<DigitalHomePageSectionModel.Item>, sectionType: String)
    fun onRecommendationImpression(elements: List<RecommendationItemEntity>)

    // Dynamic Subhomepage
    fun loadRechargeSectionData(sectionID: Int)
    fun onRechargeSectionEmpty(sectionID: Int)

    fun onRechargeSectionItemClicked(element: RechargeHomepageSections.Item)
    fun onRechargeBannerAllItemClicked(section: RechargeHomepageSections.Section)
    fun onRechargeReminderWidgetClicked(sectionID: Int)
    fun onRechargeReminderWidgetClosed(sectionID: Int)
    fun onRechargeFavoriteAllItemClicked(section: RechargeHomepageSections.Section)
    fun onRechargeLegoBannerItemClicked(sectionID: Int, itemID: Int, itemPosition: Int)
    fun onRechargeProductBannerClosed(section: RechargeHomepageSections.Section)

    fun onRechargeSectionItemImpression(element: RechargeHomepageSections.Section)
    fun onRechargeBannerImpression(element: RechargeHomepageSections.Section)
    fun onRechargeReminderWidgetImpression(sectionID: Int)
    fun onRechargeLegoBannerImpression(sectionID: Int)
}
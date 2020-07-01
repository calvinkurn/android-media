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
    fun onCategoryItemClicked(element: DigitalHomePageCategoryModel.Submenu?, position: Int)
    fun onBannerItemClicked(element: DigitalHomePageBannerModel.Banner?, position: Int)
    fun onBannerAllItemClicked()
    fun onSectionItemClicked(element: DigitalHomePageSectionModel.Item, position: Int, sectionType: String)
    fun onRecommendationClicked(element: RecommendationItemEntity, position: Int)

    fun onBannerImpressionTrack(banner: DigitalHomePageBannerModel.Banner?, position: Int)
    fun onCategoryImpression(element: DigitalHomePageCategoryModel.Submenu?, position: Int)
    fun onSectionItemImpression(elements: List<DigitalHomePageSectionModel.Item>, sectionType: String)
    fun onRecommendationImpression(elements: List<RecommendationItemEntity>)

    fun onRechargeSectionItemClicked(element: RechargeHomepageSections.Item, position: Int, sectionType: String)
    fun onRechargeBannerAllItemClicked(section: RechargeHomepageSections.Section)
    fun onRechargeReminderWidgetClicked()
    fun onRechargeFavoriteAllItemClicked(section: RechargeHomepageSections.Section)
    fun onRechargeLegoBannerItemClicked(itemPosition: Int)
    fun onRechargeProductBannerClose(section: RechargeHomepageSections.Section)

    fun onRechargeSectionItemImpression(elements: List<RechargeHomepageSections.Item>, sectionType: String)
    fun onRechargeReminderWidgetImpression()
    fun onRechargeLegoBannerItemImpression()

    fun onRechargeSectionEmpty(position: Int)
}
package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.digital.home.model.RechargeHomepageSections

interface RechargeHomepageItemListener {
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
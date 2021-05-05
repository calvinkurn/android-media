package com.tokopedia.digital.home.presentation.listener

import com.tokopedia.digital.home.model.RechargeHomepageSections

interface RechargeHomepageItemListener {
    fun loadRechargeSectionData(sectionID: String)
    fun onRechargeSectionEmpty(sectionID: String)

    fun onRechargeSectionItemClicked(element: RechargeHomepageSections.Item)
    fun onRechargeBannerAllItemClicked(section: RechargeHomepageSections.Section)
    fun onRechargeReminderWidgetClicked(sectionID: String)
    fun onRechargeReminderWidgetClosed(sectionID: String, toggleTracking: Boolean)
    fun onRechargeFavoriteAllItemClicked(section: RechargeHomepageSections.Section)
    fun onRechargeLegoBannerItemClicked(sectionID: String, itemID: String, itemPosition: Int)
    fun onRechargeProductBannerClosed(section: RechargeHomepageSections.Section)

    fun onRechargeSectionItemImpression(element: RechargeHomepageSections.Section)
    fun onRechargeBannerImpression(element: RechargeHomepageSections.Section)
    fun onRechargeReminderWidgetImpression(sectionID: String)
    fun onRechargeLegoBannerImpression(sectionID: String)
}
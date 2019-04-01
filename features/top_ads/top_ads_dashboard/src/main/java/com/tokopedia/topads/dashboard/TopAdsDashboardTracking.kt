package com.tokopedia.topads.dashboard

import com.tokopedia.track.TrackApp

class TopAdsDashboardTracking(private val router: TopAdsDashboardRouter) {
    companion object {
        val TOP_ADS_SELLER_APP = "topAdsSellerApp"
        val TOP_ADS_PRODUCT = "TopAds - Product"
        val CLICK = "Click"

        val TOP_ADS_PRODUCT_SHOP = "TopAds - Product/Shop"
        val TOP_ADS_SHOP = "TopAds - Shop"

        val KEYWORD = "keyword"
        val PRODUCT = "Product"
        val GROUP = "Group"
        val ADD_BALANCE = "Add Balance"
        val DATE_CUSTOM = "Date Custom"
        val PERIOD_OPTION = "Date Period - "
        val STATISTIC_BAR = "Statistic Bar -"

        val OPEN_PUSH_NOTIFICATION = "openPushNotification"
        val PUSH_NOTIFICATION = "Push Notification"
        val OPEN = "Open"
    }

    fun eventTopAdsProductClickKeywordDashboard() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TOP_ADS_SELLER_APP, TOP_ADS_PRODUCT, CLICK, KEYWORD)
    }

    fun eventTopAdsProductClickProductDashboard() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TOP_ADS_SELLER_APP, TOP_ADS_PRODUCT, CLICK, PRODUCT)
    }

    fun eventTopAdsProductClickGroupDashboard() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TOP_ADS_SELLER_APP, TOP_ADS_PRODUCT, CLICK, GROUP)
    }

    fun eventTopAdsProductAddBalance() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TOP_ADS_SELLER_APP, TOP_ADS_PRODUCT_SHOP, CLICK, ADD_BALANCE)
    }

    fun eventTopAdsShopChooseDateCustom() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TOP_ADS_SELLER_APP, TOP_ADS_PRODUCT, CLICK, DATE_CUSTOM)
    }

    fun eventTopAdsShopDatePeriod(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TOP_ADS_SELLER_APP, TOP_ADS_PRODUCT, CLICK, PERIOD_OPTION + label)
    }

    fun eventTopAdsProductStatisticBar(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TOP_ADS_SELLER_APP, TOP_ADS_PRODUCT, CLICK, STATISTIC_BAR + label)
    }

    fun eventTopAdsShopStatisticBar(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TOP_ADS_SELLER_APP, TOP_ADS_SHOP, CLICK, STATISTIC_BAR + label)
    }

    fun eventOpenTopadsPushNotification(label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(OPEN_PUSH_NOTIFICATION, PUSH_NOTIFICATION, OPEN, label)
    }
}
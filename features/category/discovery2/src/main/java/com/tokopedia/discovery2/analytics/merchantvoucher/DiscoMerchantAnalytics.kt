package com.tokopedia.discovery2.analytics.merchantvoucher

import com.tokopedia.discovery2.analytics.BaseDiscoveryAnalytics
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.mvcwidget.FollowWidgetType
import com.tokopedia.mvcwidget.trackers.DefaultMvcTrackerImpl

class DiscoMerchantAnalytics(private val discoveryAnalytics: BaseDiscoveryAnalytics,private val componentsItem: ComponentsItem,
                             private val positionInPage:Int,private val couponName:String?):DefaultMvcTrackerImpl() {
    var shopID :String = ""
    override fun userClickEntryPoints(shopId: String,userId: String?, source: Int,isTokomember: Boolean) {
        shopID = shopId
        discoveryAnalytics.trackSingleMerchantVoucherClick(componentsItem,shopId,userId,positionInPage,couponName)
    }

    override fun closeMainBottomSheet(@FollowWidgetType widgetType: String, shopId: String, userId: String?, source: Int) {
        val shopType = if(widgetType == FollowWidgetType.DEFAULT) "" else widgetType
        discoveryAnalytics.trackMerchantCouponCloseBottomSheet(shopId,shopType)
    }

    override fun viewCoupons(@FollowWidgetType widgetType: String, shopId: String, userId: String?, source: Int) {
        val shopType = if(widgetType == FollowWidgetType.DEFAULT) "" else widgetType
        discoveryAnalytics.trackMerchantCouponDetailImpression(componentsItem,shopId,shopType,userId,positionInPage,couponName)
    }

    override fun userClickBottomSheetCTA(@FollowWidgetType widgetType: String, label: String, userId: String) {
        val shopType = if(widgetType == FollowWidgetType.DEFAULT) "" else widgetType
        discoveryAnalytics.trackMerchantCouponVisitShopCTA(shopID,shopType)
    }

    override fun clickJadiMemberButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, source: Int, buttonTitle:String?) {
        val shopType = if(widgetType == FollowWidgetType.DEFAULT) "" else widgetType
        discoveryAnalytics.trackMerchantCouponCTASection(shopId,shopType,buttonTitle?:"")
    }

    override fun clickMulaiBelanjaButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, source: Int, buttonTitle:String?) {
        val shopType = if(widgetType == FollowWidgetType.DEFAULT) "" else widgetType
        discoveryAnalytics.trackMerchantCouponCTASection(shopId,shopType,buttonTitle?:"")
    }

    override fun clickCekInfoButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, source: Int, buttonTitle:String?) {
        val shopType = if(widgetType == FollowWidgetType.DEFAULT) "" else widgetType
        discoveryAnalytics.trackMerchantCouponCTASection(shopId,shopType,buttonTitle?:"")
    }

    override fun clickCekInfoButtonClose(widgetType: String, shopId: String, userId: String?, source: Int, buttonTitle:String?) {
        val shopType = if(widgetType == FollowWidgetType.DEFAULT) "" else widgetType
        discoveryAnalytics.trackMerchantCouponCTASection(shopId,shopType,buttonTitle?:"")
    }

    override fun clickFollowButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, source: Int, buttonTitle:String?) {
        val shopType = if(widgetType == FollowWidgetType.DEFAULT) "" else widgetType
        discoveryAnalytics.trackMerchantCouponCTASection(shopId,shopType,buttonTitle?:"")
    }


}
package com.tokopedia.discovery2.analytics.merchantvoucher

import com.tokopedia.discovery2.analytics.BaseDiscoveryAnalytics
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.mvcwidget.FollowWidgetType
import com.tokopedia.mvcwidget.trackers.DefaultMvcTrackerImpl
import com.tokopedia.mvcwidget.trackers.Tracker.Event.CLICK_COUPON_TITLE
import com.tokopedia.mvcwidget.trackers.Tracker.Event.CLICK_PRODUCT_CARD
import com.tokopedia.mvcwidget.trackers.Tracker.Event.CLICK_SHOP_NAME

class DiscoMerchantAnalytics(private val discoveryAnalytics: BaseDiscoveryAnalytics,private val componentsItem: ComponentsItem,
                             private val positionInPage:Int,private val couponName:String?,private val componentPosition:Int = -1):DefaultMvcTrackerImpl() {
    var shopID :String = ""
    override fun userClickEntryPoints(shopId: String,userId: String?, source: Int,isTokomember: Boolean, productId: String) {
        shopID = shopId
        discoveryAnalytics.trackSingleMerchantVoucherClick(componentsItem,shopId,userId,positionInPage,couponName)
    }

    override fun closeMainBottomSheet(@FollowWidgetType widgetType: String, shopId: String, userId: String?, source: Int) {
        discoveryAnalytics.trackMerchantCouponCloseBottomSheet(shopId,getShopType(widgetType))
    }

    override fun viewCoupons(@FollowWidgetType widgetType: String, shopId: String, userId: String?, source: Int) {
        discoveryAnalytics.trackMerchantCouponDetailImpression(componentsItem,shopId,getShopType(widgetType),userId,positionInPage,couponName)
    }

    override fun userClickBottomSheetCTA(@FollowWidgetType widgetType: String, label: String, userId: String) {
        discoveryAnalytics.trackMerchantCouponVisitShopCTA(shopID,getShopType(widgetType))
    }

    override fun clickJadiMemberButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, source: Int, buttonTitle:String?) {
        discoveryAnalytics.trackMerchantCouponCTASection(shopId,getShopType(widgetType),buttonTitle?:"")
    }

    override fun clickMulaiBelanjaButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, source: Int, buttonTitle:String?) {
        discoveryAnalytics.trackMerchantCouponCTASection(shopId,getShopType(widgetType),buttonTitle?:"")
    }

    override fun clickCekInfoButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, source: Int, buttonTitle:String?) {
        discoveryAnalytics.trackMerchantCouponCTASection(shopId,getShopType(widgetType),buttonTitle?:"")
    }

    override fun clickCekInfoButtonClose(widgetType: String, shopId: String, userId: String?, source: Int, buttonTitle:String?) {
        discoveryAnalytics.trackMerchantCouponCTASection(shopId,getShopType(widgetType),buttonTitle?:"")
    }

    override fun clickFollowButton(@FollowWidgetType widgetType: String, shopId: String, userId: String?, source: Int, buttonTitle:String?) {
        discoveryAnalytics.trackMerchantCouponCTASection(shopId,getShopType(widgetType),buttonTitle?:"")
    }

    override fun mvcMultiShopCardClick(
        shopName: String,
        eventAction: String,
        source: Int,
        userId: String?,
        productPosition:Int,
        label: String
    ) {
        if (componentPosition >= 0)
            when (eventAction) {
                CLICK_SHOP_NAME ->
                    discoveryAnalytics.trackMerchantVoucherMultipleShopClicks(
                        componentsItem,
                        userId,
                        componentPosition
                    )
                CLICK_COUPON_TITLE ->
                    discoveryAnalytics.trackMerchantVoucherMultipleVoucherDetailClicks(
                        componentsItem,
                        userId,
                        componentPosition
                    )
                CLICK_PRODUCT_CARD -> {
                    if (productPosition >= 0)
                        discoveryAnalytics.trackMerchantVoucherMultipleVoucherProductClicks(
                            componentsItem,
                            userId,
                            componentPosition,
                            productPosition
                        )
                    else
                        discoveryAnalytics.trackMerchantVoucherMultipleVoucherDetailClicks(
                            componentsItem,
                            userId,
                            componentPosition
                        )
                }
            }

    }


    fun getShopType(@FollowWidgetType widgetType: String):String{
        return if(widgetType == FollowWidgetType.DEFAULT) "" else widgetType
    }

}
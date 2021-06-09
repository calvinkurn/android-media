package com.tokopedia.discovery2.analytics

import com.tokopedia.discovery2.data.AdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.quickcouponresponse.ClickCouponData
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue

open class BaseDiscoveryAnalytics(val pageType: String = DISCOVERY_DEFAULT_PAGE_TYPE,
                                  val pagePath: String = EMPTY_STRING,
                                  val pageIdentifier: String = EMPTY_STRING,
                                  val campaignCode: String = EMPTY_STRING,
                                  val sourceIdentifier: String = EMPTY_STRING,
                                  val trackingQueue: TrackingQueue) {
    protected fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    open fun trackBannerImpression(banners: List<DataItem>, componentPosition: Int) {}
    open fun trackBrandRecommendationImpression(items: List<ComponentsItem>, componentPosition: Int) {}
    open fun trackBannerClick(banner: DataItem, bannerPosition: Int) {}
    open fun trackCategoryNavigationImpression(componentsItems: ArrayList<ComponentsItem>) {}
    open fun trackPlayWidgetImpression(componentsItem : ComponentsItem, userID: String?, channelId: String, shopId: String, widgetPosition: Int, channelPositionInList: Int, isAutoPlay: Boolean) {}
    open fun trackPlayWidgetClick(componentsItem : ComponentsItem, userID: String?, channelId: String, destinationURL: String, shopId: String, widgetPosition: Int, channelPositionInList: Int, isAutoPlay: Boolean) {}
    open fun trackPlayWidgetBannerClick(componentsItem : ComponentsItem, userID: String?, widgetPosition: Int) {}
    open fun trackPlayWidgetLihatSemuaClick(componentsItem : ComponentsItem, userID: String?, widgetPosition: Int) {}
    open fun trackPlayWidgetOverLayClick(componentsItem : ComponentsItem, userID: String?, widgetPosition: Int, channelPositionInList: Int, destinationURL: String) {}
    open fun trackPlayWidgetOverLayImpression(componentsItem : ComponentsItem, userID: String?, widgetPosition: Int, channelPositionInList: Int, destinationURL: String) {}
    open fun trackPlayWidgetReminderClick(componentsItem : ComponentsItem, userID: String?, widgetPosition: Int, channelPositionInList: Int, channelId: String, isRemindMe: Boolean){}
    open fun trackCategoryNavigationClick(categoryItem: DataItem?, position: Int) {}
    open fun trackClickVideo(videoUrl: String, videoName: String, videoPlayedTime: String) {}
    open fun trackBackClick() {}
    open fun trackShareClick() {}
    open fun trackSearchClick() {}
    open fun trackGlobalNavBarClick(buttonName : String, userID : String?) {}
    open fun trackLihatSemuaClick(headerName: String?) {}
    open fun trackImpressionIconDynamicComponent(headerName: String, icons: List<DataItem>) {}
    open fun trackClickIconDynamicComponent(iconPosition: Int, icon: DataItem) {}
    open fun trackClickSeeAllBanner() {}
    open fun trackClickCustomTopChat() {}
    open fun trackClickChipsFilter(filterName: String) {}
    open fun trackClickQuickFilter(filterName: String, componentName: String?, value: String, isFilterSelected: Boolean) {}
    open fun trackClickDetailedFilter(componentName: String?) {}
    open fun trackClickApplyFilter(mapParameters: Map<String, String>) {}
    open fun trackTimerSprintSale() {}
    open fun viewProductsList(componentsItems: ComponentsItem, isLogin: Boolean) {}
    open fun clearProductViewIds(isRefresh : Boolean) {}
    open fun trackProductCardClick(componentsItems: ComponentsItem, isLogin: Boolean) {}
    open fun trackEventImpressionCoupon(componentsItems: ArrayList<ComponentsItem>) {}
    open fun trackClickClaimCoupon(couponName: String?, promoCode: String?) {}
    open fun trackEventClickCoupon(coupon: DataItem?, position: Int, isDouble: Boolean) {}
    open fun trackQuickCouponImpression(clickCouponData: ClickCouponData) {}
    open fun trackQuickCouponClick(clickCouponData: ClickCouponData) {}
    open fun trackQuickCouponApply(clickCouponData: ClickCouponData) {}
    open fun trackQuickCouponPhoneVerified() {}
    open fun trackQuickCouponPhoneVerifyCancel() {}
    open fun trackOpenScreen(screenName: String, additionalInfo: AdditionalInfo?, userLoggedIn: Boolean) {}
    open fun trackTabsClick(id: String, parentPosition: Int, dataItem: DataItem, tabPosition1: Int) {}
    open fun trackCarouselBannerImpression(banners: List<DataItem>) {}
    open fun trackCarouselBannerClick(banner: DataItem, bannerPosition: Int) {}
    open fun trackBannerCarouselLihat() {}
    open fun trackEventImpressionTopAdsShop(componentDataItem: ComponentsItem, cpmData: CpmData) {}
    open fun trackClickTopAdsShop(componentDataItem: ComponentsItem, cpmData: CpmData) {}
    open fun trackTopAdsProductImpression(componentDataItem: ComponentsItem, cpmData: CpmData, componentPosition: Int, productPosition: Int, userLoggedIn: Boolean) {}
    open fun trackClickTopAdsProducts(componentDataItem: ComponentsItem, cpmData: CpmData, componentPosition: Int, productPosition: Int, userLoggedIn: Boolean) {}
    open fun trackHeaderSeeAllClick(isLogin: Boolean, componentsItems: ComponentsItem) {}
    open fun trackImpressionNavigationChips(componentsItems: ArrayList<ComponentsItem>?) {}
    open fun trackClickNavigationChips(categoryItem: DataItem?, position: Int) {}
    open fun trackClickNavigationDropDown() {}
    open fun trackClickCloseNavigation() {}
    open fun trackClickExpandNavigationAccordion(categoryId: String?) {}
    open fun trackClickCollapseNavigationAccordion(categoryId: String?) {}
    open fun trackClickCategoryOption(categoryId: String?) {}
    open fun trackNotifyClick(componentsItems: ComponentsItem, isLogin: Boolean, userID: String?) {}
    open fun trackCategoryTreeDropDownClick(userLoggedIn: Boolean) {}
    open fun trackCategoryOptionClick(userLoggedIn: Boolean, childCatID: String, applink: String?, catDepth: Int, childCatName: String) {}
    open fun trackCategoryTreeCloseClick(userLoggedIn: Boolean) {}
    open fun trackBottomNavBarClick(buttonName: String, userID: String?) {}
    open fun getHostSource() : String { return ""}
    open fun getHostTrackingSource() : String {return ""}
    open fun getEventLabel() : String {return ""}

}
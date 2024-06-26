package com.tokopedia.discovery2.analytics

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery2.analytics.merchantvoucher.MvcTrackingProperties
import com.tokopedia.discovery2.data.AdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.ParamsForOpenScreen
import com.tokopedia.discovery2.data.producthighlight.DiscoveryOCSDataModel
import com.tokopedia.discovery2.data.quickcouponresponse.ClickCouponData
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.shop.common.widget.bundle.enum.BundleTypes
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue

open class BaseDiscoveryAnalytics(
    val pageType: String = DISCOVERY_DEFAULT_PAGE_TYPE,
    val pagePath: String = EMPTY_STRING,
    val pageIdentifier: String = EMPTY_STRING,
    val campaignCode: String = EMPTY_STRING,
    val sourceIdentifier: String = EMPTY_STRING,
    val trackingQueue: TrackingQueue
) {
    protected fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    open fun trackBannerImpression(banners: List<DataItem>, componentPosition: Int?, userID: String?) {}
    open fun trackBrandRecommendationImpression(items: List<ComponentsItem>, componentPosition: Int, componentID: String) {}
    open fun trackBrandRecommendationClick(banner: DataItem, bannerPosition: Int, compID: String) {}
    open fun trackBannerClick(banner: DataItem, bannerPosition: Int, userID: String?) {}
    open fun trackTDNBannerImpression(componentsItem: ComponentsItem, userID: String?, positionInPage: Int, adID: String, shopId: String, itemPosition: Int) {}
    open fun trackPlayWidgetImpression(
        dataItem: DataItem?,
        playModel: PlayWidgetChannelUiModel,
        userID: String?,
        widgetPosition: Int,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {}
    open fun trackPlayWidgetClick(
        dataItem: DataItem?,
        userID: String?,
        playModel: PlayWidgetChannelUiModel,
        widgetPosition: Int,
        channelPositionInList: Int,
        isAutoPlay: Boolean
    ) {}
    open fun trackPlayWidgetBannerClick(dataItem: DataItem?, userID: String?, widgetPosition: Int) {}
    open fun trackPlayWidgetLihatSemuaClick(dataItem: DataItem?, userID: String?, widgetPosition: Int) {}
    open fun trackPlayWidgetOverLayClick(componentsItem: ComponentsItem, userID: String?, widgetPosition: Int, channelPositionInList: Int, destinationURL: String) {}
    open fun trackPlayWidgetOverLayImpression(componentsItem: ComponentsItem, userID: String?, widgetPosition: Int, channelPositionInList: Int, destinationURL: String) {}
    open fun trackPlayWidgetReminderClick(
        playModel: PlayWidgetChannelUiModel,
        userID: String?,
        widgetPosition: Int,
        channelPositionInList: Int,
        isRemindMe: Boolean,
        isAutoPlay: Boolean
    ) {}
    open fun trackTDNBannerClick(componentsItem: ComponentsItem, userID: String?, positionInPage: Int, adID: String, shopId: String, itemPosition: Int) {}
    open fun trackClickVideo(videoUrl: String, videoName: String, videoPlayedTime: String) {}
    open fun trackBackClick() {}
    open fun trackShareClick() {}
    open fun trackSearchClick() {}
    open fun trackGlobalNavBarClick(buttonName: String, userID: String?) {}
    open fun trackLihatSemuaClick(dataItem: DataItem?) {}
    open fun trackPromoLihat(componentsItems: ComponentsItem) {}
    open fun trackClickSeeAllBanner() {}
    open fun trackClickCustomTopChat() {}
    open fun trackClickChipsFilter(filterName: String) {}
    open fun trackClickQuickFilter(filterName: String, componentName: String?, value: String, isFilterSelected: Boolean) {}
    open fun trackClickDetailedFilter(componentName: String?) {}
    open fun trackClickApplyFilter(mapParameters: Map<String, String>) {}
    open fun trackTimerSprintSale() {}
    open fun viewProductsList(
        componentsItems: ComponentsItem,
        isLogin: Boolean,
        isFulFillment: Boolean = false,
        warehouseId: Long = 0
    ) {}
    open fun clearProductViewIds(isRefresh: Boolean) {}
    open fun trackProductCardClick(
        componentsItems: ComponentsItem,
        isLogin: Boolean,
        isFulFillment: Boolean = false,
        warehouseId: Long = 0
    ) {}

    open fun trackQuickCouponImpression(clickCouponData: ClickCouponData) {}
    open fun trackQuickCouponClick(clickCouponData: ClickCouponData) {}
    open fun trackQuickCouponApply(clickCouponData: ClickCouponData) {}
    open fun trackQuickCouponPhoneVerified() {}
    open fun trackQuickCouponPhoneVerifyCancel() {}
    open fun trackOpenScreen(screenName: String, additionalInfo: AdditionalInfo?, userLoggedIn: Boolean, paramsForOpenScreen: ParamsForOpenScreen) {}
    open fun trackTabsClick(id: String, parentPosition: Int, dataItem: DataItem, tabPosition1: Int, eventAction: String = "") {}
    open fun trackUnifyTabsClick(id: String, parentPosition: Int, dataItem: DataItem, tabPosition1: Int, eventAction: String = "") {}
    open fun trackCarouselBannerImpression(banners: List<DataItem>, componentType: String) {}
    open fun trackCarouselBannerClick(banner: DataItem, bannerPosition: Int, componentType: String) {}
    open fun trackBannerCarouselLihat() {}
    open fun trackEventImpressionTopAdsShop(componentDataItem: ComponentsItem, cpmData: CpmData) {}
    open fun trackClickTopAdsShop(componentDataItem: ComponentsItem, cpmData: CpmData) {}
    open fun trackTopAdsProductImpression(componentDataItem: ComponentsItem, cpmData: CpmData, componentPosition: Int, productPosition: Int, userLoggedIn: Boolean) {}
    open fun trackClickTopAdsProducts(componentDataItem: ComponentsItem, cpmData: CpmData, componentPosition: Int, productPosition: Int, userLoggedIn: Boolean) {}
    open fun trackHeaderSeeAllClick(isLogin: Boolean, componentsItems: ComponentsItem) {}
    open fun trackSingleMerchantVoucherImpression(components: ComponentsItem, shopId: String, userID: String?, positionInPage: Int, couponName: String?) {}
    open fun trackSingleMerchantVoucherClick(components: ComponentsItem, shopId: String, userID: String?, positionInPage: Int, couponName: String?) {}
    open fun trackMerchantCouponDetailImpression(components: ComponentsItem, shopId: String, shopType: String, userID: String?, positionInPage: Int, couponName: String?) {}
    open fun trackMerchantCouponVisitShopCTA(shopId: String, shopType: String) {}
    open fun trackMerchantCouponCTASection(shopId: String, shopType: String, buttonDetail: String) {}
    open fun trackMerchantCouponCloseBottomSheet(shopId: String, shopType: String) {}
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
    open fun getHostSource(): String { return "" }
    open fun getHostTrackingSource(): String { return "" }
    open fun getEventLabel(): String { return "" }
    open fun onTopadsHeadlineImpression(cpmModel: CpmModel, adapterPosition: Int) {}
    open fun onTopAdsHeadlineAdsClick(position: Int, applink: String?, cpmData: CpmData, components: ComponentsItem, userLoggedIn: Boolean) {}
    open fun onTopAdsProductItemListener(position: Int, product: Product, cpmData: CpmData, components: ComponentsItem, userLoggedIn: Boolean) {}
    open fun trackScrollDepth(screenScrollPercentage: Int, lastVisibleComponent: ComponentsItem?) {}
    open fun trackUnifyShare(event: String = "", eventAction: String = "", userID: String?, eventLabel: String = "") {}
    open fun trackScrollDepth(screenScrollPercentage: Int, lastVisibleComponent: ComponentsItem?, isManualScroll: Boolean) {}
    open fun trackScreenshotAccess(eventAction: String = "", eventLabel: String = "", userID: String?) {}
    open fun trackEventProductATCTokonow(componentsItems: ComponentsItem, cartId: String) {}
    open fun trackEventProductATC(componentsItems: ComponentsItem, cartID: String) {}
    open fun trackEventProductBmGmATC(componentsItems: ComponentsItem, cartID: String) {}
    open fun trackEventProductBmGmClickSeeMore(componentsItems: ComponentsItem) {}
    open fun trackMerchantVoucherMultipleImpression(
        components: ComponentsItem,
        userID: String?,
        position: Int
    ) {
    }

    open fun trackMerchantVoucherViewClickAll(
        components: ComponentsItem,
        userID: String?,
        position: Int,
        ctaText: String?
    ) {
    }

    open fun trackMerchantVoucherMultipleShopClicks(
        components: ComponentsItem,
        userID: String?,
        position: Int
    ) {
    }

    open fun trackMerchantVoucherMultipleVoucherDetailClicks(
        components: ComponentsItem,
        userID: String?,
        position: Int
    ) {
    }
    open fun trackMerchantVoucherMultipleVoucherProductClicks(
        components: ComponentsItem,
        userID: String?,
        position: Int,
        productIndex: Int
    ) {
    }
    open fun trackMerchantVoucherLihatSemuaClick(dataItem: DataItem?) {}
    open fun setOldTabPageIdentifier(pageIdentifier: String) {}
    open fun viewCalendarsList(componentsItems: ComponentsItem, userID: String) {}
    open fun trackEventClickCalendarWidget(componentsItems: ComponentsItem, userID: String) {}
    open fun trackEventClickCalendarCTA(componentsItems: ComponentsItem, userID: String) {}
    open fun clickQuestLihatButton(source: Int) {}
    open fun viewQuestWidget(source: Int, id: String) {}
    open fun clickQuestCard(source: Int, id: String) {}
    open fun slideQuestCard(source: Int, direction: String) {}
    open fun trackAnchorTabClick(components: ComponentsItem) {}
    open fun viewAnchorTabs(componentsItems: ComponentsItem) {}
    open fun trackShopCardImpression(componentsItems: ComponentsItem) {}
    open fun trackEventClickShopCard(componentsItems: ComponentsItem) {}
    open fun trackMixLeftBannerImpression(componentsItems: ComponentsItem) {}
    open fun sendMixLeftBannerImpression(componentsItems: ComponentsItem) {}
    open fun trackMixLeftBannerClick(componentsItems: ComponentsItem) {}
    open fun track3DotsOptionsClickedWishlist(productCardOptionsModel: ProductCardOptionsModel) {}
    open fun track3DotsOptionsClickedLihatToko() {}
    open fun track3DotsOptionsClickedShareProduct() {}
    open fun trackShopBannerInfiniteImpression(componentsItems: ComponentsItem) {}
    open fun trackShopBannerInfiniteClick(componentsItems: ComponentsItem) {}
    open fun trackEventClickProductBundlingChipSelection(componentsItems: ComponentsItem, selectedProduct: BundleProductUiModel, selectedSingleBundle: BundleDetailUiModel) {}
    open fun trackEventBundleProductClicked(componentsItems: ComponentsItem, bundleType: BundleTypes, bundle: BundleUiModel, selectedMultipleBundle: BundleDetailUiModel, selectedProduct: BundleProductUiModel, productItemPosition: Int) {}
    open fun trackEventProductBundlingAtcClick(componentsItems: ComponentsItem, selectedMultipleBundle: BundleDetailUiModel) {}
    open fun trackEventProductBundlingViewImpression(componentsItems: ComponentsItem, selectedBundle: BundleDetailUiModel, bundlePosition: Int) {}
    open fun trackEventProductBundlingCarouselImpression(componentsItems: ComponentsItem, bundledProductList: List<BundleUiModel>, totalBundlings: Int, totalBundleSeenPosition: Int, lastVisibleItemPosition: Int) {}
    open fun trackContentCardImpression(componentsItems: ComponentsItem, userID: String?) {}
    open fun trackContentCardClick(componentsItems: ComponentsItem, userID: String?) {}
    open fun trackPromoProductHighlightImpression(productHighlightData: List<DataItem>, components: ComponentsItem?) {}
    open fun trackProductHighlightClick(productHighlightData: DataItem, productHighlightPosition: Int, components: ComponentsItem?, isLogin: Boolean) {}
    open fun trackProductHighlightOCSClick(ocsDataModel: DiscoveryOCSDataModel, parentPosition: Int?, parentComponentId: String?) {}
    open fun trackCouponImpression(properties: List<CouponTrackingProperties>) {}
    open fun trackCouponClickEvent(properties: CouponTrackingProperties) {}
    open fun trackCouponCTAClickEvent(properties: CouponTrackingProperties) {}
    open fun trackSupportingBrandImpression(components: List<ComponentsItem>) {}
    open fun trackSupportingBrandClick(component: ComponentsItem, actionType: String) {}
    open fun trackMvcImpression(properties: List<MvcTrackingProperties>){}
    open fun trackMvcClickEvent(properties: MvcTrackingProperties, isCta: Boolean) {}
}

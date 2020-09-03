package com.tokopedia.deals.common.analytics

import android.util.Log
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.deals.common.analytics.DealsAnalyticsConstants.PRODUCT_CARD
import com.tokopedia.deals.common.model.response.Brand
import com.tokopedia.deals.common.model.response.EventProductDetail
import com.tokopedia.deals.common.ui.dataview.CuratedProductCategoryDataView
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.deals.common.ui.dataview.DealsChipsDataView
import com.tokopedia.deals.common.ui.dataview.ProductCardDataView
import com.tokopedia.deals.home.ui.dataview.BannersDataView
import com.tokopedia.deals.home.ui.dataview.CuratedCategoryDataView
import com.tokopedia.deals.home.ui.dataview.VoucherPlaceCardDataView
import com.tokopedia.deals.home.ui.dataview.VoucherPlacePopularDataView
import com.tokopedia.deals.search.model.visitor.VoucherModel
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class DealsAnalytics @Inject constructor(
        private val irisSession: IrisSession,
        private val userSession: UserSessionInterface
) {

    private fun getIrisSessionId(): String = irisSession.getSessionId()
    private fun getUserId(): String = userSession.userId

    private fun getTrackingMapWithHeader(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[DealsAnalyticsConstants.SCREEN_NAME] = "/deals/"
        map[DealsAnalyticsConstants.CURRENT_SITE] = DealsAnalyticsConstants.TOKOPEDIA_DIGITAL_DEALS
        map[DealsAnalyticsConstants.CLIENT_ID] = TrackApp.getInstance().gtm.clientIDString ?: ""
        map[DealsAnalyticsConstants.SESSION_IRIS] = getIrisSessionId()
        map[DealsAnalyticsConstants.USER_ID] = getUserId()
        map[DealsAnalyticsConstants.BUSINESS_UNIT] = DealsAnalyticsConstants.TRAVELENTERTAINMENT_BU
        map[DealsAnalyticsConstants.CATEGORY_LABEL] = DealsAnalyticsConstants.DEALS
        return map
    }

    private fun getTrackingBrandSearchWithHeader(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[DealsAnalyticsConstants.SCREEN_NAME] = "/deals/brand/search"
        map[DealsAnalyticsConstants.CURRENT_SITE] = DealsAnalyticsConstants.TOKOPEDIA_DIGITAL_DEALS
        map[DealsAnalyticsConstants.CLIENT_ID] = TrackApp.getInstance().gtm.clientIDString ?: ""
        map[DealsAnalyticsConstants.SESSION_IRIS] = getIrisSessionId()
        map[DealsAnalyticsConstants.USER_ID] = getUserId()
        map[DealsAnalyticsConstants.BUSINESS_UNIT] = DealsAnalyticsConstants.TRAVELENTERTAINMENT_BU
        map[DealsAnalyticsConstants.CATEGORY_LABEL] = DealsAnalyticsConstants.DEALS
        return map
    }

    private fun getTrackingCategorySearchWithHeader(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[DealsAnalyticsConstants.SCREEN_NAME] = "/deals/category/search"
        map[DealsAnalyticsConstants.CURRENT_SITE] = DealsAnalyticsConstants.TOKOPEDIA_DIGITAL_DEALS
        map[DealsAnalyticsConstants.CLIENT_ID] = TrackApp.getInstance().gtm.clientIDString ?: ""
        map[DealsAnalyticsConstants.SESSION_IRIS] = getIrisSessionId()
        map[DealsAnalyticsConstants.USER_ID] = getUserId()
        map[DealsAnalyticsConstants.BUSINESS_UNIT] = DealsAnalyticsConstants.TRAVELENTERTAINMENT_BU
        map[DealsAnalyticsConstants.CATEGORY_LABEL] = DealsAnalyticsConstants.DEALS
        return map
    }

    private fun getTrackingSearchWithHeader(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[DealsAnalyticsConstants.SCREEN_NAME] = "/deals/search"
        map[DealsAnalyticsConstants.CURRENT_SITE] = DealsAnalyticsConstants.TOKOPEDIA_DIGITAL_DEALS
        map[DealsAnalyticsConstants.CLIENT_ID] = TrackApp.getInstance().gtm.clientIDString ?: ""
        map[DealsAnalyticsConstants.SESSION_IRIS] = getIrisSessionId()
        map[DealsAnalyticsConstants.USER_ID] = getUserId()
        map[DealsAnalyticsConstants.BUSINESS_UNIT] = DealsAnalyticsConstants.TRAVELENTERTAINMENT_BU
        map[DealsAnalyticsConstants.CATEGORY_LABEL] = DealsAnalyticsConstants.DEALS
        return map
    }

    private fun getTrackingOpenHomePageWithHeader(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[DealsAnalyticsConstants.SCREEN_NAME] = "/deals/homepage"
        map[DealsAnalyticsConstants.CURRENT_SITE] = DealsAnalyticsConstants.TOKOPEDIA_DIGITAL_DEALS
        map[DealsAnalyticsConstants.CLIENT_ID] = TrackApp.getInstance().gtm.clientIDString ?: ""
        map[DealsAnalyticsConstants.SESSION_IRIS] = getIrisSessionId()
        map[DealsAnalyticsConstants.USER_ID] = getUserId()
        map[DealsAnalyticsConstants.BUSINESS_UNIT] = DealsAnalyticsConstants.TRAVELENTERTAINMENT_BU
        return map
    }

    private fun MutableMap<String, Any>.addGeneralEvent(event: String, action: String, label: String): MutableMap<String, Any>? {
        this[TrackAppUtils.EVENT] = event
        this[TrackAppUtils.EVENT_CATEGORY] = DealsAnalyticsConstants.Category.DIGITAL_DEALS
        this[TrackAppUtils.EVENT_ACTION] = action
        this[TrackAppUtils.EVENT_LABEL] = label
        return this
    }

    fun eventClickChangeLocationSearchPage(oldLocation: String, newLocation: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CHANGE_LOCATION_SEARCH_PAGE,
                String.format(DealsAnalyticsConstants.Label.CHANGE_LOCATION, oldLocation, newLocation)
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventViewSearchResultSearchPage(keyword: String, location: String, items: List<VoucherModel>) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.VIEW_SEARCH_RESULT,
                DealsAnalyticsConstants.Action.VIEW_SEARCH_RESULT,
                String.format(DealsAnalyticsConstants.Label.VIEW_SEARCH_RESULT, keyword, location)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CURRENCY_CODE, DealsAnalyticsConstants.IDR,
                DealsAnalyticsConstants.IMPRESSIONS, getECommerceDataVoucherListSearchPage(items)
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceDataVoucherListSearchPage(items: List<VoucherModel>): MutableList<MutableMap<String, Any>> {
        val dataImpressions = mutableListOf<MutableMap<String, Any>>()
        items.forEach {
            val price = if (it.realPrice.isNotEmpty()) it.realPrice.toInt() else 0
            val impression = DataLayer.mapOf(
                    DealsAnalyticsConstants.Item.name, it.voucherName,
                    DealsAnalyticsConstants.Item.id, it.voucherId,
                    DealsAnalyticsConstants.Item.price, price,
                    DealsAnalyticsConstants.Item.brand, it.merchantName,
                    DealsAnalyticsConstants.Item.category, DealsAnalyticsConstants.NONE,
                    DealsAnalyticsConstants.Item.variant, DealsAnalyticsConstants.NONE,
                    DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.SEARCH_RESULT_LIST,
                    DealsAnalyticsConstants.Item.position, it.position + 1
            )
            dataImpressions.add(impression)
        }
        return dataImpressions
    }

    fun eventViewSearchNoResultSearchPage(keyword: String, location: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.VIEW_DEALS_IRIS,
                DealsAnalyticsConstants.Action.VIEW_SEARCH_RESULT,
                String.format(DealsAnalyticsConstants.Label.VIEW_SEARCH_RESULT_NOT_FOUND, keyword, location)
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickSearchResultProductSearchPage(item: VoucherModel) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PRODUCT_CLICK,
                DealsAnalyticsConstants.Action.SEARCH_RESULT_CLICK,
                String.format(DealsAnalyticsConstants.Label.SEARCH_RESULT_CLICK, item.voucherName, item.merchantName, item.position + 1)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CLICK, DataLayer.mapOf(DealsAnalyticsConstants.ACTION_FIELD,
                    DataLayer.mapOf(DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.SEARCH_RESULT_LIST),
                DealsAnalyticsConstants.PRODUCTS, getECommerceDataVoucherSearchPage(item)
        )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceDataVoucherSearchPage(item: VoucherModel): MutableList<MutableMap<String, Any>> {
        val price = if (item.realPrice.isNotEmpty()) item.realPrice.toInt() else 0

        val data = mutableListOf<MutableMap<String, Any>>()
        data.add(DataLayer.mapOf(
                DealsAnalyticsConstants.Item.name, item.voucherName,
                DealsAnalyticsConstants.Item.id, item.voucherId,
                DealsAnalyticsConstants.Item.price, price,
                DealsAnalyticsConstants.Item.brand, item.merchantName,
                DealsAnalyticsConstants.Item.category, DealsAnalyticsConstants.NONE,
                DealsAnalyticsConstants.Item.variant, DealsAnalyticsConstants.NONE,
                DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.SEARCH_RESULT_LIST,
                DealsAnalyticsConstants.Item.position, item.position + 1)
        )
        return data
    }

    fun eventClickSearchResultBrandSearchPage(item: Brand, position: Int) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PRODUCT_CLICK,
                DealsAnalyticsConstants.Action.SEARCH_RESULT_CLICK,
                String.format(DealsAnalyticsConstants.Label.SEARCH_RESULT_BRAND_CLICK, item.title, position + 1)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CLICK, DataLayer.mapOf(
                    DealsAnalyticsConstants.ACTION_FIELD, DataLayer.mapOf(
                        DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.SEARCH_RESULT_LIST),
                DealsAnalyticsConstants.PRODUCTS, getECommerceDataBrandSearchPage(item, position)
        )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceDataBrandSearchPage(item: Brand, position: Int): MutableList<MutableMap<String, Any>> {
        val data = mutableListOf<MutableMap<String, Any>>()
        data.add(DataLayer.mapOf(
                DealsAnalyticsConstants.Item.name, item.title,
                DealsAnalyticsConstants.Item.id, item.id,
                DealsAnalyticsConstants.Item.price, 0,
                DealsAnalyticsConstants.Item.brand, item.title,
                DealsAnalyticsConstants.Item.category, DealsAnalyticsConstants.NONE,
                DealsAnalyticsConstants.Item.variant, DealsAnalyticsConstants.NONE,
                DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.SEARCH_RESULT_LIST,
                DealsAnalyticsConstants.Item.position, position + 1)
        )
        return data
    }

    fun eventViewChipsSearchPage() {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.VIEW_DEALS_IRIS,
                DealsAnalyticsConstants.Action.CHIPS_IMPRESSION,
                "-"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickChipsSearchPage(chipsName: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CHIPS_CLICK,
                String.format(DealsAnalyticsConstants.Label.CHIPS_CLICK, chipsName)
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventViewLastSeenSearchPage() {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.VIEW_DEALS_IRIS,
                DealsAnalyticsConstants.Action.LASTSEEN_IMPRESSION,
                "-"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickLastSeenSearchPage(lastSeenName: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.LASTSEEN_CLICK,
                String.format(DealsAnalyticsConstants.Label.LASTSEEN_CLICK, lastSeenName)
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickChangeLocationBrandPage(oldLocation: String, newLocation: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CHANGE_LOCATION_BRAND_PAGE,
                String.format(DealsAnalyticsConstants.Label.CHANGE_LOCATION, oldLocation, newLocation)
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickSearchBrandPage() {
        val map = getTrackingBrandSearchWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.SEARCH_BRAND_PAGE_CLICK,
                "-"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventViewSearchResultBrandPage(keyword: String, location: String, items: List<DealsBrandsDataView.Brand>, categoryName: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PRODUCT_VIEW,
                DealsAnalyticsConstants.Action.VIEW_SEARCH_RESULT_BRAND_PAGE,
                String.format(DealsAnalyticsConstants.Label.VIEW_SEARCH_RESULT, keyword, location)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CURRENCY_CODE, DealsAnalyticsConstants.IDR,
                DealsAnalyticsConstants.IMPRESSIONS, getECommerceDataBrandListBrandPage(items, categoryName)
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceDataBrandListBrandPage(items: List<DealsBrandsDataView.Brand>, categoryName: String): MutableList<MutableMap<String, Any>> {
        val dataImpressions = mutableListOf<MutableMap<String, Any>>()
        items.forEach {
            val impression = DataLayer.mapOf(
                    DealsAnalyticsConstants.Item.name, it.title,
                    DealsAnalyticsConstants.Item.id, it.id,
                    DealsAnalyticsConstants.Item.category, categoryName,
                    DealsAnalyticsConstants.Item.brand, it.title,
                    DealsAnalyticsConstants.Item.price, 0,
                    DealsAnalyticsConstants.Item.variant, DealsAnalyticsConstants.NONE,
                    DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.FOOD_VOUCHER_LIST,
                    DealsAnalyticsConstants.Item.position, it.position + 1
            )
            dataImpressions.add(impression)
        }
        return dataImpressions
    }

    fun eventViewSearchNoResultBrandPage(keyword: String, categoryName: String, location: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.VIEW_DEALS_IRIS,
                DealsAnalyticsConstants.Action.VIEW_SEARCH_RESULT_BRAND_PAGE,
                String.format(DealsAnalyticsConstants.Label.VIEW_SEARCH_RESULT_BRAND_NOT_FOUND, keyword, categoryName, location)
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickSearchResultBrandBrandPage(item: DealsBrandsDataView.Brand, categoryName: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PRODUCT_CLICK,
                DealsAnalyticsConstants.Action.SEARCH_RESULT_BRAND_CLICK,
                String.format(DealsAnalyticsConstants.Label.SEARCH_RESULT_BRAND_CLICK, item.title, item.position + 1)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CLICK, DataLayer.mapOf(DealsAnalyticsConstants.ACTION_FIELD, DataLayer.mapOf(
                    DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.FOOD_VOUCHER_LIST),
                DealsAnalyticsConstants.PRODUCTS, getECommerceDataBrandBrandPage(item, categoryName)
        )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceDataBrandBrandPage(item: DealsBrandsDataView.Brand, categoryName: String): MutableList<MutableMap<String, Any>> {
        val data = mutableListOf<MutableMap<String, Any>>()
        data.add(DataLayer.mapOf(
                DealsAnalyticsConstants.Item.name, item.title,
                DealsAnalyticsConstants.Item.id, item.id,
                DealsAnalyticsConstants.Item.category, categoryName,
                DealsAnalyticsConstants.Item.variant, DealsAnalyticsConstants.NONE,
                DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.FOOD_VOUCHER_LIST,
                DealsAnalyticsConstants.Item.price, 0,
                DealsAnalyticsConstants.Item.brand, item.title,
                DealsAnalyticsConstants.Item.position, item.position + 1))
        return data
    }

    fun eventClickCategoryTabBrandPage(categoryName: String, position: Int) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CATEGORY_TAB_BRAND_PAGE,
                String.format(DealsAnalyticsConstants.Label.CATEGORY_TAB_CLICK, categoryName, position + 1)
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventViewPopularBrandBrandPage(items: List<DealsBrandsDataView.Brand>, categoryName: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PRODUCT_VIEW,
                DealsAnalyticsConstants.Action.BRAND_POPULAR_IMPRESSION_BRAND_PAGE,
                "-"
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CURRENCY_CODE, DealsAnalyticsConstants.IDR,
                DealsAnalyticsConstants.IMPRESSIONS, getECommerceDataBrandListBrandPage(items, categoryName)
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun eventSeeHomePage() {
        val map = getTrackingOpenHomePageWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.OPEN_SCREEN,
                "-",
                "-"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventSeeHomePageBanner(bannerId: String, bannerPosition: Int, promotions: BannersDataView.BannerDataView) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PROMO_VIEW,
                DealsAnalyticsConstants.Action.BANNER_IMPRESSION,
                String.format(DealsAnalyticsConstants.Label.BANNER_VIEW, bannerId, bannerPosition + 1)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.Event.PROMO_VIEW, DataLayer.mapOf(
                DealsAnalyticsConstants.PROMOTIONS, getECommerceHomePageBannerData(listOf(promotions), bannerPosition)
        )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickHomePageBanner(bannerId: String, bannerPosition: Int, promotions: BannersDataView.BannerDataView) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PROMO_CLICK,
                DealsAnalyticsConstants.Action.CLICK_BANNER,
                String.format(DealsAnalyticsConstants.Label.BANNER_VIEW, bannerId, bannerPosition + 1)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.Event.PROMO_CLICK, DataLayer.mapOf(
                DealsAnalyticsConstants.PROMOTIONS, getECommerceHomePageBannerData(listOf(promotions), bannerPosition)
        )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceHomePageBannerData(promotions: List<BannersDataView.BannerDataView>, startPosition: Int = 0): MutableList<MutableMap<String, Any>> {
        val data = mutableListOf<MutableMap<String, Any>>()
        for ((index, banner) in promotions.withIndex()) {
            data.add(DataLayer.mapOf(
                    DealsAnalyticsConstants.Promotions.id, banner.bannerId,
                    DealsAnalyticsConstants.Promotions.name, banner.bannerName,
                    DealsAnalyticsConstants.Promotions.creative, banner.bannerName,
                    DealsAnalyticsConstants.Promotions.creative_url, banner.bannerImageUrl,
                    DealsAnalyticsConstants.Promotions.position, startPosition + index + 1,
                    DealsAnalyticsConstants.Promotions.category, DealsAnalyticsConstants.Category.DIGITAL_DEALS
            ))
        }
        return data
    }

    fun eventClickAllBanner() {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PROMO_CLICK,
                DealsAnalyticsConstants.Action.CLICK_VIEW_ALL_BANNER,
                ""
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickCategoryIcon(categoryIconName: String, position: Int) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CLICK_CATEGORY_ICON,
                String.format(DealsAnalyticsConstants.Label.CATEGORY_ICON_CLICK, categoryIconName, position + 1)
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventSeeAllBrandPopular() {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CLICK_VIEW_ALL_BRAND_POPULAR,
                ""
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventSeeAllBrandPopularOnCategoryPage() {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CLICK_VIEW_ALL_BRAND_POPULAR_CATEGORY_PAGE,
                ""
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventChangeLocationHomePage(prevLocation: String, newLocation: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CHANGE_LOCATION_HOME_PAGE,
                String.format(DealsAnalyticsConstants.Label.LOCATION_HOME_PAGE_CHANGE, prevLocation, newLocation)
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickSearchHomePage() {
        val map = getTrackingSearchWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CLICK_SEARCH,
                "-"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickSearchCategoryPage() {
        val map = getTrackingCategorySearchWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CLICK_SEARCH,
                "-"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventScrollToBrandPopular(brand: DealsBrandsDataView.Brand, position: Int) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PRODUCT_VIEW,
                DealsAnalyticsConstants.Action.BRAND_POPULAR_IMPRESSION,
                String.format(DealsAnalyticsConstants.Label.BRAND_NAME_SCROLL, brand.title, position + 1)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CURRENCY_CODE, DealsAnalyticsConstants.IDR,
                DealsAnalyticsConstants.IMPRESSIONS, getECommerceBrandPopularImpression(brand, position)
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceBrandPopularImpression(item: DealsBrandsDataView.Brand, position: Int): MutableList<MutableMap<String, Any>> {
        val dataImpressions = mutableListOf<MutableMap<String, Any>>()
        item.apply {
            val impression = DataLayer.mapOf(
                    DealsAnalyticsConstants.Item.name, title,
                    DealsAnalyticsConstants.Item.id, id,
                    DealsAnalyticsConstants.Item.price, 0,
                    DealsAnalyticsConstants.Item.brand, title,
                    DealsAnalyticsConstants.Item.category, title,
                    DealsAnalyticsConstants.Item.variant, DealsAnalyticsConstants.NONE,
                    DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.FOOD_VOUCHER_LIST,
                    DealsAnalyticsConstants.Item.position, position + 1
            )
            dataImpressions.add(impression)
        }
        return dataImpressions
    }

    fun eventClickChangeLocationCategoryPage(oldLocation: String, newLocation: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CHANGE_LOCATION_CATEGORY_PAGE,
                String.format(DealsAnalyticsConstants.Label.CHANGE_LOCATION_CATEGORY_PAGE, oldLocation, newLocation)
        )
        Log.d("row33", "$map")
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickBrandPopular(item: DealsBrandsDataView.Brand, position: Int, isFromCategory: Boolean) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PRODUCT_CLICK,
                if (isFromCategory) DealsAnalyticsConstants.Action.CLICK_ON_BRAND_POPULAR_CATEGORY_PAGE
                else DealsAnalyticsConstants.Action.CLICK_ON_BRAND_POPULAR,
                String.format(DealsAnalyticsConstants.Label.BRAND_CLICK, item.title, position + 1)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CLICK, DataLayer.mapOf(
                    DealsAnalyticsConstants.ACTION_FIELD, DataLayer.mapOf(DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.FOOD_VOUCHER_LIST),
                    DealsAnalyticsConstants.PRODUCTS, getECommerceDataClickBrandPopular(item, position))
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceDataClickBrandPopular(item: DealsBrandsDataView.Brand, position: Int): MutableList<MutableMap<String, Any>> {
        val listProduct = mutableListOf<MutableMap<String, Any>>()
        val product = DataLayer.mapOf(
                DealsAnalyticsConstants.Products.name, item.title,
                DealsAnalyticsConstants.Products.id, item.id,
                DealsAnalyticsConstants.Products.variant, DealsAnalyticsConstants.NONE,
                DealsAnalyticsConstants.Products.list, DealsAnalyticsConstants.FOOD_VOUCHER_LIST,
                DealsAnalyticsConstants.Products.position, position + 1
        )
        listProduct.add(product)
        return listProduct
    }

    fun eventClickViewAllProductCardInHomepage() {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CLICK_VIEW_ALL_PRODUCT_CARD_HOME_PAGE,
                "-"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun impressionCuratedProduct(curatedProductCategoryDataView: CuratedProductCategoryDataView, position: Int) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PRODUCT_VIEW,
                String.format(DealsAnalyticsConstants.Action.PRODUCT_CARD_HOME_PAGE_IMPRESSION,curatedProductCategoryDataView.title),
                String.format(DealsAnalyticsConstants.Label.BRAND_NAME_SCROLL, curatedProductCategoryDataView.title, position + 1)
        )

        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CURRENCY_CODE, DealsAnalyticsConstants.IDR,
                DealsAnalyticsConstants.IMPRESSIONS, getECommerceCuratedProduct(curatedProductCategoryDataView)
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceCuratedProduct(curatedProduct: CuratedProductCategoryDataView): MutableList<MutableMap<String, Any>> {
        val dataImpressions = mutableListOf<MutableMap<String, Any>>()
        curatedProduct.productCards.forEachIndexed { index, it ->
            val price = if (it.priceNonCurrency.isNotEmpty()) it.priceNonCurrency.toInt() else 0
            val impression = DataLayer.mapOf(
                    DealsAnalyticsConstants.Item.name, it.title,
                    DealsAnalyticsConstants.Item.id, it.id,
                    DealsAnalyticsConstants.Item.price, price,
                    DealsAnalyticsConstants.Item.brand, it.brand,
                    DealsAnalyticsConstants.Item.category, it.categoryName,
                    DealsAnalyticsConstants.Item.variant, DealsAnalyticsConstants.NONE,
                    DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.FOOD_VOUCHER_LIST,
                    DealsAnalyticsConstants.Item.position, index + 1
            )
            dataImpressions.add(impression)
        }

        return dataImpressions
    }

    fun curatedProductClick(productCardDataView: ProductCardDataView, position: Int, sectionTitle: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PRODUCT_CLICK,
                String.format(DealsAnalyticsConstants.Action.CLICK_ON_PRODUCT_CARD_HOME_PAGE, sectionTitle),
                String.format(DealsAnalyticsConstants.Label.CLICK_PRODUCT_HOMEPAGE, productCardDataView.title, productCardDataView.brand, position + 1)
        )

        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CLICK, DataLayer.mapOf(
                    DealsAnalyticsConstants.ACTION_FIELD, DataLayer.mapOf(DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.PRODUCT_HOME_PAGE_LIST),
                    DealsAnalyticsConstants.PRODUCTS, getCuratedProductClick(productCardDataView, position)
        ))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getCuratedProductClick(productCardDataView: ProductCardDataView, position: Int): MutableList<MutableMap<String, Any>> {
        val price = if (productCardDataView.priceNonCurrency.isNotEmpty()) productCardDataView.priceNonCurrency.toInt() else 0
        val data = mutableListOf<MutableMap<String, Any>>()

        data.add(DataLayer.mapOf(
                DealsAnalyticsConstants.Item.name, productCardDataView.title,
                DealsAnalyticsConstants.Item.id, productCardDataView.id,
                DealsAnalyticsConstants.Item.price, price,
                DealsAnalyticsConstants.Item.brand, productCardDataView.brand,
                DealsAnalyticsConstants.Item.category, DealsAnalyticsConstants.NONE,
                DealsAnalyticsConstants.Item.variant, DealsAnalyticsConstants.NONE,
                DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.PRODUCT_HOME_PAGE_LIST,
                DealsAnalyticsConstants.Item.position, position + 1
        ))
        return data
    }

    fun clickAllCuratedProduct(title: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                String.format(DealsAnalyticsConstants.Action.CLICK_VIEW_ALL_PRODUCT_CARD_HOME_PAGE2, title),
                "-"
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun clickOrderListDeals() {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CLICK_ON_ORDER_LIST,
                "-"
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickCategoryTabCategoryPage(categoryName: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CATEGORY_TAB_CATEGORY_PAGE,
                String.format(DealsAnalyticsConstants.Label.CATEGORY_TAB_CLICK_ONLY_CATEGORY_NAME, categoryName)
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventClickChipsCategory() {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CHIPS_CLICK_CATEGORY_PAGE,
                "-"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventApplyChipsCategory(dealsChipsDataView: DealsChipsDataView) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        val filter = mapFilterAppliedChips(dealsChipsDataView)
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.APPLY_FILTER_CATEGORY_PAGE,
                String.format(DealsAnalyticsConstants.Label.CATEGORY_FILTER_CHIPS_APPLIED, filter)
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    private fun mapFilterAppliedChips(dealsChipsDataView: DealsChipsDataView): String {
        val builder = StringBuilder()
        if (dealsChipsDataView.chipList.isNotEmpty()) {
            dealsChipsDataView.chipList.forEach {
                if (it.isSelected)
                    builder.append(" - ${it.title}")
            }
        } else builder.append("")

        return builder.toString()
    }

    fun impressionProductCategory(productCardDataView: ProductCardDataView, position: Int, page: Int) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PRODUCT_VIEW,
                DealsAnalyticsConstants.Action.PRODUCT_CARD_CATEGORY_PAGE_IMPRESSION,
                String.format(DealsAnalyticsConstants.Label.BRAND_NAME_SCROLL, PRODUCT_CARD, page)
        )

        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CURRENCY_CODE, DealsAnalyticsConstants.IDR,
                DealsAnalyticsConstants.IMPRESSIONS, getECommerceProductCategory(productCardDataView, position)
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceProductCategory(productCardDataView: ProductCardDataView, position: Int): MutableList<MutableMap<String, Any>> {
        val dataImpressions = mutableListOf<MutableMap<String, Any>>()
        productCardDataView.let {
            val price = if (it.priceNonCurrency.isNotEmpty()) it.priceNonCurrency.toInt() else 0
            val impression = DataLayer.mapOf(
                    DealsAnalyticsConstants.Item.name, it.title,
                    DealsAnalyticsConstants.Item.id, it.id,
                    DealsAnalyticsConstants.Item.price, price,
                    DealsAnalyticsConstants.Item.brand, it.brand,
                    DealsAnalyticsConstants.Item.category, it.categoryName,
                    DealsAnalyticsConstants.Item.variant, DealsAnalyticsConstants.NONE,
                    DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.FOOD_VOUCHER_LIST,
                    DealsAnalyticsConstants.Item.position, position + 1
            )
            dataImpressions.add(impression)
        }

        return dataImpressions
    }

    fun eventSeePopularLandmarkView(voucherPlaceCard: VoucherPlacePopularDataView, position: Int) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PROMO_VIEW,
                DealsAnalyticsConstants.Action.IMPRESSION_ON_POPULAR_LANDMARK,
                String.format(DealsAnalyticsConstants.Label.POPULAR_LANDMARK_VIEW, voucherPlaceCard.title, position)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.Event.PROMO_VIEW, DataLayer.mapOf(DealsAnalyticsConstants.PROMOTIONS,
                getECommerceLandmarkPopular(voucherPlaceCard.voucherPlaceCards)
        ))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickLandmarkPopular(item: VoucherPlaceCardDataView, position: Int) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PROMO_CLICK,
                DealsAnalyticsConstants.Action.CLICK_ON_POPULAR_LANDMARK,
                String.format(DealsAnalyticsConstants.Label.POPULAR_LANDMARK_CLICK, item.name, position)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.Event.PROMO_CLICK, DataLayer.mapOf(
                    DealsAnalyticsConstants.PROMOTIONS, getECommerceLandmarkPopular(listOf(item), position))
        )

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceLandmarkPopular(items: List<VoucherPlaceCardDataView>, startPosition: Int = 0): MutableList<MutableMap<String, Any>> {
        val dataPromotions = mutableListOf<MutableMap<String, Any>>()
        for ((index, item) in items.withIndex()) {
            item.apply {
                val promotions = DataLayer.mapOf(
                        DealsAnalyticsConstants.Promotions.id, item.id,
                        DealsAnalyticsConstants.Promotions.name, DealsAnalyticsConstants.DEALS_POPULAR_LANDMARK,
                        DealsAnalyticsConstants.Promotions.creative, item.name,
                        DealsAnalyticsConstants.Promotions.creative_url, imageUrl,
                        DealsAnalyticsConstants.Promotions.position, index + 1 + startPosition

                )
                dataPromotions.add(promotions)
            }
        }
        return dataPromotions
    }


    fun eventSeeCuratedSection(curatedCategoryDataView: CuratedCategoryDataView, position: Int) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PROMO_VIEW,
                DealsAnalyticsConstants.Action.IMPRESSION_ON_CURATED_CARD,
                String.format(DealsAnalyticsConstants.Label.CURATED_CARD_VIEW, curatedCategoryDataView.title, position)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.Event.PROMO_VIEW, DataLayer.mapOf(DealsAnalyticsConstants.PROMOTIONS,
                getECommerceViewCurated(curatedCategoryDataView.curatedCategories)
        ))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickCuratedSection(curatedCategory: CuratedCategoryDataView.CuratedCategory, position: Int) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PROMO_CLICK,
                DealsAnalyticsConstants.Action.CLICK_ON_CURATED_CARD,
                String.format(DealsAnalyticsConstants.Label.CURATED_CARD_VIEW, curatedCategory.name, position)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.Event.PROMO_CLICK, DataLayer.mapOf(DealsAnalyticsConstants.PROMOTIONS, getECommerceViewCurated(listOf(curatedCategory))
        ))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceViewCurated(items: List<CuratedCategoryDataView.CuratedCategory>, startingPosition: Int = 0): MutableList<MutableMap<String, Any>> {
        val dataPromotions = mutableListOf<MutableMap<String, Any>>()
        for ((index, item) in items.withIndex()) {
            item.apply {
                val promotions = DataLayer.mapOf(
                        DealsAnalyticsConstants.Promotions.id, item.id,
                        DealsAnalyticsConstants.Promotions.name, DealsAnalyticsConstants.DEALS_CURATED_CARD,
                        DealsAnalyticsConstants.Promotions.creative, item.name,
                        DealsAnalyticsConstants.Promotions.creative_url, item.imageUrl,
                        DealsAnalyticsConstants.Promotions.position, startingPosition + index + 1

                )
                dataPromotions.add(promotions)
            }
        }

        return dataPromotions
    }

    fun eventSearchResultCaseShownOnCategoryPage(keyword: String, location: String, eventProductDetail: List<EventProductDetail>, pageSize: Int, page: Int) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PRODUCT_VIEW,
                DealsAnalyticsConstants.Action.IMPRESSION_ON_SEARCH_RESULT,
                String.format(DealsAnalyticsConstants.Label.SEARCH_RESULT_CASE_SHOWN, keyword, location)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CURRENCY_CODE, DealsAnalyticsConstants.IDR,
                DealsAnalyticsConstants.IMPRESSIONS, getECommerceViewSearchResultCaseShown(eventProductDetail, pageSize, page)
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceViewSearchResultCaseShown(items: List<EventProductDetail>, pageSize: Int, page: Int): MutableList<MutableMap<String, Any>> {
        val dataPromotions = mutableListOf<MutableMap<String, Any>>()
        for ((index, item) in items.withIndex()) {
            item.apply {
                val promotions = DataLayer.mapOf(
                        DealsAnalyticsConstants.Impressions.name, item.displayName,
                        DealsAnalyticsConstants.Impressions.id, item.brandId,
                        DealsAnalyticsConstants.Impressions.brand, item.brand.title,
                        DealsAnalyticsConstants.Impressions.category, item.category.firstOrNull()?.title ?: "",
                        DealsAnalyticsConstants.Impressions.list, DealsAnalyticsConstants.FOOD_VOUCHER_LIST,
                        DealsAnalyticsConstants.Impressions.price, item.salesPrice,
                        DealsAnalyticsConstants.Impressions.position, (pageSize * page) + index + 1

                )
                dataPromotions.add(promotions)
            }
        }

        return dataPromotions
    }
}
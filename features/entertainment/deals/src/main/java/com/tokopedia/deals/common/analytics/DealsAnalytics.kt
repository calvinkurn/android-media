package com.tokopedia.deals.common.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.deals.common.model.response.Brand
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
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

    private fun MutableMap<String, Any>.addGeneralEvent(event: String, action: String, label: String): MutableMap<String, Any>? {
        this[TrackAppUtils.EVENT] = event
        this[TrackAppUtils.EVENT_CATEGORY] = DealsAnalyticsConstants.Category.DIGITAL_DEALS
        this[TrackAppUtils.EVENT_ACTION] = action
        this[TrackAppUtils.EVENT_LABEL] = label
        return this
    }

    fun eventClickChangeLocationSearchPage(oldLocation: String, newLocation : String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CHANGE_LOCATION_SEARCH_PAGE,
                String.format(DealsAnalyticsConstants.Label.CHANGE_LOCATION, oldLocation, newLocation)
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventViewSearchResultSearchPage(keyword: String, location : String, items: List<VoucherModel>) {
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
                    DealsAnalyticsConstants.Item.position, it.position+1
            )
            dataImpressions.add(impression)
        }
        return dataImpressions
    }

    fun eventViewSearchNoResultSearchPage(keyword: String, location : String) {
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
                String.format(DealsAnalyticsConstants.Label.SEARCH_RESULT_CLICK, item.voucherName, item.merchantName, item.position+1)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
            DealsAnalyticsConstants.CLICK, DataLayer.mapOf(DealsAnalyticsConstants.ACTION_FIELD, DealsAnalyticsConstants.SEARCH_RESULT_LIST,
                DealsAnalyticsConstants.PRODUCTS, getECommerceDataVoucherSearchPage(item)
            )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceDataVoucherSearchPage(item: VoucherModel): MutableMap<String, Any> {
        val price = if (item.realPrice.isNotEmpty()) item.realPrice.toInt() else 0
        return DataLayer.mapOf(
                DealsAnalyticsConstants.Item.name, item.voucherName,
                DealsAnalyticsConstants.Item.id, item.voucherId,
                DealsAnalyticsConstants.Item.price, price,
                DealsAnalyticsConstants.Item.brand, item.merchantName,
                DealsAnalyticsConstants.Item.category, DealsAnalyticsConstants.NONE,
                DealsAnalyticsConstants.Item.variant, DealsAnalyticsConstants.NONE,
                DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.SEARCH_RESULT_LIST,
                DealsAnalyticsConstants.Item.position, item.position + 1
        )
    }

    fun eventClickSearchResultBrandSearchPage(item: Brand, position: Int) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PRODUCT_CLICK,
                DealsAnalyticsConstants.Action.SEARCH_RESULT_CLICK,
                String.format(DealsAnalyticsConstants.Label.SEARCH_RESULT_BRAND_CLICK, item.title, position+1)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CLICK, DataLayer.mapOf(DealsAnalyticsConstants.ACTION_FIELD, DealsAnalyticsConstants.SEARCH_RESULT_LIST,
                DealsAnalyticsConstants.PRODUCTS, getECommerceDataBrandSearchPage(item, position)
            )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceDataBrandSearchPage(item: Brand, position: Int): MutableMap<String, Any> {
        return DataLayer.mapOf(
                DealsAnalyticsConstants.Item.name, item.title,
                DealsAnalyticsConstants.Item.id, item.id,
                DealsAnalyticsConstants.Item.category, DealsAnalyticsConstants.NONE,
                DealsAnalyticsConstants.Item.variant, DealsAnalyticsConstants.NONE,
                DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.SEARCH_RESULT_LIST,
                DealsAnalyticsConstants.Item.position, position+1
        )
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
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.SEARCH_BRAND_PAGE_CLICK,
                "-"
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventViewSearchResultBrandPage(keyword: String, location : String, items: List<DealsBrandsDataView.Brand>, categoryName: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PROMO_VIEW,
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
                    DealsAnalyticsConstants.Item.variant, DealsAnalyticsConstants.NONE,
                    DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.FOOD_VOUCHER_LIST,
                    DealsAnalyticsConstants.Item.position, it.position+1
            )
            dataImpressions.add(impression)
        }
        return dataImpressions
    }

    fun eventViewSearchNoResultBrandPage(keyword: String, categoryName: String, location : String) {
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
                DealsAnalyticsConstants.Event.PROMO_CLICK,
                DealsAnalyticsConstants.Action.SEARCH_RESULT_BRAND_CLICK,
                String.format(DealsAnalyticsConstants.Label.SEARCH_RESULT_BRAND_CLICK, item.title, item.position+1)
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CLICK, DataLayer.mapOf(DealsAnalyticsConstants.ACTION_FIELD, DealsAnalyticsConstants.FOOD_VOUCHER_LIST,
                DealsAnalyticsConstants.PRODUCTS, getECommerceDataBrandBrandPage(item, categoryName)
            )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceDataBrandBrandPage(item: DealsBrandsDataView.Brand, categoryName: String): MutableMap<String, Any> {
        return DataLayer.mapOf(
                DealsAnalyticsConstants.Item.name, item.title,
                DealsAnalyticsConstants.Item.id, item.id,
                DealsAnalyticsConstants.Item.category, categoryName,
                DealsAnalyticsConstants.Item.variant, DealsAnalyticsConstants.NONE,
                DealsAnalyticsConstants.Item.list, DealsAnalyticsConstants.FOOD_VOUCHER_LIST,
                DealsAnalyticsConstants.Item.position, item.position+1
        )
    }

    fun eventClickCategoryTabBrandPage(categoryName: String, position: Int) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.CLICK_DEALS,
                DealsAnalyticsConstants.Action.CATEGORY_TAB_BRAND_PAGE,
                String.format(DealsAnalyticsConstants.Label.CATEGORY_TAB_CLICK, categoryName, position+1)
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventViewPopularBrandBrandPage(items: List<DealsBrandsDataView.Brand>, categoryName: String) {
        val map = getTrackingMapWithHeader() as MutableMap<String, Any>
        map.addGeneralEvent(
                DealsAnalyticsConstants.Event.PROMO_VIEW,
                DealsAnalyticsConstants.Action.BRAND_POPULAR_IMPRESSION_BRAND_PAGE,
                "-"
        )
        map[DealsAnalyticsConstants.ECOMMERCE_LABEL] = DataLayer.mapOf(
                DealsAnalyticsConstants.CURRENCY_CODE, DealsAnalyticsConstants.IDR,
                DealsAnalyticsConstants.IMPRESSIONS, getECommerceDataBrandListBrandPage(items, categoryName)
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }
}
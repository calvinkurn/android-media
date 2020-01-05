package com.tokopedia.discovery.find.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics

private const val KEY_EVENT = "event"
private const val KEY_EVENT_CATEGORY = "eventCategory"
private const val KEY_EVENT_ACTION = "eventAction"
private const val KEY_EVENT_LABEL = "eventLabel"
private const val EVENT_CLICK_FIND_VALUE = "clickFind"
private const val EVENT_CLICK_BREADCRUMB_VALUE = "click Breadcrumb"
private const val EVENT_FIND_VALUE = "find"
private const val EVENT_PRODUCT_VIEW_VALUE = "productView"
private const val EVENT_PRODUCT_IMPRESSION_VALUE = "product list impression"
private const val KEY_ECOMMERCE = "ecommerce"
private const val KEY_CURRENCY_CODE = "currencyCode"
private const val CURRENCY_VALUE = "IDR"
private const val KEY_IMPRESSIONS = "impressions"
private const val KEY_NAME = "name"
private const val KEY_ID = "id"
private const val KEY_PRICE = "price"
private const val KEY_BRAND = ""
private const val KEY_CATEGORY = "category"
private const val KEY_VARIANT = "variant"
private const val KEY_LIST = "list"
private const val KEY_POSITION = "position"
private const val EVENT_PRODUCT_CLICK_VALUE = "productClick"
private const val EVENT_PRODUCT_CLICK_ACTION_VALUE = "click product list"
private const val EVENT_CLICK_WISHLIST_VALUE = "clickWishlist"
private const val EVENT_CLICK_WISHLIST_ACTION_VALUE = "add to wishlist"
private const val EVENT_REMOVE_WISHLIST_ACTION_VALUE = "remove wishlist"
private const val EVENT_VIEW_ALL_ACTION_VALUE = "click view all"
private const val EVENT_FILTER_ACTION_VALUE = "click filter"
private const val EVENT_SORT_ACTION_VALUE = "click sort"
private const val EVENT_VIEW_TYPE_ACTION_VALUE = "click view type"
private const val EVENT_RELATED_SEARCH_ACTION_VALUE = "click pencarian terkait"
private const val EVENT_PRICE_LIST_ACTION_VALUE = "click daftar harga"
private const val EVENT_BACK_BUTTON_ACTION_VALUE = "click back button"
private const val EVENT_SEARCH_BAR_ACTION_VALUE = "click search bar"
private const val EVENT_HOME_ICON_ACTION_VALUE = "click home icon"
private const val EVENT_SEARCH_KEYWORD_ACTION_VALUE = "click search"
private const val KEY_CLICK = "click"
private const val KEY_ACTION_FIELD = "actionField"
private const val KEY_PRODUCTS = "products"
private const val TOP_ADS_VALUE = " - topads"

class FindPageAnalytics {

    companion object {
        val findPageAnalytics: FindPageAnalytics by lazy { FindPageAnalytics() }
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun eventClickBreadCrumb(destinationUrl: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_CLICK_BREADCRUMB_VALUE,
                KEY_EVENT_LABEL, destinationUrl
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventProductListViewImpression(productName: String, productId: String, productPrice: String,
                                       productBrand: String, categoryBreadCrumb: String,
                                       productVariant: String, productListName: String, productPositionIndex: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_PRODUCT_VIEW_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_PRODUCT_IMPRESSION_VALUE,
                KEY_EVENT_LABEL, "",
                KEY_ECOMMERCE, DataLayer.mapOf(
                KEY_CURRENCY_CODE, CURRENCY_VALUE,
                KEY_IMPRESSIONS, DataLayer.mapOf(DataLayer.mapOf(
                KEY_NAME, productName,
                KEY_ID, productId,
                KEY_PRICE, productPrice,
                KEY_BRAND, productBrand,
                KEY_CATEGORY, categoryBreadCrumb,
                KEY_VARIANT, productVariant,
                KEY_LIST, productListName,
                KEY_POSITION, productPositionIndex
        ))))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventProductClick(productName: String, productId: String, productPrice: String,
                          productBrand: String, categoryBreadCrumb: String,
                          productVariant: String, productListName: String, productPositionIndex: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_PRODUCT_CLICK_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_PRODUCT_CLICK_ACTION_VALUE,
                KEY_EVENT_LABEL, productId,
                KEY_ECOMMERCE, DataLayer.mapOf(
                KEY_CLICK, DataLayer.mapOf(
                KEY_ACTION_FIELD, DataLayer.mapOf(
                KEY_LIST, productListName,
                KEY_PRODUCTS, DataLayer.mapOf(
                KEY_NAME, productName,
                KEY_ID, productId,
                KEY_PRICE, productPrice,
                KEY_BRAND, productBrand,
                KEY_CATEGORY, categoryBreadCrumb,
                KEY_VARIANT, productVariant,
                KEY_LIST, productListName,
                KEY_POSITION, productPositionIndex
        )))))
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickWishList(productId: String, isWishListed: Boolean, isTopAds: Boolean) {
        val tracker = getTracker()
        var eventAction: String = if (isWishListed) {
            EVENT_CLICK_WISHLIST_ACTION_VALUE
        } else {
            EVENT_REMOVE_WISHLIST_ACTION_VALUE
        }

        if (isTopAds) {
            eventAction += TOP_ADS_VALUE
        }

        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_WISHLIST_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, eventAction,
                KEY_EVENT_LABEL, productId
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickViewAll(destinationUrl: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_VIEW_ALL_ACTION_VALUE,
                KEY_EVENT_LABEL, destinationUrl
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickFilter() {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_FILTER_ACTION_VALUE,
                KEY_EVENT_LABEL, ""
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickSort(sortValue: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_SORT_ACTION_VALUE,
                KEY_EVENT_LABEL, sortValue
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickViewType() {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_VIEW_TYPE_ACTION_VALUE,
                KEY_EVENT_LABEL, ""
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickRelatedSearch() {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_RELATED_SEARCH_ACTION_VALUE,
                KEY_EVENT_LABEL, ""
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickPriceList() {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_PRICE_LIST_ACTION_VALUE,
                KEY_EVENT_LABEL, ""
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickBackButton() {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_BACK_BUTTON_ACTION_VALUE,
                KEY_EVENT_LABEL, ""
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickSearchBar() {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_SEARCH_BAR_ACTION_VALUE,
                KEY_EVENT_LABEL, ""
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickHomeIcon() {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_HOME_ICON_ACTION_VALUE,
                KEY_EVENT_LABEL, ""
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickSearchKeyword(keyword: String) {
        val tracker = getTracker()
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_SEARCH_KEYWORD_ACTION_VALUE,
                KEY_EVENT_LABEL, keyword
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }
}
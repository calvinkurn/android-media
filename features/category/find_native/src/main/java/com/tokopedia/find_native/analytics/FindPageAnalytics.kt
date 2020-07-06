package com.tokopedia.find_native.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.common_category.model.productModel.ProductsItem
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
private const val KEY_BRAND = "brand"
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
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_CLICK_BREADCRUMB_VALUE,
                KEY_EVENT_LABEL, destinationUrl
        )
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun eventProductListViewImpression(viewedProductList: List<Visitable<Any>>,
                                       viewedTopAdsList: List<Visitable<Any>>,
                                       findNavScreenName: String) {
        val list = ArrayList<Map<String, Any>>()
        val itemList = ArrayList<Visitable<Any>>()

        itemList.addAll(viewedProductList)
        itemList.addAll(viewedTopAdsList)
        var productListName: String
        for (element in itemList) {
            val item = element as ProductsItem
            productListName = getProductListName(item, findNavScreenName)
            val map = HashMap<String, Any>()
            map[KEY_NAME] = item.name
            map[KEY_ID] = item.id.toString()
            map[KEY_PRICE] = getItemPriceInNumber(item.price)
            map[KEY_BRAND] = ""
            map[KEY_CATEGORY] = item.categoryBreadcrumb + " / " + item.category
            map[KEY_LIST] = productListName
            map[KEY_VARIANT] = ""
            map[KEY_POSITION] = item.adapter_position + 1
            list.add(map)
        }
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_PRODUCT_VIEW_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_PRODUCT_IMPRESSION_VALUE,
                KEY_EVENT_LABEL, "",
                KEY_ECOMMERCE, DataLayer.mapOf(
                KEY_CURRENCY_CODE, CURRENCY_VALUE,
                KEY_IMPRESSIONS, list))
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    private fun getItemPriceInNumber(price: String): String {
        var result: String = price.replace("Rp", "", true)
        result = result.replace(".", "")
        result.trim()
        return result
    }

    private fun getProductListName(item: ProductsItem, findNavScreenName: String): String {
        var productListName = EVENT_FIND_VALUE + "/" + findNavScreenName + "/" + item.name
        if (item.isTopAds) {
            productListName += TOP_ADS_VALUE
        }
        return productListName
    }

    fun eventProductClick(product: ProductsItem, findNavScreenName: String) {
        val productListName = "$EVENT_FIND_VALUE/$findNavScreenName"
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_PRODUCT_CLICK_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_PRODUCT_CLICK_ACTION_VALUE,
                KEY_EVENT_LABEL, product.id?.toString() ?: "",
                KEY_ECOMMERCE, DataLayer.mapOf(
                KEY_CLICK, DataLayer.mapOf(
                KEY_ACTION_FIELD, DataLayer.mapOf(
                KEY_LIST, productListName),
                KEY_PRODUCTS, DataLayer.listOf(
                DataLayer.mapOf(
                        KEY_NAME, product.name,
                        KEY_ID, product.id?.toString() ?: "",
                        KEY_PRICE, getItemPriceInNumber(product.price),
                        KEY_BRAND, "",
                        KEY_CATEGORY, "${product.categoryBreadcrumb}/${product.category}",
                        KEY_VARIANT, "",
                        KEY_LIST, productListName,
                        KEY_POSITION, product.adapter_position + 1
                )))))
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun eventClickWishList(productId: String, isWishListed: Boolean, isTopAds: Boolean) {
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
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun eventClickViewAll(destinationUrl: String) {
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_VIEW_ALL_ACTION_VALUE,
                KEY_EVENT_LABEL, destinationUrl
        )
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun eventClickFilter() {
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_FILTER_ACTION_VALUE,
                KEY_EVENT_LABEL, ""
        )
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun eventClickSort(sortValue: String) {
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_SORT_ACTION_VALUE,
                KEY_EVENT_LABEL, sortValue
        )
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun eventClickViewType() {
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_VIEW_TYPE_ACTION_VALUE,
                KEY_EVENT_LABEL, ""
        )
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun eventClickRelatedSearch() {
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_RELATED_SEARCH_ACTION_VALUE,
                KEY_EVENT_LABEL, ""
        )
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun eventClickPriceList() {
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_PRICE_LIST_ACTION_VALUE,
                KEY_EVENT_LABEL, ""
        )
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun eventClickBackButton() {
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_BACK_BUTTON_ACTION_VALUE,
                KEY_EVENT_LABEL, ""
        )
        getTracker().sendEnhanceEcommerceEvent(map)
    }

    fun eventClickSearchKeyword(keyword: String) {
        val map = DataLayer.mapOf(
                KEY_EVENT, EVENT_CLICK_FIND_VALUE,
                KEY_EVENT_CATEGORY, EVENT_FIND_VALUE,
                KEY_EVENT_ACTION, EVENT_SEARCH_KEYWORD_ACTION_VALUE,
                KEY_EVENT_LABEL, keyword
        )
        getTracker().sendEnhanceEcommerceEvent(map)
    }
}
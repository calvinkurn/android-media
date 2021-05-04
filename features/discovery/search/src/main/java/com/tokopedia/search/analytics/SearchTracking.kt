package com.tokopedia.search.analytics

import android.content.Context
import android.text.TextUtils
import com.tokopedia.analytic_constant.Event.Companion.ADDTOCART
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.iris.util.KEY_SESSION_IRIS
import com.tokopedia.search.analytics.SearchEventTracking
import com.tokopedia.search.analytics.SearchEventTracking.ECommerce.Companion.CLICK
import com.tokopedia.search.analytics.SearchEventTracking.ECommerce.Companion.CURRENCY_CODE
import com.tokopedia.search.analytics.SearchEventTracking.ECommerce.Companion.IDR
import com.tokopedia.search.analytics.SearchEventTracking.ECommerce.Companion.IMPRESSIONS
import com.tokopedia.search.analytics.SearchEventTracking.ECommerce.Companion.PRODUCTS
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import org.json.JSONArray
import java.util.*

/**
 * Created by henrypriyono on 1/5/18.
 */
object SearchTracking {
    private const val ACTION_FIELD = "/searchproduct - %s"
    private const val ORGANIC = "organic"
    private const val ORGANIC_ADS = "organic ads"
    private const val ECOMMERCE = "ecommerce"
    private const val EVENT_CATEGORY_EMPTY_SEARCH = "empty search"
    private const val EVENT_CATEGORY_SEARCH_RESULT = "search result"
    private const val PROMO_CLICK = "promoClick"
    private const val PROMOTIONS = "promotions"
    const val EVENT_CLICK_SEARCH_RESULT = "clickSearchResult"
    private const val EVENT_ACTION_CLICK_NEW_SEARCH = "click - lakukan pencarian baru"
    private const val PROMO_VIEW = "promoView"
    private const val EVENT_ACTION_CLICK_SEE_ALL_NAV_WIDGET = "click - lihat semua widget"
    private const val EVENT_ACTION_IMPRESSION_WIDGET_DIGITAL_PRODUCT = "impression widget - digital product"

    @JvmStatic
    fun screenTrackSearchSectionFragment(screen: String?) {
        if (TextUtils.isEmpty(screen)) {
            return
        }
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screen)
    }

    @JvmStatic
    fun eventSearchResultSort(screenName: String, sortByValue: String?, userId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        SearchTrackingConstant.EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SORT_BY,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.SORT_BY + " - " + screenName,
                        SearchTrackingConstant.EVENT_LABEL, sortByValue,
                        SearchTrackingConstant.USER_ID, userId
                )
        )
    }

    @JvmStatic
    fun eventAppsFlyerViewListingSearch(productsId: JSONArray, keyword: String, prodIds: ArrayList<String?>) {
        val listViewEvent: MutableMap<String, Any> = HashMap()
        listViewEvent["af_content_id"] = prodIds
        listViewEvent["af_currency"] = "IDR"
        listViewEvent["af_content_type"] = "product"
        listViewEvent["af_search_string"] = keyword
        if (productsId.length() > 0) {
            listViewEvent["af_success"] = "success"
        } else {
            listViewEvent["af_success"] = "fail"
        }
        TrackApp.getInstance().appsFlyer.sendTrackEvent("af_search", listViewEvent)
    }

    @JvmStatic
    fun trackEventClickQuickFilter(filterName: String, filterValue: String, isSelected: Boolean, userId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        SearchTrackingConstant.EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.FILTER_PRODUCT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.QUICK_FILTER,
                        SearchTrackingConstant.EVENT_LABEL, "$filterName - $filterValue - $isSelected",
                        SearchTrackingConstant.USER_ID, userId
                )
        )
    }

    fun trackImpressionSearchResultShop(shopItemList: List<Any>, keyword: String?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_VIEW,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_SHOP,
                        SearchTrackingConstant.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                        "promotions", DataLayer.listOf(*shopItemList.toTypedArray())
                )
                )
                )
        )
    }

    fun eventSearchResultShopItemClick(shopItem: Any?, shopId: String, keyword: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_SHOP,
                        SearchTrackingConstant.EVENT_LABEL, "$shopId - $keyword",
                        ECOMMERCE, DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                        "promotions", DataLayer.listOf(shopItem)
                )
                )
                )
        )
    }

    fun eventImpressionSearchResultShopProductPreview(shopItemProductList: List<Any>, keyword: String?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_PRODUCT_SHOP_TAB,
                        SearchTrackingConstant.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                        *shopItemProductList.toTypedArray()
                ))
                )
        )
    }

    fun eventSearchResultShopProductPreviewClick(shopItemProduct: Any?, keyword: String?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_PRODUCT_SHOP_TAB,
                        SearchTrackingConstant.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf("list", "/searchproduct - shop productlist"),
                        "products", DataLayer.listOf(shopItemProduct)
                )
                )
                )
        )
    }

    fun eventSearchResultShopItemClosedClick(shopItemClosed: Any?, shopId: String, keyword: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_SHOP_INACTIVE,
                        SearchTrackingConstant.EVENT_LABEL, "$shopId - $keyword",
                        ECOMMERCE, DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                        "promotions", DataLayer.listOf(shopItemClosed)
                )
                )
                )
        )
    }

    @JvmStatic
    fun trackEventClickSearchResultProduct(item: Any?,
                                           isOrganicAds: Boolean,
                                           eventLabel: String?,
                                           filterSortParams: String?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf("event", "productClick",
                        "eventCategory", "search result",
                        "eventAction", "click - product",
                        "eventLabel", eventLabel,
                        "ecommerce", DataLayer.mapOf("click",
                        DataLayer.mapOf("actionField",
                                DataLayer.mapOf("list", getActionFieldString(isOrganicAds)),
                                "products", DataLayer.listOf(item)
                        )
                ),
                        "searchFilter", filterSortParams
                )
        )
    }

    private fun getActionFieldString(isOrganicAds: Boolean): String {
        val organicStatus = if (isOrganicAds) ORGANIC_ADS else ORGANIC
        return String.format(ACTION_FIELD, organicStatus)
    }

    @JvmStatic
    fun eventImpressionSearchResultProduct(trackingQueue: TrackingQueue,
                                           list: List<Any>,
                                           eventLabel: String?,
                                           irisSessionId: String) {
        val map = DataLayer.mapOf("event", "productView",
                "eventCategory", "search result",
                "eventAction", "impression - product",
                "eventLabel", eventLabel,
                "ecommerce", DataLayer.mapOf(
                "currencyCode", "IDR",
                "impressions", DataLayer.listOf(
                *list.toTypedArray()
        ))
        ) as HashMap<String, Any>
        if (!TextUtils.isEmpty(irisSessionId)) map[KEY_SESSION_IRIS] = irisSessionId
        trackingQueue.putEETracking(
                map
        )
    }

    @JvmStatic
    fun eventClickSuggestedSearch(currentKeyword: String?, suggestion: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.CLICK_FUZZY_KEYWORDS_SUGGESTION, String.format("%s - %s", currentKeyword, suggestion)))
    }

    @JvmStatic
    fun eventSearchResultChangeGrid(gridName: String, screenName: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.GRID_MENU,
                SearchEventTracking.Action.CLICK_CHANGE_GRID + gridName,
                screenName
        ))
    }

    @JvmStatic
    fun eventSearchResultTabClick(context: Context?, tabTitle: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.SEARCH_TAB,
                SearchEventTracking.Action.CLICK_TAB,
                tabTitle
        ))
    }

    private fun generateFilterEventLabel(selectedFilter: Map<String, String>?): String {
        if (selectedFilter == null) {
            return ""
        }
        val filterList: MutableList<String?> = ArrayList()
        for ((key, value) in selectedFilter) {
            filterList.add("$key=$value")
        }
        return TextUtils.join("&", filterList)
    }

    fun eventSearchNoResult(context: Context?,
                            keyword: String?, screenName: String?,
                            selectedFilter: Map<String, String>?) {
        eventSearchNoResult(keyword, screenName, selectedFilter, "", "", "")
    }

    @JvmStatic
    fun eventSearchNoResult(keyword: String?, screenName: String?,
                            selectedFilter: Map<String, String>?,
                            alternativeKeyword: String?,
                            resultCode: String?,
                            keywordProcess: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                SearchEventTracking.Event.EVENT_VIEW_SEARCH_RESULT,
                SearchEventTracking.Category.EVENT_TOP_NAV,
                String.format(
                        SearchEventTracking.Action.NO_SEARCH_RESULT_WITH_TAB,
                        screenName
                ),
                String.format("keyword: %s - type: %s - alternative: %s - param: %s - treatment: %s",
                        keyword,
                        if (!TextUtils.isEmpty(resultCode)) resultCode else "none/other",
                        if (!TextUtils.isEmpty(alternativeKeyword)) alternativeKeyword else "none/other",
                        generateFilterEventLabel(selectedFilter),
                        if (!TextUtils.isEmpty(keywordProcess)) keywordProcess else "none / other"
                )
        )
    }

    fun eventUserClickNewSearchOnEmptySearch(context: Context?, screenName: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENT_CATEGORY_EMPTY_SEARCH,
                EVENT_ACTION_CLICK_NEW_SEARCH, String.format("tab: %s", screenName))
    }

    @JvmStatic
    fun eventUserClickNewSearchOnEmptySearchProduct(keyword: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENT_CATEGORY_SEARCH_RESULT,
                SearchEventTracking.Action.CLICK_CHANGE_KEYWORD,
                keyword
        )
    }

    @JvmStatic
    fun eventUserClickSeeAllGlobalNavWidget(keyword: String,
                                            productName: String,
                                            applink: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENT_CATEGORY_SEARCH_RESULT,
                EVENT_ACTION_CLICK_SEE_ALL_NAV_WIDGET,
                generateEventLabelGlobalNav(keyword, productName, applink)
        )
    }

    @JvmStatic
    fun trackEventClickGlobalNavWidgetItem(item: Any?,
                                           keyword: String,
                                           productName: String,
                                           applink: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK,
                        SearchTrackingConstant.EVENT_LABEL, generateEventLabelGlobalNav(keyword, productName, applink),
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, DataLayer.listOf(item))))
        )
    }

    private fun generateEventLabelGlobalNav(keyword: String, productName: String, applink: String): String {
        return String.format("keyword: %s - product: %s - applink: %s", keyword, productName, applink)
    }

    @JvmStatic
    fun trackEventImpressionGlobalNavWidgetItem(trackingQueue: TrackingQueue,
                                                list: List<Any>,
                                                keyword: String?) {
        trackingQueue.putEETracking(
                DataLayer.mapOf(
                        SearchTrackingConstant.EVENT, PROMO_VIEW,
                        SearchTrackingConstant.EVENT_CATEGORY, EVENT_CATEGORY_SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, EVENT_ACTION_IMPRESSION_WIDGET_DIGITAL_PRODUCT,
                        SearchTrackingConstant.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS, DataLayer.listOf(*list.toTypedArray())))
                ) as HashMap<String, Any>
        )
    }

    @JvmStatic
    fun eventSuccessWishlistSearchResultProduct(wishlistTrackingModel: WishlistTrackingModel) {
        val eventTrackingMap: MutableMap<String, Any> = HashMap()
        eventTrackingMap[SearchTrackingConstant.EVENT] = SearchEventTracking.Event.CLICK_WISHLIST
        eventTrackingMap[SearchTrackingConstant.EVENT_CATEGORY] = SearchEventTracking.Category.SEARCH_RESULT.toLowerCase()
        eventTrackingMap[SearchTrackingConstant.EVENT_ACTION] = generateWishlistClickEventAction(wishlistTrackingModel.isAddWishlist, wishlistTrackingModel.isUserLoggedIn)
        eventTrackingMap[SearchTrackingConstant.EVENT_LABEL] = generateWishlistClickEventLabel(wishlistTrackingModel.productId, wishlistTrackingModel.isTopAds, wishlistTrackingModel.keyword)
        TrackApp.getInstance().gtm.sendGeneralEvent(eventTrackingMap)
    }

    private fun generateWishlistClickEventAction(isAddWishlist: Boolean, isLoggedIn: Boolean): String {
        return (getAddOrRemoveWishlistAction(isAddWishlist)
                + " - "
                + SearchEventTracking.Action.MODULE
                + " - "
                + getIsLoggedInWishlistAction(isLoggedIn))
    }

    private fun getAddOrRemoveWishlistAction(isAddWishlist: Boolean): String {
        return if (isAddWishlist) SearchEventTracking.Action.ADD_WISHLIST else SearchEventTracking.Action.REMOVE_WISHLIST
    }

    private fun getIsLoggedInWishlistAction(isLoggedIn: Boolean): String {
        return if (isLoggedIn) SearchEventTracking.Action.LOGIN else SearchEventTracking.Action.NON_LOGIN
    }

    private fun generateWishlistClickEventLabel(productId: String, isTopAds: Boolean, keyword: String): String {
        return (productId
                + " - "
                + getTopAdsOrGeneralLabel(isTopAds)
                + " - "
                + keyword)
    }

    private fun getTopAdsOrGeneralLabel(isTopAds: Boolean): String {
        return if (isTopAds) SearchEventTracking.Label.TOPADS else SearchEventTracking.Label.GENERAL
    }

    @JvmStatic
    fun eventActionClickCartButton(keyword: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                SearchEventTracking.Event.CLICK_TOP_NAV,
                SearchEventTracking.Category.TOP_NAV_SEARCH_RESULT_PAGE,
                SearchEventTracking.Action.CLICK_CART_BUTTON_SEARCH_RESULT,
                keyword
        )
    }

    @JvmStatic
    fun eventActionClickHomeButton(keyword: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                SearchEventTracking.Event.CLICK_TOP_NAV,
                SearchEventTracking.Category.TOP_NAV_SEARCH_RESULT_PAGE,
                SearchEventTracking.Action.CLICK_HOME_BUTTON_SEARCH_RESULT,
                keyword
        )
    }

    @JvmStatic
    fun trackEventProductLongPress(keyword: String?, productId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.LONG_PRESS_PRODUCT, String.format(SearchEventTracking.Label.KEYWORD_PRODUCT_ID, keyword, productId))
    }

    @JvmStatic
    fun trackEventClickSearchBar(keyword: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                SearchEventTracking.Event.CLICK_TOP_NAV,
                SearchEventTracking.Category.EVENT_TOP_NAV_SEARCH_SRP,
                SearchEventTracking.Action.CLICK_SEARCH_BOX,
                keyword)
    }

    @JvmStatic
    fun trackEventImpressionBannedProductsEmptySearch(keyword: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                SearchEventTracking.Event.VIEW_SEARCH_RESULT_IRIS,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.IMPRESSION_BANNED_PRODUCT_TICKER_EMPTY,
                keyword
        )
    }

    fun trackEventClickGoToBrowserBannedProductsEmptySearch(keyword: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                SearchEventTracking.Event.CLICK_SEARCH_RESULT_IRIS,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.CLICK_BANNED_PRODUCT_TICKER_EMPTY,
                keyword
        )
    }

    @JvmStatic
    fun trackEventImpressionBannedProductsWithResult(keyword: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                SearchEventTracking.Event.VIEW_SEARCH_RESULT_IRIS,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.IMPRESSION_BANNED_PRODUCT_TICKER_RELATED,
                keyword
        )
    }

    @JvmStatic
    fun trackEventImpressionTicker(keyword: String, typeId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                SearchEventTracking.Event.VIEW_SEARCH_RESULT_IRIS,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.IMPRESSION_TICKER,
                "$typeId - $keyword"
        )
    }

    @JvmStatic
    fun trackEventClickTicker(keyword: String, typeId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.CLICK_TICKER,
                "$typeId - $keyword"
        )
    }

    @JvmStatic
    fun trackMoEngageSearchAttempt(query: String?, hasProductList: Boolean, category: HashMap<String?, String?>?) {
        val value = DataLayer.mapOf(
                SearchEventTracking.MOENGAGE.KEYWORD, query,
                SearchEventTracking.MOENGAGE.IS_RESULT_FOUND, hasProductList
        )
        if (category != null) {
            value[SearchEventTracking.MOENGAGE.CATEGORY_ID_MAPPING] = JSONArray(Arrays.asList<Any?>(*category.keys.toTypedArray()))
            value[SearchEventTracking.MOENGAGE.CATEGORY_NAME_MAPPING] = JSONArray(category.values)
        }
        TrackApp.getInstance().moEngage.sendTrackEvent(value, SearchEventTracking.EventMoEngage.SEARCH_ATTEMPT)
    }

    @JvmStatic
    fun trackGTMEventSearchAttempt(generalSearchTrackingModel: GeneralSearchTrackingModel) {
        val value = DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.CLICK_SEARCH,
                SearchTrackingConstant.EVENT_CATEGORY, generalSearchTrackingModel.eventCategory,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.GENERAL_SEARCH,
                SearchTrackingConstant.EVENT_LABEL, generalSearchTrackingModel.eventLabel,
                SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                SearchTrackingConstant.USER_ID, generalSearchTrackingModel.userId,
                SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
                SearchTrackingConstant.IS_RESULT_FOUND, generalSearchTrackingModel.isResultFound,
                SearchTrackingConstant.CATEGORY_ID_MAPPING, generalSearchTrackingModel.categoryIdMapping,
                SearchTrackingConstant.CATEGORY_NAME_MAPPING, generalSearchTrackingModel.categoryNameMapping,
                SearchTrackingConstant.RELATED_KEYWORD, generalSearchTrackingModel.relatedKeyword
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(value)
    }

    fun trackEventImpressionShopRecommendation(shopItem: List<Any>, keyword: String?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_VIEW,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_SHOP_ALTERNATIVE,
                        SearchTrackingConstant.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                        "promotions", DataLayer.listOf(*shopItem.toTypedArray())
                )
                )
                )
        )
    }

    fun trackEventClickShopRecommendation(shopItem: Any?, shopId: String, keyword: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_SHOP_ALTERNATIVE,
                        SearchTrackingConstant.EVENT_LABEL, "$shopId - $keyword",
                        ECOMMERCE, DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                        "promotions", DataLayer.listOf(shopItem)
                )
                )
                )
        )
    }

    fun trackEventImpressionShopRecommendationProductPreview(shopItemProductList: List<Any>, keyword: String?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_PRODUCT_SHOP_TAB_ALTERNATIVE,
                        SearchTrackingConstant.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                        *shopItemProductList.toTypedArray()
                ))
                )
        )
    }

    fun trackEventClickShopRecommendationProductPreview(shopItemProduct: Any?, keyword: String?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_PRODUCT_SHOP_TAB_ALTERNATIVE,
                        SearchTrackingConstant.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf("list", "/notifcenter"),
                        "products", DataLayer.listOf(shopItemProduct)
                )
                )
                )
        )
    }

    @JvmStatic
    fun trackImpressionInspirationCarouselList(trackingQueue: TrackingQueue, type: String, keyword: String, list: List<Any>) {
        val map = DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_INSPIRATION_CAROUSEL_PRODUCT,
                SearchTrackingConstant.EVENT_LABEL, "$type - $keyword",
                ECOMMERCE, DataLayer.mapOf(
                "currencyCode", "IDR",
                "impressions", DataLayer.listOf(
                *list.toTypedArray()
        )
        )
        ) as HashMap<String, Any>
        trackingQueue.putEETracking(
                map
        )
    }

    @JvmStatic
    fun trackImpressionInspirationCarouselInfo(trackingQueue: TrackingQueue, type: String, keyword: String, list: List<Any>) {
        val map = DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_VIEW,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_INSPIRATION_CAROUSEL_INFO_PRODUCT,
                SearchTrackingConstant.EVENT_LABEL, "$type - $keyword",
                ECOMMERCE, DataLayer.mapOf(
                SearchEventTracking.Event.PROMO_VIEW, DataLayer.mapOf(
                "promotions", DataLayer.listOf(
                *list.toTypedArray()
        )
        )
        )
        ) as HashMap<String, Any>
        trackingQueue.putEETracking(
                map
        )
    }

    @JvmStatic
    fun trackEventClickInspirationCarouselOptionSeeAll(type: String, keywordBefore: String, keywordAfter: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_INSPIRATION_CAROUSEL_SEARCH,
                        SearchTrackingConstant.EVENT_LABEL, "$type - $keywordBefore - $keywordAfter"
                )
        )
    }

    @JvmStatic
    fun trackEventClickInspirationCarouselListProduct(type: String,
                                                      keyword: String,
                                                      products: List<Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_INSPIRATION_CAROUSEL_PRODUCT,
                        SearchTrackingConstant.EVENT_LABEL, "$type - $keyword",
                        ECOMMERCE, DataLayer.mapOf("click",
                        DataLayer.mapOf("actionField",
                                DataLayer.mapOf("list", "/search - carousel"),
                                "products", DataLayer.listOf(
                                *products.toTypedArray()
                        )
                        )
                )
                )
        )
    }

    @JvmStatic
    fun trackEventClickInspirationCarouselInfoProduct(type: String,
                                                      keyword: String,
                                                      products: List<Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK,
                        SearchTrackingConstant.EVENT_LABEL, "$type - $keyword",
                        ECOMMERCE, DataLayer.mapOf(
                        SearchEventTracking.Event.PROMO_CLICK, DataLayer.mapOf(
                        "promotions", DataLayer.listOf(
                        *products.toTypedArray()
                )
                )
                )
                )
        )
    }

    @JvmStatic
    fun trackEventClickInspirationCarouselGridBanner(
            type: String, keyword: String, bannerData: Any?, userId: String?
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_INSPIRATION_CAROUSEL_GRID_BANNER,
                        SearchTrackingConstant.EVENT_LABEL, "$type - $keyword",
                        SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                        SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
                        SearchTrackingConstant.USER_ID, userId,
                        ECOMMERCE, DataLayer.mapOf(
                        SearchEventTracking.Event.PROMO_CLICK, DataLayer.mapOf(
                        PROMOTIONS, DataLayer.listOf(bannerData)
                )
                )
                )
        )
    }

    @JvmStatic
    fun trackImpressionInspirationCarouselChips(
            trackingQueue: TrackingQueue,
            type: String,
            keyword: String,
            chipsValue: String,
            userId: String?,
            list: List<Any>
    ) {
        val map = DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_INSPIRATION_CAROUSEL_CHIPS_PRODUCT,
                SearchTrackingConstant.EVENT_LABEL, "$type - $keyword - $chipsValue",
                SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                SearchTrackingConstant.USER_ID, userId,
                SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
                ECOMMERCE, DataLayer.mapOf(
                "currencyCode", "IDR",
                "impressions", DataLayer.listOf(
                *list.toTypedArray()
        )
        )
        ) as HashMap<String, Any>
        trackingQueue.putEETracking(
                map
        )
    }

    @JvmStatic
    fun trackEventClickInspirationCarouselChipsProduct(
            type: String,
            keyword: String,
            chipsValue: String,
            userId: String?,
            products: List<Any>
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_INSPIRATION_CAROUSEL_CHIPS_PRODUCT,
                        SearchTrackingConstant.EVENT_LABEL, "$type - $keyword - $chipsValue",
                        SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                        SearchTrackingConstant.USER_ID, userId,
                        SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
                        ECOMMERCE, DataLayer.mapOf("click",
                        DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "/search - carousel chips"),
                                "products", DataLayer.listOf(
                                *products.toTypedArray()
                        )
                        )
                )
                )
        )
    }

    @JvmStatic
    fun trackEventClickInspirationCarouselChipsSeeAll(type: String, keyword: String, chipsValue: String, userId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_INSPIRATION_CAROUSEL_CHIPS_LIHAT_SEMUA,
                        SearchTrackingConstant.EVENT_LABEL, "$type - $keyword - $chipsValue",
                        SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                        SearchTrackingConstant.USER_ID, userId,
                        SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH
                )
        )
    }

    @JvmStatic
    fun trackEventClickInspirationCarouselChipsVariant(type: String, keyword: String, chipsValue: String, userId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_INSPIRATION_CAROUSEL_CHIPS_VARIANT,
                        SearchTrackingConstant.EVENT_LABEL, "$type - $keyword - $chipsValue",
                        SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                        SearchTrackingConstant.USER_ID, userId,
                        SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH
                )
        )
    }

    @JvmStatic
    fun trackEventImpressionBroadMatch(trackingQueue: TrackingQueue, keyword: String?, alternativeKeyword: String?, userId: String?, broadMatchItems: List<Any>) {
        val map = DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_BROAD_MATCH,
                SearchTrackingConstant.EVENT_LABEL, String.format("%s - %s", keyword, alternativeKeyword),
                SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
                SearchTrackingConstant.USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                "currencyCode", "IDR",
                "impressions", DataLayer.listOf(
                *broadMatchItems.toTypedArray()
        ))
        ) as HashMap<String, Any>
        trackingQueue.putEETracking(
                map
        )
    }

    @JvmStatic
    fun trackEventClickBroadMatchSeeMore(keyword: String?, alternativeKeyword: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        SearchTrackingConstant.EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_BROAD_MATCH_LIHAT_SEMUA,
                        SearchTrackingConstant.EVENT_LABEL, String.format("%s - %s", keyword, alternativeKeyword))
        )
    }

    @JvmStatic
    fun trackEventClickBroadMatchItem(keyword: String?, alternativeKeyword: String?, userId: String?, broadMatchItems: List<Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_BROAD_MATCH,
                        SearchTrackingConstant.EVENT_LABEL, String.format("%s - %s", keyword, alternativeKeyword),
                        SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                        SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
                        SearchTrackingConstant.USER_ID, userId,
                        ECOMMERCE, DataLayer.mapOf("click",
                        DataLayer.mapOf("actionField",
                                DataLayer.mapOf("list", "/search - broad match"),
                                "products", DataLayer.listOf(
                                *broadMatchItems.toTypedArray()
                        )
                        )
                )
                )
        )
    }

    @JvmStatic
    fun trackEventClickInspirationCardOption(label: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.CLICK_INSPIRATION_CARD,
                label
        )
    }

    @JvmStatic
    fun trackEventAddToCart(keyword: String?, isOrganicAds: Boolean, productItem: Any?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, ADDTOCART,
                        TrackAppUtils.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        TrackAppUtils.EVENT_ACTION, SearchEventTracking.Action.CLICK_ADD_TO_CART_ON_PRODUCT_OPTIONS,
                        TrackAppUtils.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                            CURRENCY_CODE, IDR,
                            SearchEventTracking.ECommerce.ADD, DataLayer.mapOf(
                                SearchEventTracking.ECommerce.ACTION_FIELD, DataLayer.mapOf(
                                    "list", getActionFieldString(isOrganicAds)
                                ),
                                SearchEventTracking.ECommerce.PRODUCTS, DataLayer.listOf(productItem)
                            )
                        )
                )
        )
    }

    @JvmStatic
    fun trackEventGoToShopPage(keyword: String?, item: Any?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        TrackAppUtils.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        TrackAppUtils.EVENT_ACTION, SearchEventTracking.Action.CLICK,
                        TrackAppUtils.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS, DataLayer.listOf(item)))
                )
        )
    }

    @JvmStatic
    fun trackEventShareProduct(queryKey: String, productId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        TrackAppUtils.EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                        TrackAppUtils.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        TrackAppUtils.EVENT_ACTION, SearchEventTracking.Action.CLICK_SHARE_PRODUCT_OPTIONS,
                        TrackAppUtils.EVENT_LABEL, "$queryKey - $productId"
                )
        )
    }

    @JvmStatic
    fun trackEventImpressionDynamicProductCarousel(
            trackingQueue: TrackingQueue,
            type: String?,
            keyword: String?,
            userId: String?,
            broadMatchItems: List<Any>,
    ) {
        val map = DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_DYNAMIC_PRODUCT_CAROUSEL,
                SearchTrackingConstant.EVENT_LABEL, String.format("%s - %s", type, keyword),
                SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
                SearchTrackingConstant.USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    "currencyCode", "IDR",
                    "impressions", DataLayer.listOf(*broadMatchItems.toTypedArray())
                )
        ) as HashMap<String, Any>

        trackingQueue.putEETracking(map)
    }

    @JvmStatic
    fun trackEventClickDynamicProductCarousel(
            type: String?,
            keyword: String?,
            userId: String?,
            broadMatchItems: List<Any>,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_DYNAMIC_PRODUCT_CAROUSEL,
                        SearchTrackingConstant.EVENT_LABEL, String.format("%s - %s", type, keyword),
                        SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                        SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
                        SearchTrackingConstant.USER_ID, userId,
                        ECOMMERCE, DataLayer.mapOf(CLICK,
                            DataLayer.mapOf(
                                    ACTION_FIELD, DataLayer.mapOf("list", "/search - carousel"),
                                    PRODUCTS, DataLayer.listOf(*broadMatchItems.toTypedArray())
                            )
                        )
                )
        )
    }

    @JvmStatic
    fun trackEventClickDynamicProductCarouselSeeMore(type: String?,keyword: String?, alternativeKeyword: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        SearchTrackingConstant.EVENT, SearchEventTracking.Event.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_DYNAMIC_PRODUCT_CAROUSEL_SEE_MORE,
                        SearchTrackingConstant.EVENT_LABEL, String.format("%s - %s - %s", type, keyword, alternativeKeyword),
                        SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                        SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH
                )
        )
    }
}
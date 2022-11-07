package com.tokopedia.search.analytics

import android.text.TextUtils
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.iris.util.KEY_SESSION_IRIS
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.search.utils.joinActiveOptionsToString
import com.tokopedia.search.utils.orNone
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import org.json.JSONArray
import java.util.Locale

/**
 * Created by henrypriyono on 1/5/18.
 */
object SearchTracking {
    private const val ACTION_FIELD = "/searchproduct - %s - %s - component:%s"
    private const val ORGANIC = "organic"
    private const val ORGANIC_ADS = "organic ads"
    private const val ECOMMERCE = "ecommerce"
    private const val EVENT_CATEGORY_EMPTY_SEARCH = "empty search"
    private const val PROMOTIONS = "promotions"
    const val EVENT_CLICK_SEARCH_RESULT = "clickSearchResult"
    private const val EVENT_ACTION_CLICK_NEW_SEARCH = "click - lakukan pencarian baru"
    private const val CAROUSEL_UNIFICATION_LIST_NAME = " /search - carousel %s - component:%s"

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
    fun eventAppsFlyerViewListingSearch(productsId: JSONArray, keyword: String, prodIds: ArrayList<String?>, allProdIds: ArrayList<String?>?) {
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

        //add branch search event
        allProdIds?.let {
            LinkerManager.getInstance().sendEvent(
                LinkerUtils.createGenericRequest(
                    LinkerConstants.EVENT_SEARCH,
                    it
                )
            )
        }
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

    fun trackImpressionSearchResultShop(shopItemList: ArrayList<Any>, keyword: String?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_VIEW,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_SHOP,
                        SearchTrackingConstant.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                        "promotions", shopItemList
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

    fun eventImpressionSearchResultShopProductPreview(shopItemProductList: ArrayList<Any>, keyword: String?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_PRODUCT_SHOP_TAB,
                        SearchTrackingConstant.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", shopItemProductList
                )
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
    fun trackEventClickSearchResultProduct(
        item: Any?,
        eventLabel: String,
        userId: String,
        productAnalyticsData: ProductClickAnalyticsData,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                TrackAppUtils.EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                TrackAppUtils.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                TrackAppUtils.EVENT_ACTION, SearchEventTracking.Action.CLICK_PRODUCT,
                TrackAppUtils.EVENT_LABEL, eventLabel,
                SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                SearchTrackingConstant.USER_ID, userId,
                SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
                ECOMMERCE, DataLayer.mapOf(
                    "click", DataLayer.mapOf(
                        "actionField", DataLayer.mapOf(
                            "list", getActionFieldString(
                                productAnalyticsData.isOrganicAds,
                                productAnalyticsData.topadsTag,
                                productAnalyticsData.componentId
                            )
                        ),
                        "products", DataLayer.listOf(item)
                    )
                ),
                "searchFilter", productAnalyticsData.filterSortParams,
            )
        )
    }

    fun getActionFieldString(
        isOrganicAds: Boolean,
        topadsTag: Int,
        componentId: String,
    ): String {
        val organicStatus = if (isOrganicAds) ORGANIC_ADS else ORGANIC
        return String.format(
            ACTION_FIELD,
            organicStatus,
            topadsTag.toString(),
            componentId,
        )
    }

    @JvmStatic
    fun eventImpressionSearchResultProduct(
            trackingQueue: TrackingQueue,
            list: ArrayList<Any>,
            eventLabel: String?,
            irisSessionId: String,
            userId: String,
    ) {
        val map = DataLayer.mapOf(
            TrackAppUtils.EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
            TrackAppUtils.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
            TrackAppUtils.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_PRODUCT,
            TrackAppUtils.EVENT_LABEL, eventLabel,
            SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
            SearchTrackingConstant.USER_ID, userId,
            SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
            ECOMMERCE, DataLayer.mapOf(
                "currencyCode", "IDR",
                "impressions", list
            ),
        ) as HashMap<String, Any>
        if (!TextUtils.isEmpty(irisSessionId)) map[KEY_SESSION_IRIS] = irisSessionId
        trackingQueue.putEETracking(
                map
        )
    }

    @JvmStatic
    fun eventSearchResultTabClick(tabTitle: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.SEARCH_TAB,
                SearchEventTracking.Action.CLICK_TAB,
                tabTitle
        ))
    }

    fun eventUserClickNewSearchOnEmptySearch(screenName: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                EVENT_CLICK_SEARCH_RESULT,
                EVENT_CATEGORY_EMPTY_SEARCH,
                EVENT_ACTION_CLICK_NEW_SEARCH, String.format(Locale.getDefault(),"tab: %s", screenName))
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
    fun trackEventProductLongPress(keyword: String?, productId: String?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                SearchEventTracking.Event.SEARCH_RESULT,
                SearchEventTracking.Category.SEARCH_RESULT,
                SearchEventTracking.Action.LONG_PRESS_PRODUCT, String.format(SearchEventTracking.Label.KEYWORD_PRODUCT_ID, keyword, productId))
    }

    @JvmStatic
    fun trackMoEngageSearchAttempt(query: String?, hasProductList: Boolean, category: HashMap<String?, String?>?) {
        val value = DataLayer.mapOf(
                SearchEventTracking.MOENGAGE.KEYWORD, query,
                SearchEventTracking.MOENGAGE.IS_RESULT_FOUND, hasProductList
        )
        if (category != null) {
            value[SearchEventTracking.MOENGAGE.CATEGORY_ID_MAPPING] = JSONArray(category.keys)
            value[SearchEventTracking.MOENGAGE.CATEGORY_NAME_MAPPING] = JSONArray(category.values)
        }
        TrackApp.getInstance().moEngage.sendTrackEvent(value, SearchEventTracking.EventMoEngage.SEARCH_ATTEMPT)
    }

    @JvmStatic
    fun trackGTMEventSearchAttempt(generalSearchTrackingModel: GeneralSearchTrackingModel) {
        val value = DataLayer.mapOf(
            SearchTrackingConstant.EVENT, SearchEventTracking.Event.CLICK_TOP_NAV,
            SearchTrackingConstant.EVENT_CATEGORY, generalSearchTrackingModel.eventCategory,
            SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.GENERAL_SEARCH,
            SearchTrackingConstant.EVENT_LABEL, generalSearchTrackingModel.eventLabel,
            SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
            SearchTrackingConstant.USER_ID, generalSearchTrackingModel.userId,
            SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
            SearchTrackingConstant.IS_RESULT_FOUND, generalSearchTrackingModel.isResultFound,
            SearchTrackingConstant.CATEGORY_ID_MAPPING, generalSearchTrackingModel.categoryIdMapping,
            SearchTrackingConstant.CATEGORY_NAME_MAPPING, generalSearchTrackingModel.categoryNameMapping,
            SearchTrackingConstant.RELATED_KEYWORD, generalSearchTrackingModel.relatedKeyword,
            SearchTrackingConstant.PAGE_SOURCE, generalSearchTrackingModel.pageSource,
            SearchTrackingConstant.SEARCHFILTER, generalSearchTrackingModel.searchFilter,
            SearchTrackingConstant.ANDROID_ID, TrackApp.getInstance().appsFlyer.googleAdId,
            SearchComponentTrackingConst.COMPONENT, generalSearchTrackingModel.componentId,
            SearchEventTracking.EXTERNAL_REFERENCE, generalSearchTrackingModel.externalReference.orNone(),
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(value)
    }

    fun trackEventImpressionShopRecommendation(shopItem: ArrayList<Any>, keyword: String?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_VIEW,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_SHOP_ALTERNATIVE,
                        SearchTrackingConstant.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                        "promotions", shopItem
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

    fun trackEventImpressionShopRecommendationProductPreview(shopItemProductList: ArrayList<Any>, keyword: String?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_PRODUCT_SHOP_TAB_ALTERNATIVE,
                        SearchTrackingConstant.EVENT_LABEL, keyword,
                        ECOMMERCE, DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", shopItemProductList)
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
    fun trackImpressionInspirationCarouselInfo(trackingQueue: TrackingQueue, type: String, keyword: String, list: ArrayList<Any>) {
        val map = DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_VIEW,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_INSPIRATION_CAROUSEL_INFO_PRODUCT,
                SearchTrackingConstant.EVENT_LABEL, "$type - $keyword",
                ECOMMERCE, DataLayer.mapOf(
                SearchEventTracking.Event.PROMO_VIEW, DataLayer.mapOf(
                "promotions", list
        )
        )
        ) as HashMap<String, Any>
        trackingQueue.putEETracking(
                map
        )
    }

    @JvmStatic
    fun trackEventClickInspirationCarouselInfoProduct(type: String,
                                                      keyword: String,
                                                      products: ArrayList<Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(SearchTrackingConstant.EVENT, SearchEventTracking.Event.PROMO_CLICK,
                        SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                        SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK,
                        SearchTrackingConstant.EVENT_LABEL, "$type - $keyword",
                        ECOMMERCE, DataLayer.mapOf(
                        SearchEventTracking.Event.PROMO_CLICK, DataLayer.mapOf(
                        "promotions", products
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
    fun trackEventGeneralSearchShop(generalSearchTrackingShop: GeneralSearchTrackingShop) {
        val generalSearchShopDataLayer = DataLayer.mapOf(
            SearchTrackingConstant.EVENT, SearchEventTracking.Event.CLICK_TOP_NAV,
            SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.GENERAL_SEARCH_SHOP,
            SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.EVENT_TOP_NAV,
            SearchTrackingConstant.EVENT_LABEL, generalSearchTrackingShop.eventLabel,
            SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
            SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
            SearchTrackingConstant.PAGE_SOURCE, generalSearchTrackingShop.pageSource,
            SearchTrackingConstant.RELATED_KEYWORD, generalSearchTrackingShop.relatedKeyword,
            SearchTrackingConstant.SEARCHFILTER, generalSearchTrackingShop.searchFilter,
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(generalSearchShopDataLayer)
    }

    fun getInspirationCarouselUnificationListName(type: String, componentId: String): String =
        CAROUSEL_UNIFICATION_LIST_NAME.format(type, componentId)

    fun trackEventImpressionInspirationCarouselUnification(
        trackingQueue: TrackingQueue,
        eventLabel: String,
        products: ArrayList<Any>,
    ) {
        val impressionDataLayer = DataLayer.mapOf(
            SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_VIEW,
            SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
            SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.IMPRESSION_CAROUSEL_PRODUCT,
            SearchTrackingConstant.EVENT_LABEL, eventLabel,
            ECOMMERCE, DataLayer.mapOf(
                "currencyCode", "IDR",
                "impressions", products
            )
        ) as HashMap<String, Any>

        trackingQueue.putEETracking(impressionDataLayer)
    }

    fun trackEventClickInspirationCarouselUnification(
        eventLabel: String,
        type: String,
        componentId: String,
        products: ArrayList<Any>,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.PRODUCT_CLICK,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_CAROUSEL_PRODUCT,
                SearchTrackingConstant.EVENT_LABEL, eventLabel,
                ECOMMERCE, DataLayer.mapOf("click",
                    DataLayer.mapOf(
                        "actionField", DataLayer.mapOf(
                            "list", getInspirationCarouselUnificationListName(type, componentId)
                        ),
                        "products", products
                    )
                )
            )
        )
    }

    fun trackEventClickDropdownQuickFilter(filterTitle: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.CLICK_SEARCH,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT_PAGE,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.CLICK_DROPDOWN_QUICK_FILTER,
                SearchTrackingConstant.EVENT_LABEL, "$filterTitle",
                SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
            )
        )
    }

    fun trackEventApplyDropdownQuickFilter(optionList: List<Option>?) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                SearchTrackingConstant.EVENT, SearchEventTracking.Event.CLICK_SEARCH,
                SearchTrackingConstant.EVENT_CATEGORY, SearchEventTracking.Category.SEARCH_RESULT_PAGE,
                SearchTrackingConstant.EVENT_ACTION, SearchEventTracking.Action.APPLY_DROPDOWN_QUICK_FILTER,
                SearchTrackingConstant.EVENT_LABEL, optionList?.joinActiveOptionsToString(),
                SearchEventTracking.CURRENT_SITE, SearchEventTracking.TOKOPEDIA_MARKETPLACE,
                SearchEventTracking.BUSINESS_UNIT, SearchEventTracking.SEARCH,
            )
        )
    }
}

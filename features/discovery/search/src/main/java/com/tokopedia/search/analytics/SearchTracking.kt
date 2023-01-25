package com.tokopedia.search.analytics

import android.text.TextUtils
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery.common.analytics.SearchComponentTrackingConst
import com.tokopedia.iris.util.KEY_SESSION_IRIS
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.search.utils.orNone
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import org.json.JSONArray
import java.util.*

/**
 * Created by henrypriyono on 1/5/18.
 */
object SearchTracking {
    private const val ACTION_FIELD = "/searchproduct - %s - %s - component:%s"
    private const val ORGANIC = "organic"
    private const val ORGANIC_ADS = "organic ads"
    private const val ECOMMERCE = "ecommerce"
    private const val EVENT_CATEGORY_EMPTY_SEARCH = "empty search"
    const val EVENT_CLICK_SEARCH_RESULT = "clickSearchResult"
    private const val EVENT_ACTION_CLICK_NEW_SEARCH = "click - lakukan pencarian baru"

    @JvmStatic
    fun screenTrackSearchSectionFragment(screen: String?) {
        if (TextUtils.isEmpty(screen)) {
            return
        }
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screen)
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
}

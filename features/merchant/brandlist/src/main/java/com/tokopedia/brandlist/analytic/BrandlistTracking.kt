package com.tokopedia.brandlist.analytic

import android.content.Context
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue

class BrandlistTracking(context: Context) {

    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }
    private var trackingQueue = TrackingQueue(context)

    companion object{
        private const val EVENT = "event"
        private const val EVENT_CATEGORY = "eventCategory"
        private const val EVENT_ACTION = "eventAction"
        private const val EVENT_LABEL = "eventLabel"

        private const val ECOMMERCE = "ecommerce"
        private const val CLICK = "click"
        private const val IMPRESSION = "impression"

        private const val PROMO_CLICK = "promoClick"
        private const val PROMO_VIEW = "promoView"
        private const val PROMOTIONS = "promotions"
        private const val OPEN_SCREEN = "openScreen"

        private const val EVENT_VALUE = "clickOSAllBrands"
        private const val EVENT_CATEGORY_VALUE = "official store all brands page"
    }


    fun sendScreen(categoryName: String) {
        val screenName = "/official-store/brand/$categoryName"
        val customDimension = java.util.HashMap<String, String>()
        customDimension["event"] = OPEN_SCREEN
        customDimension["screenName"] = "/official-store/brand/$categoryName"
        tracker.sendScreenAuthenticated(screenName, customDimension)
    }


    fun clickSearchBox(categoryTab: String, keyword: String, isThereAnySearchResult: Boolean) {
        val isSearchResult = if (isThereAnySearchResult) "search result" else "no search result"
        tracker.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        EVENT_VALUE,
                        "$EVENT_CATEGORY_VALUE - $categoryTab",
                        "$CLICK search - $isSearchResult",
                        keyword
                )
        )
    }

    fun clickBrandOnSearchBox(categoryTab: String, optionalParam: String, isLogin: Boolean, keyword: String, shopId: String) {
        val statusLogin =  if (isLogin) "login" else "non login"
        tracker.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        EVENT_VALUE,
                        "$EVENT_CATEGORY_VALUE - $categoryTab",
                        "$CLICK - shop - $optionalParam - $statusLogin",
                        "$shopId - $keyword"
                )
        )
    }

    fun clickCategory(categoryTabSelected: String, currentCategoryTab: String) {
        tracker.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        EVENT_VALUE,
                        "$EVENT_CATEGORY_VALUE - $currentCategoryTab",
                        "$CLICK all brands page category tab",
                        categoryTabSelected
                )
        )
    }

    fun clickBrandPilihan(shopId: String, isLogin: Boolean, shopName: String,
                          imgUrl: String, shoplogoPosition: String, categoryTabName: String) {
        val statusLogin =  if (isLogin) "login" else "non login"
        val data = DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - $categoryTabName",
                EVENT_ACTION, "$CLICK - shop - brand of choice list - $statusLogin",
                EVENT_LABEL, shopId,
                ECOMMERCE, DataLayer.mapOf(
                    PROMO_CLICK, DataLayer.mapOf(
                        PROMOTIONS, DataLayer.listOf(
                            DataLayer.mapOf(
                                    "id", shopId,
                                    "name", "/officialstore/brand/$categoryTabName - brand of choice list",
                                    "position", shoplogoPosition,
                                    "creative", shopName,
                                    "creative_url", imgUrl
                            )
                        )
                    )
                )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun impressionBrandPilihan(isLogin: Boolean, categoryName: String, shopId: String, shoplogoPosition: String,
                               imgUrl: String, shopName: String) {
        val statusLogin =  if (isLogin) "login" else "non login"
        val data = DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - $categoryName",
                EVENT_ACTION, "$IMPRESSION - shop - brand of choice list - $statusLogin",
                EVENT_LABEL, "shop impression",
                ECOMMERCE, DataLayer.mapOf(
                    PROMO_VIEW, DataLayer.mapOf(
                            PROMOTIONS, DataLayer.listOf(
                                DataLayer.mapOf(
                                        "id", shopId,
                                        "name", "/officialstore/brand/$categoryName - brand of choice list",
                                        "position", shoplogoPosition,
                                        "creative", shopName,
                                        "creative_url", imgUrl
                                )
                            )
                    )
                )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun clickLihatSemua(isLogin: Boolean, categoryTabName: String) {
        val statusLogin =  if (isLogin) "login" else "non login"
        tracker.sendGeneralEvent(
                TrackAppUtils.gtmData(
                        EVENT_VALUE,
                        "$EVENT_CATEGORY_VALUE - $categoryTabName",
                        "$CLICK - brand pilihan - view all",
                        statusLogin
                )
        )
    }

    fun clickBrandPopular(categoryTabName: String, shopId: String, shopLogoPosition: String, shopName: String, imgUrl: String, isLogin: Boolean) {
        val statusLogin =  if (isLogin) "login" else "non login"
        val data = DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - $categoryTabName",
                EVENT_ACTION, "$CLICK - shop - popular brand list - $statusLogin",
                EVENT_LABEL, shopId,
                ECOMMERCE, DataLayer.mapOf(
                    PROMO_CLICK, DataLayer.mapOf(
                        PROMOTIONS, DataLayer.listOf(
                            DataLayer.mapOf(
                                    "id", shopId,
                                    "name", "/officialstore/brand/$categoryTabName - popular brand list",
                                    "position", shopLogoPosition,
                                    "creative", shopName,
                                    "creative_url", imgUrl
                            )
                        )
                    )
                )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun impressionBrandPopular(isLogin: Boolean, shopId: String, categoryTabName: String, shoplogoPosition: String,
                               shopName: String, imgUrl: String) {
        val statusLogin =  if (isLogin) "login" else "non login"
        val data = DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - $categoryTabName",
                EVENT_ACTION, "$IMPRESSION - shop - popular brand list - $statusLogin",
                EVENT_LABEL, "shop impression",
                ECOMMERCE, DataLayer.mapOf(
                    PROMO_VIEW, DataLayer.mapOf(
                        PROMOTIONS, DataLayer.listOf(
                            DataLayer.mapOf(
                                    "id", shopId,
                                    "name", "/officialstore/brand/$categoryTabName - popular brand list",
                                    "position", shoplogoPosition,
                                    "creative", shopName,
                                    "creative_url", imgUrl
                            )
                        )
                    )
                )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun clickBrandBaruTokopedia(isLogin: Boolean, shopId: String, categoryTabName: String, shoplogoPosition: String,
                                shopName: String, imgUrl: String) {
        val statusLogin =  if (isLogin) "login" else "non login"
        val data = DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - $categoryTabName",
                EVENT_ACTION, "$CLICK - shop - new brand list - $statusLogin",
                EVENT_LABEL, shopId,
                ECOMMERCE, DataLayer.mapOf(
                    PROMO_CLICK, DataLayer.mapOf(
                        PROMOTIONS, DataLayer.listOf(
                            DataLayer.mapOf(
                                    "id", shopId,
                                    "name", "/officialstore/brand/$categoryTabName - new brand list",
                                    "position", shoplogoPosition,
                                    "creative", shopName,
                                    "creative_url", imgUrl
                            )
                        )
                    )
                )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun impressionBrandBaru(isLogin: Boolean, shopId: String, categoryTabName: String, shoplogoPosition: String,
                            shopName: String, imgUrl: String) {
        val statusLogin =  if (isLogin) "login" else "non login"
        val data = DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - $categoryTabName",
                EVENT_ACTION, "$IMPRESSION - shop - new brand list - $statusLogin",
                EVENT_LABEL, "shop impression",
                ECOMMERCE, DataLayer.mapOf(
                    PROMO_VIEW, DataLayer.mapOf(
                        PROMOTIONS, DataLayer.listOf(
                            DataLayer.mapOf(
                                    "id", shopId,
                                    "name", "/officialstore/brand/$categoryTabName - new brand list",
                                    "position", shoplogoPosition,
                                    "creative", shopName,
                                    "creative_url", imgUrl
                            )
                        )
                    )
                )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun clickBrand(isLogin: Boolean, shopId: String, categoryTabName: String, shoplogoPosition: String,
                   shopName: String, imgUrl: String, isSearch: Boolean, keyword: String) {
        val statusLogin =  if (isLogin) "login" else "non login"
        val statusSearch = if (isSearch) "search result" else "not search result"
        val keywordValue = if (keyword.isEmpty()) "NaN" else keyword
        val data = DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - $categoryTabName",
                EVENT_ACTION, "$CLICK - shop - all brand list - $statusSearch - $statusLogin",
                EVENT_LABEL, "$shopId - $statusSearch - $keyword",
                ECOMMERCE, DataLayer.mapOf(
                    PROMO_CLICK, DataLayer.mapOf(
                        PROMOTIONS, DataLayer.listOf(
                            DataLayer.mapOf(
                                    "id", shopId,
                                    "name", "/officialstore/brand/$categoryTabName - $statusSearch - $keywordValue - all brand list",
                                    "position", shoplogoPosition,
                                    "creative", shopName,
                                    "creative_url", imgUrl
                            )
                        )
                    )
                )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun impressionBrand(isLogin: Boolean, shopId: String, categoryTabName: String, shoplogoPosition: String,
                        shopName: String, imgUrl: String, isSearch: Boolean, keyword: String) {
        val statusLogin =  if (isLogin) "login" else "non login"
        val statusSearch = if (isSearch) "search result" else "not search result"
        val keywordValue = if (keyword.isEmpty()) "NaN" else keyword
        val data = DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, "$EVENT_CATEGORY_VALUE - $categoryTabName",
                EVENT_ACTION, "$IMPRESSION - shop - all brand list - $statusLogin",
                EVENT_LABEL, "$statusSearch - $keyword",
                ECOMMERCE, DataLayer.mapOf(
                    PROMO_VIEW, DataLayer.mapOf(
                        PROMOTIONS, DataLayer.listOf(
                            DataLayer.mapOf(
                                    "id", shopId,
                                    "name", "/officialstore/brand/$categoryTabName - $statusSearch - $keywordValue - all brand list",
                                    "position", shoplogoPosition,
                                    "creative", shopName,
                                    "creative_url", imgUrl
                            )
                        )
                    )
                )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun sendAll(){
        trackingQueue.sendAll()
    }
}
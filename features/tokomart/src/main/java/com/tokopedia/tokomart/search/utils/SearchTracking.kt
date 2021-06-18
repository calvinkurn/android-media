package com.tokopedia.tokomart.search.utils

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_ADD_QUANTITY
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_APPLY_FILTER
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_BANNER
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_CATEGORY_FILTER
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_CHOOSE_VARIANT_ON_PRODUCT_CARD
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_FILTER_OPTION
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_FUZZY_KEYWORDS_REPLACE
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_PRODUCT
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_REMOVE_QUANTITY
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.CLICK_TAMBAH_KE_KERANJANG
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.IMPRESSION_BANNER
import com.tokopedia.tokomart.search.utils.SearchTracking.Action.IMPRESSION_PRODUCT
import com.tokopedia.tokomart.search.utils.SearchTracking.Category.TOKONOW_SEARCH_RESULT
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.ACTION_FIELD
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.ADD
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.CLICK
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.CURRENCYCODE
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.ECOMMERCE
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.IDR
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.IMPRESSIONS
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.LIST
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.PRODUCTS
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.PROMOTIONS
import com.tokopedia.tokomart.search.utils.SearchTracking.ECommerce.PROMO_CLICK
import com.tokopedia.tokomart.search.utils.SearchTracking.Event.ADD_TO_CART
import com.tokopedia.tokomart.search.utils.SearchTracking.Event.PRODUCT_CLICK
import com.tokopedia.tokomart.search.utils.SearchTracking.Event.PRODUCT_VIEW
import com.tokopedia.tokomart.search.utils.SearchTracking.Misc.HOME_AND_BROWSE
import com.tokopedia.tokomart.search.utils.SearchTracking.Misc.TOKONOW_SEARCH_PRODUCT_ORGANIC
import com.tokopedia.tokomart.search.utils.SearchTracking.Misc.USER_ID
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

object SearchTracking {

    object Event {
        const val PRODUCT_VIEW = "productView"
        const val PRODUCT_CLICK = "productClick"
        const val ADD_TO_CART = "addToCart"
        const val PROMO_VIEW = "promoView"
        const val PROMO_CLICK = "promoClick"
    }

    object Action {
        const val GENERAL_SEARCH = "general search"
        const val IMPRESSION_PRODUCT = "impression - product"
        const val CLICK_PRODUCT = "click - product"
        const val CLICK_FILTER_OPTION = "click - filter option"
        const val CLICK_APPLY_FILTER = "click - apply filter"
        const val CLICK_CATEGORY_FILTER = "click - category filter"
        const val CLICK_FUZZY_KEYWORDS_REPLACE = "click - fuzzy keywords - replace"
        const val CLICK_TAMBAH_KE_KERANJANG = "click - tambah ke keranjang"
        const val CLICK_ADD_QUANTITY = "click - add quantity"
        const val CLICK_REMOVE_QUANTITY = "click - remove quantity"
        const val CLICK_CHOOSE_VARIANT_ON_PRODUCT_CARD = "click - choose variant on product card"
        const val IMPRESSION_BANNER = "impression - banner"
        const val CLICK_BANNER = "click - banner"
        const val EVENT_ACTION_CLICK_SEARCH_BAR_VALUE = "click - search - search bar"
    }

    object Category {
        const val TOKONOW_TOP_NAV = "tokonow - top nav"
        const val TOKONOW_SEARCH_RESULT = "tokonow - search result"
    }

    object ECommerce {
        const val ECOMMERCE = "ecommerce"
        const val CURRENCYCODE = "currencyCode"
        const val IDR = "IDR"
        const val IMPRESSIONS = "impressions"
        const val CLICK = "click"
        const val ACTION_FIELD = "actionField"
        const val LIST = "list"
        const val PRODUCTS = "products"
        const val ADD = "add"
        const val PROMO_VIEW = "promoView"
        const val PROMO_CLICK = "promoClick"
        const val PROMOTIONS = "promotions"
    }

    object Misc {
        const val NONE_OTHER = "none / other"
        const val TOKO_NOW = "toko now"
        const val USER_ID = "userId"
        const val HASIL_PENCARIAN_DI_TOKONOW = "Hasil pencarian di TokoNOW!"
        const val LOCAL_SEARCH = "local_search"
        const val TOKONOW_SEARCH_PRODUCT_ORGANIC = "/tokonow - searchproduct - organic"
        const val HOME_AND_BROWSE = "home & browse"
    }

    fun getDimension90(pageId: String) =
            "${Misc.HASIL_PENCARIAN_DI_TOKONOW}.$TOKONOW.${Misc.LOCAL_SEARCH}.$pageId"

    fun sendGeneralEvent(dataLayer: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(dataLayer)
    }

    fun sendProductImpressionEvent(
            trackingQueue: TrackingQueue,
            list: List<Any>,
            keyword: String,
            userId: String,
    ) {
        val map = DataLayer.mapOf(
                EVENT, PRODUCT_VIEW,
                EVENT_ACTION, IMPRESSION_PRODUCT,
                EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                EVENT_LABEL, keyword,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    CURRENCYCODE, IDR,
                    IMPRESSIONS, DataLayer.listOf(*list.toTypedArray())
                )
        ) as HashMap<String, Any>

        trackingQueue.putEETracking(map)
    }

    fun sendProductClickEvent(
            dataLayer: Any,
            keyword: String,
            userId: String,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                DataLayer.mapOf(
                        EVENT, PRODUCT_CLICK,
                        EVENT_ACTION, CLICK_PRODUCT,
                        EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                        EVENT_LABEL, keyword,
                        KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                        KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                        USER_ID, userId,
                        ECOMMERCE, DataLayer.mapOf(
                            CLICK, DataLayer.mapOf(
                                ACTION_FIELD, DataLayer.mapOf(
                                    LIST, TOKONOW_SEARCH_PRODUCT_ORGANIC,
                                    PRODUCTS, DataLayer.listOf(dataLayer)
                                )
                            ),
                        )
                )
        )
    }
    
    fun sendOpenFilterPageEvent() {
        sendGeneralEvent(
                DataLayer.mapOf(
                    EVENT, EVENT_CLICK_TOKONOW,
                    EVENT_ACTION, CLICK_FILTER_OPTION,
                    EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                    EVENT_LABEL, "",
                    KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                    KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                )
        )
    }

    fun sendApplySortFilterEvent(filterParams: String) {
        sendGeneralEvent(
                DataLayer.mapOf(
                    EVENT, EVENT_CLICK_TOKONOW,
                    EVENT_ACTION, CLICK_APPLY_FILTER,
                    EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                    EVENT_LABEL, filterParams,
                    KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                    KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                )
        )
    }

    fun sendApplyCategoryL2FilterEvent(categoryName: String) {
        sendGeneralEvent(
                DataLayer.mapOf(
                    EVENT, EVENT_CLICK_TOKONOW,
                    EVENT_ACTION, CLICK_CATEGORY_FILTER,
                    EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                    EVENT_LABEL, categoryName,
                    KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                    KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                )
        )
    }

    fun sendSuggestionClickEvent(originalKeyword: String, fuzzyKeyword: String, ) {
        sendGeneralEvent(
                DataLayer.mapOf(
                    EVENT, EVENT_CLICK_TOKONOW,
                    EVENT_ACTION, CLICK_FUZZY_KEYWORDS_REPLACE,
                    EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                    EVENT_LABEL, "$originalKeyword - $fuzzyKeyword",
                    KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                    KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                )
        )
    }

    fun sendAddToCartEvent(
            dataLayer: Any,
            keyword: String,
            userId: String,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, ADD_TO_CART,
                EVENT_ACTION, CLICK_TAMBAH_KE_KERANJANG,
                EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                EVENT_LABEL, keyword,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    ADD, DataLayer.mapOf(PRODUCTS, DataLayer.listOf(dataLayer)),
                    CURRENCYCODE, IDR,
                ),
            )
        )
    }

    fun sendIncreaseQtyEvent(keyword: String, productId: String) {
        sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_ADD_QUANTITY,
                EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                EVENT_LABEL, "$keyword - $productId",
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendDecreaseQtyEvent(keyword: String, productId: String) {
        sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_REMOVE_QUANTITY,
                EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                EVENT_LABEL, "$keyword - $productId",
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendChooseVariantEvent(keyword: String, productId: String) {
        sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_CHOOSE_VARIANT_ON_PRODUCT_CARD,
                EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                EVENT_LABEL, "$keyword - $productId",
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendBannerImpressionEvent(
            channelModel: ChannelModel,
            keyword: String,
            userId: String,
            sortFilterParams: String,
            pageId: String,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, Event.PROMO_VIEW,
                EVENT_ACTION, IMPRESSION_BANNER,
                EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                EVENT_LABEL, keyword,
                KEY_BUSINESS_UNIT, HOME_AND_BROWSE,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    ECommerce.PROMO_VIEW, DataLayer.mapOf(
                        PROMOTIONS, channelModel.getAsObjectDataLayer(sortFilterParams, pageId)
                    ),
                )
            )
        )
    }

    private fun ChannelModel.getAsObjectDataLayer(sortFilterParam: String, pageId: String): List<Any> {
        return channelGrids.mapIndexed { index, channelGrid ->
            getChannelGridAsObjectDataLayer(this, channelGrid, sortFilterParam, pageId, index)
        }
    }

    private fun getChannelGridAsObjectDataLayer(
            channelModel: ChannelModel,
            channelGrid: ChannelGrid,
            sortFilterParam: String,
            pageId: String,
            position: Int,
    ): Any {
        val channelModelId = channelModel.id ?: ""
        val channelGridId = channelGrid.id
        val persoType = channelModel.trackingAttributionModel.persoType
        val categoryId = channelModel.trackingAttributionModel.categoryId
        val id = channelModelId + "_" + channelGridId + "_" + persoType + "_" + categoryId

        val promoName = channelModel.trackingAttributionModel.promoName
        val name = "/tokonow - search - $promoName"

        return DataLayer.mapOf(
                "id", id,
                "name", name,
                "creative", channelGrid.id,
                "position", position,
                "dimension61", sortFilterParam,
                "dimension90", getDimension90(pageId)
        )
    }

    fun sendBannerClickEvent(
            channelModel: ChannelModel,
            keyword: String,
            userId: String,
            sortFilterParams: String,
            pageId: String,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, Event.PROMO_CLICK,
                EVENT_ACTION, CLICK_BANNER,
                EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                EVENT_LABEL, keyword,
                KEY_BUSINESS_UNIT, HOME_AND_BROWSE,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    PROMO_CLICK, DataLayer.mapOf(
                        PROMOTIONS, channelModel.getAsObjectDataLayer(sortFilterParams, pageId)
                    ),
                )
            )
        )
    }
}
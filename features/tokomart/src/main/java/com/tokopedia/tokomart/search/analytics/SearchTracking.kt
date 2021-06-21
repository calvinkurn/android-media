package com.tokopedia.tokomart.search.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.filter.common.data.Option
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOP_NAV
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.CLICK_ADD_QUANTITY
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.CLICK_APPLY_CATEGORY_FILTER
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.CLICK_APPLY_FILTER
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.CLICK_BANNER
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.CLICK_CATEGORY_FILTER
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.CLICK_CHOOSE_VARIANT_ON_PRODUCT_CARD
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.CLICK_FILTER_OPTION
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.CLICK_FUZZY_KEYWORDS_REPLACE
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.CLICK_PRODUCT
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.CLICK_QUICK_FILTER
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.CLICK_REMOVE_QUANTITY
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.CLICK_TAMBAH_KE_KERANJANG
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.EVENT_ACTION_CLICK_SEARCH_BAR_VALUE
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.IMPRESSION_BANNER
import com.tokopedia.tokomart.search.analytics.SearchTracking.Action.IMPRESSION_PRODUCT
import com.tokopedia.tokomart.search.analytics.SearchTracking.Category.TOKONOW_SEARCH_RESULT
import com.tokopedia.tokomart.search.analytics.SearchTracking.Misc.TOKONOW_SEARCH_PRODUCT_ORGANIC
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.ACTION_FIELD
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.ADD
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.CLICK
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.CURRENCYCODE
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.ECOMMERCE
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.IDR
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.IMPRESSIONS
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.LIST
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.PRODUCTS
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.PROMOTIONS
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Event.ADD_TO_CART
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Event.PRODUCT_CLICK
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Event.PRODUCT_VIEW
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Event.PROMO_CLICK
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Event.PROMO_VIEW
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Misc.HOME_AND_BROWSE
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Misc.USER_ID
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

object SearchTracking {

    object Action {
        const val GENERAL_SEARCH = "general search"
        const val IMPRESSION_PRODUCT = "impression - product"
        const val CLICK_PRODUCT = "click - product"
        const val CLICK_FILTER_OPTION = "click - filter option"
        const val CLICK_QUICK_FILTER = "click - quick filter"
        const val CLICK_APPLY_FILTER = "click - apply filter"
        const val CLICK_CATEGORY_FILTER = "click - category filter"
        const val CLICK_FUZZY_KEYWORDS_REPLACE = "click - fuzzy keywords - replace"
        const val CLICK_TAMBAH_KE_KERANJANG = "click - tambah ke keranjang"
        const val CLICK_ADD_QUANTITY = "click - add quantity"
        const val CLICK_REMOVE_QUANTITY = "click - remove quantity"
        const val CLICK_CHOOSE_VARIANT_ON_PRODUCT_CARD = "click - choose variant on product card"
        const val IMPRESSION_BANNER = "impression - banner"
        const val CLICK_BANNER = "click - banner"
        const val CLICK_APPLY_CATEGORY_FILTER = "click - apply category filter"
        const val EVENT_ACTION_CLICK_SEARCH_BAR_VALUE = "click - search - search bar"
    }

    object Category {
        const val TOKONOW_TOP_NAV = "tokonow - top nCLICK_CATEGORY_FILTERav"
        const val TOKONOW_SEARCH_RESULT = "tokonow - search result"
    }

    object Misc {
        const val HASIL_PENCARIAN_DI_TOKONOW = "Hasil pencarian di TokoNOW!"
        const val TOKONOW_SEARCH_PRODUCT_ORGANIC = "/tokonow - searchproduct - organic"
    }

    fun sendGeneralEvent(dataLayer: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(dataLayer)
    }

    fun sendSearchBarClickEvent(keyword: String) {
        sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_TOKONOW,
                        EVENT_ACTION, EVENT_ACTION_CLICK_SEARCH_BAR_VALUE,
                        EVENT_CATEGORY, EVENT_CATEGORY_TOP_NAV,
                        EVENT_LABEL, keyword,
                        KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                        KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                )
        )
    }

    fun sendProductImpressionEvent(
            trackingQueue: TrackingQueue,
            productItemDataView: ProductItemDataView,
            filterSortValue: String,
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
                    IMPRESSIONS, DataLayer.listOf(
                        productItemDataView.getAsImpressionClickObjectDataLayer(filterSortValue)
                    )
                )
        ) as HashMap<String, Any>

        trackingQueue.putEETracking(map)
    }

    private fun ProductItemDataView.getAsObjectDataLayerMap(
            filterSortValue: String
    ): MutableMap<String, Any> {
        return DataLayer.mapOf(
                "brand", SearchCategoryTrackingConst.Misc.NONE_OTHER,
                "category", SearchCategoryTrackingConst.Misc.NONE_OTHER,
                "dimension100", sourceEngine,
                "dimension61", filterSortValue,
                "dimension81", SearchCategoryTrackingConst.Misc.TOKO_NOW,
                "dimension96", boosterList,
                "id", id,
                "dimension40", TOKONOW_SEARCH_PRODUCT_ORGANIC,
                "name", name,
                "price", priceInt,
                "variant", SearchCategoryTrackingConst.Misc.NONE_OTHER,
        )
    }

    private fun ProductItemDataView.getAsImpressionClickObjectDataLayer(
            filterSortValue: String,
    ): Any {
        return getAsObjectDataLayerMap(filterSortValue).also {
            it.putAll(DataLayer.mapOf(
                    "position", position,
            ))
        }
    }

    fun sendProductClickEvent(
            productItemDataView: ProductItemDataView,
            keyword: String,
            userId: String,
            filterSortValue: String,
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
                            PRODUCTS, DataLayer.listOf(
                                productItemDataView.getAsImpressionClickObjectDataLayer(filterSortValue)
                            )
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

    fun sendQuickFilterClickEvent(option: Option, isSelected: Boolean) {
        sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_TOKONOW,
                        EVENT_ACTION, CLICK_QUICK_FILTER,
                        EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                        EVENT_LABEL, "${option.name} - ${option.value} - $isSelected",
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

    fun sendApplyCategoryL3FilterEvent(categoryFilterParam: String) {
        sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_TOKONOW,
                        EVENT_ACTION, CLICK_APPLY_CATEGORY_FILTER,
                        EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                        EVENT_LABEL, categoryFilterParam,
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
            productItemDataView: ProductItemDataView,
            keyword: String,
            userId: String,
            sortFilterParams: String,
            quantity: Int,
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
                    ADD, DataLayer.mapOf(
                        PRODUCTS, DataLayer.listOf(
                            productItemDataView.getAsATCObjectDataLayer(sortFilterParams, quantity)
                        )
                    ),
                    CURRENCYCODE, IDR,
                ),
            )
        )
    }

    private fun ProductItemDataView.getAsATCObjectDataLayer(
            filterSortValue: String,
            quantity: Int,
    ): Any {
        return getAsObjectDataLayerMap(filterSortValue).also {
            it.putAll(DataLayer.mapOf(
                    "quantity", quantity,
                    "shop_id", shop.id,
                    "shop_name", shop.name,
            ))
        }
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
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_ACTION, IMPRESSION_BANNER,
                EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                EVENT_LABEL, keyword,
                KEY_BUSINESS_UNIT, HOME_AND_BROWSE,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    PROMO_VIEW, DataLayer.mapOf(
                        PROMOTIONS, channelModel.getAsObjectDataLayer(sortFilterParams)
                    ),
                )
            )
        )
    }

    private fun ChannelModel.getAsObjectDataLayer(sortFilterParam: String): List<Any> {
        return channelGrids.mapIndexed { index, channelGrid ->
            getChannelGridAsObjectDataLayer(this, channelGrid, sortFilterParam, index)
        }
    }

    private fun getChannelGridAsObjectDataLayer(
            channelModel: ChannelModel,
            channelGrid: ChannelGrid,
            sortFilterParam: String,
            position: Int,
    ): Any {
        val channelModelId = channelModel.id
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
        )
    }

    fun sendBannerClickEvent(
            channelModel: ChannelModel,
            keyword: String,
            userId: String,
            sortFilterParams: String,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_ACTION, CLICK_BANNER,
                EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                EVENT_LABEL, keyword,
                KEY_BUSINESS_UNIT, HOME_AND_BROWSE,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    PROMO_CLICK, DataLayer.mapOf(
                        PROMOTIONS, channelModel.getAsObjectDataLayer(sortFilterParams)
                    ),
                )
            )
        )
    }
}
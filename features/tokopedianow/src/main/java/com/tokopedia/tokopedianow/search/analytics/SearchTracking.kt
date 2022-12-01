package com.tokopedia.tokopedianow.search.analytics

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.filter.common.data.Option
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.SCREEN_NAME_TOKONOW_OOC
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_ADD_QUANTITY
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_APPLY_CATEGORY_FILTER
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_APPLY_FILTER
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_BANNER
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_BROADMATCH
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_BROADMATCH_LIHAT_SEMUA
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_CARI_BARANG_DI_TOKONOW
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_CATEGORY_FILTER
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_CATEGORY_JUMPER
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_CHOOSE_VARIANT_ON_PRODUCT_CARD
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_DELETE_ITEM_FROM_CART
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_FILTER_OPTION
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_FUZZY_KEYWORDS_SUGGESTION
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_PRODUCT
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_QUICK_FILTER
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_REMOVE_QUANTITY
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_TAMBAH_KE_KERANJANG
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.CLICK_VIEW_ALL_ON_TOKONOW_RECOMMENDATION
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.IMPRESSION_BANNER
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.IMPRESSION_BROADMATCH
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Action.IMPRESSION_PRODUCT
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOKONOW_EMPTY_RESULT
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOKONOW_NO_SEARCH_RESULT
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOKONOW_SEARCH_RESULT
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Misc.TOKONOW_BROAD_MATCH
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Misc.TOKONOW_OOC_SCREEN_NAME
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Misc.TOKONOW_SEARCH_PRODUCT_ORGANIC
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.ACTION_FIELD
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.ADD
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.CLICK
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.CURRENCYCODE
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.ECOMMERCE
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.IDR
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.IMPRESSIONS
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.LIST
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.PRODUCTS
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.PROMOTIONS
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Event.ADD_TO_CART
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Event.PRODUCT_CLICK
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Event.PRODUCT_VIEW
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Event.PROMO_CLICK
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Event.PROMO_VIEW
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.DEFAULT
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.HOME_AND_BROWSE
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.USER_ID
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.similarproduct.analytic.TokonowSimilarProductConstants
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.track.constant.TrackerConstant
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
        const val CLICK_FUZZY_KEYWORDS_SUGGESTION = "click - fuzzy keywords - suggestion"
        const val CLICK_TAMBAH_KE_KERANJANG = "click - tambah ke keranjang"
        const val CLICK_ADD_QUANTITY = "click - add quantity"
        const val CLICK_REMOVE_QUANTITY = "click - remove quantity"
        const val CLICK_CHOOSE_VARIANT_ON_PRODUCT_CARD = "click - choose variant on product card"
        const val IMPRESSION_BANNER = "impression - banner"
        const val CLICK_BANNER = "click - banner"
        const val CLICK_APPLY_CATEGORY_FILTER = "click - apply category filter"
        const val CLICK_DELETE_ITEM_FROM_CART = "click - delete all items from cart"
        const val CLICK_CATEGORY_JUMPER = "click - category jumper"
        const val CLICK_CARI_BARANG_DI_TOKONOW = "click - cari barang di tokonow"
        const val IMPRESSION_SRP_PRODUCT_TOKONOW =
            "impression product on tokonow product recommendation"
        const val CLICK_SRP_PRODUCT_TOKONOW =
            "click product on tokonow product recommendation"
        const val CLICK_ATC_SRP_PRODUCT_TOKONOW =
            "click add to cart on tokonow product recommendation"
        const val IMPRESSION_SRP_RECOM_OOC = "view product on recom widget on tokonow srp while the address is out of coverage (OOC)"
        const val CLICK_SRP_RECOM_OOC = "click product on recom widget on tokonow srp while the address is out of coverage (OOC)"
        const val IMPRESSION_BROADMATCH = "impression - broad match"
        const val CLICK_BROADMATCH = "click - broad match"
        const val CLICK_BROADMATCH_LIHAT_SEMUA = "click - broad match lihat semua"
        const val CLICK_VIEW_ALL_ON_TOKONOW_RECOMMENDATION = "click view all on tokonow recommendation"
    }

    object Category {
        const val TOP_NAV = "top nav"
        const val TOKONOW_SEARCH_RESULT = "tokonow - search result"
        const val TOKONOW_NO_SEARCH_RESULT = "tokonow - no search result"
        const val TOKONOW_EMPTY_RESULT =
            "tokonow empty search result"
        const val TOKOOW_SEARCH_RESULT_PAGE = "tokonow search result page"
        const val TOKONOW_DASH_SEARCH_PAGE = "tokonow - search page"
    }

    object Misc {
        const val TOKONOW_SEARCH_PRODUCT_ORGANIC = "/tokonow - searchproduct - organic"
        const val TOKONOW_SEARCH_PRODUCT_ATC_VARIANT = "/tokonow - search page"
        const val RECOM_LIST_PAGE = "searchproduct"
        const val RECOM_LIST_PAGE_EMPTY_SEARCH =
            "/${RECOM_LIST_PAGE} - tokonow - rekomendasi untuk anda - empty_search - %s"
        const val TOKONOW_BROAD_MATCH = "/tokonow - broad match"
        const val TOKONOW_OOC_SCREEN_NAME = "search result tokonow"
    }

    fun sendGeneralEvent(dataLayer: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(dataLayer)
    }

    fun sendProductImpressionEvent(
            trackingQueue: TrackingQueue,
            productItemDataView: ProductItemDataView,
            keyword: String,
            userId: String,
            filterSortValue: String,
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

    private fun ProductItemDataView.getAsImpressionClickObjectDataLayer(
            filterSortValue: String,
    ): Any {
        return getAsObjectDataLayerMap(filterSortValue).also {
            it.putAll(DataLayer.mapOf(
                    "position", position,
                    "list", TOKONOW_SEARCH_PRODUCT_ORGANIC,
            ))
        }
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
                "id", productCardModel.productId,
                "name", productCardModel.name,
                "price", productCardModel.price.getDigits(),
                "variant", SearchCategoryTrackingConst.Misc.NONE_OTHER,
        )
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
                        ACTION_FIELD, DataLayer.mapOf(LIST, TOKONOW_SEARCH_PRODUCT_ORGANIC),
                        PRODUCTS, DataLayer.listOf(
                            productItemDataView.getAsClickObjectDataLayer(filterSortValue)
                        )
                    ),
                )
            )
        )
    }

    private fun ProductItemDataView.getAsClickObjectDataLayer(
            filterSortValue: String,
    ): Any {
        return getAsObjectDataLayerMap(filterSortValue).also {
            it.putAll(DataLayer.mapOf(
                    "position", position,
            ))
        }
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
                    EVENT_ACTION, CLICK_FUZZY_KEYWORDS_SUGGESTION,
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
        val headerName = if (promoName.isEmpty()) DEFAULT else promoName
        val name = "/tokonow - search - $headerName"

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

    fun sendDeleteCartEvent(
            productId: String,
    ) {
        sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_DELETE_ITEM_FROM_CART,
                EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                EVENT_LABEL, productId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendClickCategoryJumperEvent(categoryName: String) {
        sendGeneralEvent(
            DataLayer.mapOf(
                    EVENT, EVENT_CLICK_TOKONOW,
                    EVENT_ACTION, CLICK_CATEGORY_JUMPER,
                    EVENT_CATEGORY, TOKONOW_NO_SEARCH_RESULT,
                    EVENT_LABEL, categoryName,
                    KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                    KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendClickCTAToHome() {
        sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_TOKONOW,
                        EVENT_ACTION, CLICK_CARI_BARANG_DI_TOKONOW,
                        EVENT_CATEGORY, TOKONOW_NO_SEARCH_RESULT,
                        EVENT_LABEL, "",
                        KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                        KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                )
        )
    }

    fun sendBroadMatchImpressionEvent(
        trackingQueue: TrackingQueue,
        broadMatchItemDataView: TokoNowProductCardCarouselItemUiModel,
        keyword: String,
        userId: String,
        position: Int
    ) {
        val map = DataLayer.mapOf(
            EVENT, PRODUCT_VIEW,
            EVENT_ACTION, IMPRESSION_BROADMATCH,
            EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
            EVENT_LABEL, "$keyword - ${broadMatchItemDataView.alternativeKeyword}",
            KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
            KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            USER_ID, userId,
            ECOMMERCE, DataLayer.mapOf(
                CURRENCYCODE, IDR,
                IMPRESSIONS, DataLayer.listOf(
                    broadMatchItemDataView.getAsImpressionClickObjectDataLayer(position)
                )
            )
        ) as HashMap<String, Any>

        trackingQueue.putEETracking(map)
    }

    private fun TokoNowProductCardCarouselItemUiModel.getAsImpressionClickObjectDataLayer(position: Int): Any {
        return getAsObjectDataLayerMap().also {
            it.putAll(DataLayer.mapOf(
                "list", TOKONOW_BROAD_MATCH,
                "position", position.getTrackerPosition().toString(),
            ))
        }
    }

    private fun TokoNowProductCardCarouselItemUiModel.getAsObjectDataLayerMap(): MutableMap<String, Any> {
        return DataLayer.mapOf(
            "brand", SearchCategoryTrackingConst.Misc.NONE_OTHER,
            "category", SearchCategoryTrackingConst.Misc.NONE_OTHER,
            "id", productCardModel.productId,
            "name", productCardModel.name,
            "price", productCardModel.price.getDigits().toString(),
            "variant", SearchCategoryTrackingConst.Misc.NONE_OTHER,
        )
    }

    fun sendBroadMatchClickEvent(
        broadMatchItemDataView: TokoNowProductCardCarouselItemUiModel,
        keyword: String,
        userId: String,
        position: Int
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, PRODUCT_CLICK,
                EVENT_ACTION, CLICK_BROADMATCH,
                EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                EVENT_LABEL, "$keyword - ${broadMatchItemDataView.alternativeKeyword}",
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    CLICK, DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(LIST, TOKONOW_BROAD_MATCH),
                        PRODUCTS, DataLayer.listOf(
                            broadMatchItemDataView.getAsImpressionClickObjectDataLayer(position)
                        )
                    ),
                )
            )
        )
    }

    fun sendBroadMatchSeeAllClickEvent(title: String, keyword: String) {
        sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_BROADMATCH_LIHAT_SEMUA,
                EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                EVENT_LABEL, "$keyword - $title",
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendBroadMatchAddToCartEvent(
        broadMatchItemDataView: TokoNowProductCardCarouselItemUiModel,
        keyword: String,
        userId: String,
        quantity: Int,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, ADD_TO_CART,
                EVENT_ACTION, CLICK_TAMBAH_KE_KERANJANG,
                EVENT_CATEGORY, TOKONOW_SEARCH_RESULT,
                EVENT_LABEL, "$keyword - ${broadMatchItemDataView.alternativeKeyword}",
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    ADD, DataLayer.mapOf(
                        PRODUCTS, DataLayer.listOf(
                            broadMatchItemDataView.getAsATCObjectDataLayer(quantity)
                        )
                    ),
                    CURRENCYCODE, IDR,
                ),
            )
        )
    }

    private fun TokoNowProductCardCarouselItemUiModel.getAsATCObjectDataLayer(quantity: Int): Any {
        return getAsObjectDataLayerMap().also {
            it.putAll(DataLayer.mapOf(
                "dimension40", TOKONOW_BROAD_MATCH,
                "quantity", quantity,
                "shop_id", productCardModel.productId,
                "category_id", SearchCategoryTrackingConst.Misc.NONE_OTHER,
            ))
        }
    }

    fun sendRecommendationSeeAllClickEvent(keyword: String) {
        sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_VIEW_ALL_ON_TOKONOW_RECOMMENDATION,
                EVENT_CATEGORY, TOKONOW_EMPTY_RESULT,
                EVENT_LABEL, keyword,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendOOCOpenScreenTracking(isLoggedInStatus: Boolean) {
        TokoNowCommonAnalytics.onOpenScreen(
            isLoggedInStatus = isLoggedInStatus,
            screenName = SCREEN_NAME_TOKONOW_OOC + TOKONOW_OOC_SCREEN_NAME
        )
    }

    /* Similar Product Bottomsheet Trackers*/

    fun trackClickSimilarProductBtn(warehouseId: String, productId: String, userId: String) {
        val dataLayer = createGeneralDataLayer(
            event = TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES,
            action = CategoryTracking.Action.EVENT_ACTION_CLICK_SIMILAR_PRODUCT_BTN,
            label = "$warehouseId - $productId",
            userId = userId
        ).apply {
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_PRODUCT_ID, productId)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID, TokonowSimilarProductConstants.TRACKER_ID_CLICK_SIMILAR_PRODUCT_BUTTON_SEARCH)
        }

        sendEnhanceEcommerceEvent(
            eventName = TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES,
            dataLayer = dataLayer
        )
    }

    fun trackImpressionBottomSheet(warehouseId: String, productId: String, similarProducts: ArrayList<SimilarProductUiModel>, userId: String) {

        val items = arrayListOf<Bundle>()
        similarProducts.forEach { product ->
            items.add(
                createProductItemDataLayer(
                    index = product.position,
                    id = product.id,
                    name = product.name,
                    price = product.priceFmt,
                    category = product.categoryName
                )
            )
        }

        val dataLayer = createGeneralDataLayer(
            event = TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST,
            action = CategoryTracking.Action.EVENT_ACTION_IMPRESSION_BOTTOMSHEET,
            label = "$warehouseId - $productId - ${similarProducts.firstOrNull()?.id ?: ""} - ${similarProducts.getOrNull(1)?.id ?: ""} - ${similarProducts.getOrNull(2)?.id ?: ""}",
            userId = userId
        ).apply {
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_PRODUCT_ID, productId)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID, TokonowSimilarProductConstants.TRACKER_ID_VIEW_SIMILAR_PRODUCT_BOTTOMSHEET_SEARCH)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST, "/tokonow - product card - similar product recom")
            putParcelableArrayList(TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS, ArrayList(items))
        }

        sendEnhanceEcommerceEvent(
            eventName = TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_PG_IRIS,
            dataLayer = dataLayer
        )
    }

    fun trackClickProduct(warehouseId: String, productId: String, similarProducts: ArrayList<SimilarProductUiModel>, userId: String) {

        val items = arrayListOf<Bundle>()
        similarProducts.forEach { product ->
            items.add(
                createProductItemDataLayer(
                    index = product.position,
                    id = product.id,
                    name = product.name,
                    price = product.priceFmt,
                    category = product.categoryId
                )
            )
        }

        val dataLayer = createGeneralDataLayer(
            event = TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT,
            action = CategoryTracking.Action.EVENT_ACTION_CLICK_PRODUCT,
            label = "$warehouseId - $productId - ${similarProducts.firstOrNull()?.id ?: ""} - ${similarProducts.getOrNull(1)?.id ?: ""} - ${similarProducts.getOrNull(2)?.id ?: ""}",
            userId = userId
        ).apply {
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_PRODUCT_ID, productId)
            putString(
                TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID,
                TokonowSimilarProductConstants.TRACKER_ID_CLICK_PRODUCT_SEARCH
            )
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST, "/tokonow - product card - similar product recom")
            putParcelableArrayList(TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS, ArrayList(items))
        }

        sendEnhanceEcommerceEvent(
            eventName = TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT,
            dataLayer = dataLayer
        )
    }

    fun trackClickAddToCart(userId: String, warehouseId: String, product: SimilarProductUiModel, similarProducts: ArrayList<SimilarProductUiModel>) {
        val items = listOf(
            createAtcProductItemDataLayer(
                id = product.id,
                name = product.name,
                price = product.priceFmt,
                categoryName = product.categoryName,
                categoryId = product.categoryId,
                quantity = product.quantity.toString(),
                shopId = product.shopId,
                shopName = product.shopName
            )
        )

        val dataLayer = createGeneralDataLayer(
            event = TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART,
            action = CategoryTracking.Action.EVENT_ACTION_CLICK_ADD_TO_CART,
            label = "$warehouseId - ${product.id} - ${similarProducts.firstOrNull()?.id ?: ""} - ${similarProducts.getOrNull(1)?.id ?: ""} - ${similarProducts.getOrNull(2)?.id ?: ""}",
            userId = userId
        ).apply {
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID, userId)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_PRODUCT_ID, product.id)
            putString(
                TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID, TokonowSimilarProductConstants.TRACKER_ID_ADD_TO_CART_SEARCH
            )
            putParcelableArrayList(TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS, ArrayList(items))
        }

        sendEnhanceEcommerceEvent(
            eventName = TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART,
            dataLayer = dataLayer
        )
    }

    fun trackClickCloseBottomsheet(
        warehouseId: String,
        productId: String,
        similarProducts: ArrayList<SimilarProductUiModel>,
        userId: String
    ) {
        val dataLayer = createGeneralDataLayer(
            event = TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES,
            action = CategoryTracking.Action.EVENT_ACTION_CLICK_CLOSE_BOTTOMSHEET,
            label = "$warehouseId - ${productId} - ${similarProducts.firstOrNull()?.id ?: ""} - ${similarProducts.getOrNull(1)?.id ?: ""} - ${similarProducts.getOrNull(2)?.id ?: ""}",
            userId = userId
        ).apply {
            putString(
                TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID,
                TokonowSimilarProductConstants.TRACKER_ID_CLICK_CLOSE_BOTTOMSHEET_SEARCH
            )
        }

        sendEnhanceEcommerceEvent(
            eventName = TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES,
            dataLayer = dataLayer
        )
    }

    fun trackImpressionEmptyState(warehouseId: String, productId: String, userId: String) {
        val dataLayer = createGeneralDataLayer(
            event = TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES,
            action = CategoryTracking.Action.EVENT_ACTION_IMPRESSION_EMPTY_STATE,
            label = "$warehouseId - $productId",
            userId = userId
        ).apply {
            putString(
                TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID,
                TokonowSimilarProductConstants.TRACKER_ID_VIEW_EMPTY_STATE_SEARCH
            )
        }

        sendEnhanceEcommerceEvent(
            eventName = TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES,
            dataLayer = dataLayer
        )
    }

    private fun createGeneralDataLayer(event: String, action: String, label: String = TokoNowCommonAnalyticConstants.VALUE.DEFAULT_EMPTY_VALUE, userId: String): Bundle {
        return Bundle().apply {
            putString(TrackerConstant.EVENT, event)
            putString(TrackerConstant.EVENT_ACTION, action)
            putString(TrackerConstant.EVENT_CATEGORY, TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOKOPEDIA_NOW_SEARCH)
            putString(TrackerConstant.EVENT_LABEL, label)
            putString(
                TrackerConstant.BUSINESS_UNIT,
                TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
            )
            putString(TrackerConstant.CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(TrackerConstant.USERID, userId)
        }
    }

    private fun createProductItemDataLayer(
        index: Int = 0,
        id: String = "",
        name: String = "",
        price: String = "",
        brand: String = "none/other",
        category: String = "",
        variant: String = "none/other"
    ): Bundle {
        return Bundle().apply {
            putInt(TokoNowCommonAnalyticConstants.KEY.KEY_INDEX, index)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_BRAND, brand)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_CATEGORY, category)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID, id)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME, name)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_VARIANT, variant)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_PRICE, price)
        }
    }

    private fun createAtcProductItemDataLayer(
        id: String = "",
        name: String = "",
        price: String = "",
        brand: String = "none/other",
        categoryName: String = "",
        categoryId: String = "",
        quantity: String = "",
        variant: String = "none/other",
        shopId: String = "",
        shopName: String = TokoNowCommonAnalyticConstants.VALUE.DEFAULT_EMPTY_VALUE
    ): Bundle {
        return Bundle().apply {
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_BRAND, brand)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_CATEGORY, categoryName)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID, id)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME, name)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_VARIANT, variant)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_PRICE, price)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_CATEGORY_ID, categoryId)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_QUANTITY, quantity)
            putString(TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_ID, shopId)
            putString(
                TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_NAME,
                TokoNowCommonAnalyticConstants.VALUE.DEFAULT_EMPTY_VALUE
            )
            putString(
                TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_TYPE,
                TokoNowCommonAnalyticConstants.VALUE.DEFAULT_EMPTY_VALUE
            )
        }
    }

    private fun sendEnhanceEcommerceEvent(eventName: String, dataLayer: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, dataLayer)
    }

}

package com.tokopedia.tokopedianow.category.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.ADD_QUANTITY_ON_BOTTOM_SHEET
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.APPLY_CATEGORY_FILTER
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_APPLY_FILTER
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_ATC_ON_PAST_PURCHASE_WIDGET
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_BANNER
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_CART_BUTTON_TOP_NAV
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_CATEGORY_FILTER
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_FILTER
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_LEVEL_2_FILTER_WIDGET
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_LIHAT_CATEGORY_LAINNYA
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_PILIH_VARIANT_BUTTON
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_PRODUCT
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_PRODUCT_ON_PAST_PURCHASE_WIDGET
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_QUICK_FILTER
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_SEARCH_BAR
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_SEMUA_KATEGORI
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.CLICK_VIEW_ALL_ON_TOKONOW_CLP_RECOMMENDATION
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.IMPRESSION_BANNER
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.IMPRESSION_ON_PAST_PURCHASE_WIDGET
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.IMPRESSION_PRODUCT
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Action.REMOVE_QUANTITY_ON_BOTTOM_SHEET
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Category.TOKONOW_CATEGORY_PAGE
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Category.TOKONOW_DASH_CATEGORY_PAGE
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Category.TOP_NAV_TOKONOW_CATEGORY_PAGE
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Event.CLICK_TOP_NAV
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Misc.LABEL_GROUP_HALAL
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Misc.NORMAL_PRICE
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Misc.PRODUCT_ID
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Misc.SLASH_PRICE
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Misc.TOKONOW_CATEGORY_ORGANIC
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Misc.TOKONOW_CATEGORY_PAGE_PAST_PURCHASE_WIDGET
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Misc.WITHOUT_HALAL_LABEL
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Misc.WITHOUT_VARIANT
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Misc.WITH_HALAL_LABEL
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking.Misc.WITH_VARIANT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
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
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Event.PRODUCT_CLICK
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Event.PRODUCT_VIEW
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Event.PROMO_CLICK
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Event.PROMO_VIEW
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.DEFAULT
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.HOME_AND_BROWSE
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.NONE_OTHER
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.TOKO_NOW
import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.USER_ID
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

object CategoryTracking {

    object Event {
        const val CLICK_TOP_NAV = "clickTopNav"
    }

    object Action {
        const val CLICK_SEARCH_BAR = "click search bar"
        const val CLICK_CART_BUTTON_TOP_NAV = "click cart button top nav"
        const val IMPRESSION_BANNER = "impression banner"
        const val CLICK_BANNER = "click banner"
        const val CLICK_SEMUA_KATEGORI = "click semua kategori"
        const val IMPRESSION_PRODUCT = "impression product"
        const val CLICK_PRODUCT = "click product"
        const val CLICK_LEVEL_2_FILTER_WIDGET = "click level 2 filter widget"
        const val CLICK_LIHAT_CATEGORY_LAINNYA = "click lihat kategori lainnya"
        const val CLICK_FILTER = "click filter"
        const val CLICK_QUICK_FILTER = "click quick filter"
        const val CLICK_APPLY_FILTER = "click apply filter"
        const val CLICK_CATEGORY_FILTER = "click category filter"
        const val APPLY_CATEGORY_FILTER = "apply category filter"
        const val ADD_TO_CART = "add to cart"
        const val CLICK_PILIH_VARIANT_BUTTON = "click pilih variant button"
        const val ADD_QUANTITY_ON_BOTTOM_SHEET = "add quantity on bottom sheet"
        const val REMOVE_QUANTITY_ON_BOTTOM_SHEET = "remove quantity on bottom sheet"
        const val IMPRESSION_CLP_PRODUCT_TOKONOW =
            "impression product on tokonow clp product recommendation"
        const val CLICK_CLP_PRODUCT_TOKONOW =
            "click product on tokonow clp product recommendation"
        const val CLICK_ATC_CLP_PRODUCT_TOKONOW =
            "click add to cart on tokonow clp product recommendation"
        const val IMPRESSION_CLP_RECOM_OOC = "view product on recom widget-tokonow-clp while the address is out of coverage (OOC)"
        const val CLICK_CLP_RECOM_OOC = "click product on recom widget-tokonow-clp while the address is out of coverage (OOC)"
        const val IMPRESSION_ON_PAST_PURCHASE_WIDGET = "impression on past purchase widget"
        const val CLICK_PRODUCT_ON_PAST_PURCHASE_WIDGET = "click product on past purchase widget"
        const val CLICK_ATC_ON_PAST_PURCHASE_WIDGET = "click atc on past purchase widget"
        const val CLICK_VIEW_ALL_ON_TOKONOW_CLP_RECOMMENDATION = "click view all on tokonow clp recommendation"
    }

    object Category {
        const val TOP_NAV_TOKONOW_CATEGORY_PAGE =  "top nav - tokonow category page"
        const val TOKONOW_CATEGORY_PAGE = "tokonow category page"
        const val TOKONOW_DASH_CATEGORY_PAGE = "tokonow - category page"
    }

    object Misc {
        const val PRODUCT_ID = "productId"
        const val TOKONOW_CATEGORY_ORGANIC = "/tokonow - category - %s"
        const val RECOM_LIST_PAGE = "clp_product"
        const val RECOM_LIST_PAGE_NON_OOC = "/$RECOM_LIST_PAGE - tokonow - rekomendasi untuk anda"
        const val WITH_HALAL_LABEL = "with halal label"
        const val WITHOUT_HALAL_LABEL = "without halal label"
        const val SLASH_PRICE = "slash price"
        const val NORMAL_PRICE = "normal price"
        const val WITH_VARIANT = "with variant"
        const val WITHOUT_VARIANT = "without variant"
        const val TOKONOW_CATEGORY_PAGE_PAST_PURCHASE_WIDGET =
            "/tokonow - category page - past_purchase_widget"
        const val LABEL_GROUP_HALAL = "Halal"
    }

    fun sendGeneralEvent(dataLayer: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(dataLayer)
    }

    fun sendSearchBarClickEvent(categoryId: String) {
        sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, CLICK_TOP_NAV,
                        EVENT_ACTION, CLICK_SEARCH_BAR,
                        EVENT_CATEGORY, TOP_NAV_TOKONOW_CATEGORY_PAGE,
                        EVENT_LABEL, categoryId,
                        KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                        KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                )
        )
    }

    fun sendCartClickEvent(categoryId: String) {
        sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_TOKONOW,
                        EVENT_ACTION, CLICK_CART_BUTTON_TOP_NAV,
                        EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                        EVENT_LABEL, categoryId,
                        KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                        KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                )
        )
    }

    fun sendBannerImpressionEvent(channelModel: ChannelModel, categoryId: String, userId: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_ACTION, IMPRESSION_BANNER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, HOME_AND_BROWSE,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    PROMO_VIEW, DataLayer.mapOf(
                        PROMOTIONS, channelModel.getAsObjectDataLayer()
                    ),
                )
            )
        )
    }

    private fun ChannelModel.getAsObjectDataLayer(): List<Any> {
        return channelGrids.mapIndexed { index, channelGrid ->
            getChannelGridAsObjectDataLayer(this, channelGrid, index)
        }
    }

    private fun getChannelGridAsObjectDataLayer(
            channelModel: ChannelModel,
            channelGrid: ChannelGrid,
            position: Int,
    ): Any {
        val channelModelId = channelModel.id
        val channelGridId = channelGrid.id
        val persoType = channelModel.trackingAttributionModel.persoType
        val categoryId = channelModel.trackingAttributionModel.categoryId
        val id = channelModelId + "_" + channelGridId + "_" + persoType + "_" + categoryId

        val promoName = channelModel.trackingAttributionModel.promoName
        val headerName = if (promoName.isEmpty()) DEFAULT else promoName
        val name = "/tokonow - category - $headerName"

        return DataLayer.mapOf(
                "id", id,
                "name", name,
                "creative", channelGrid.id,
                "position", position,
        )
    }

    fun sendBannerClickEvent(channelModel: ChannelModel, categoryId: String, userId: String) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_ACTION, CLICK_BANNER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, HOME_AND_BROWSE,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    PROMO_CLICK, DataLayer.mapOf(
                        PROMOTIONS, channelModel.getAsObjectDataLayer()
                    ),
                )
            )
        )
    }

    fun sendAllCategoryClickEvent(categoryId: String) {
        sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_TOKONOW,
                        EVENT_ACTION, CLICK_SEMUA_KATEGORI,
                        EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                        EVENT_LABEL, categoryId,
                        KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                        KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                )
        )
    }

    fun sendProductImpressionEvent(
            trackingQueue: TrackingQueue,
            productItemDataView: ProductItemDataView,
            categoryId: String,
            userId: String,
            categoryIdTracking: String,
    ) {
        val map = DataLayer.mapOf(
                EVENT, PRODUCT_VIEW,
                EVENT_ACTION, IMPRESSION_PRODUCT,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                PRODUCT_ID, productItemDataView.id,
                ECOMMERCE, DataLayer.mapOf(
                    CURRENCYCODE, IDR,
                    IMPRESSIONS, DataLayer.listOf(
                        productItemDataView.getAsImpressionClickDataLayer(categoryIdTracking)
                    )
                )
        ) as HashMap<String, Any>

        trackingQueue.putEETracking(map)
    }

    private fun ProductItemDataView.getAsImpressionClickDataLayer(categoryIdTracking: String): Any {
        return DataLayer.mapOf(
                "brand", NONE_OTHER,
                "category", NONE_OTHER,
                "id", id,
                "name", name,
                "price", priceInt,
                "variant", NONE_OTHER,
                "position", position,
                "list", String.format(TOKONOW_CATEGORY_ORGANIC, categoryIdTracking)
        )
    }

    fun sendProductClickEvent(
            productItemDataView: ProductItemDataView,
            categoryId: String,
            userId: String,
            categoryIdTracking: String,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, PRODUCT_CLICK,
                EVENT_ACTION, CLICK_PRODUCT,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                PRODUCT_ID, productItemDataView.id,
                ECOMMERCE, DataLayer.mapOf(
                    CLICK, DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(LIST, TOKONOW_CATEGORY_ORGANIC),
                        PRODUCTS, DataLayer.listOf(
                            productItemDataView.getAsImpressionClickDataLayer(categoryIdTracking)
                        )
                    )
                ),
            )
        )
    }

    fun sendApplyCategoryL2FilterEvent(categoryId: String, filterCategoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
            EVENT, EVENT_CLICK_TOKONOW,
            EVENT_ACTION, CLICK_LEVEL_2_FILTER_WIDGET,
            EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
            EVENT_LABEL, "$categoryId - $filterCategoryId",
            KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
            KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
        ))
    }

    fun sendAisleClickEvent(categoryId: String, aisleCategoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
            EVENT, EVENT_CLICK_TOKONOW,
            EVENT_ACTION, CLICK_LIHAT_CATEGORY_LAINNYA,
            EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
            EVENT_LABEL, "$categoryId - $aisleCategoryId",
            KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
            KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
        ))
    }

    fun sendFilterClickEvent(categoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
        ))
    }

    fun sendQuickFilterClickEvent(categoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_QUICK_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
        ))
    }

    fun sendApplySortFilterEvent(categoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_APPLY_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
        ))
    }

    fun sendOpenCategoryL3FilterEvent(categoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_CATEGORY_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
        ))
    }

    fun sendApplyCategoryL3FilterEvent(categoryId: String, filterCategoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, APPLY_CATEGORY_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, "$categoryId - $filterCategoryId",
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
        ))
    }

    fun sendAddToCartEvent(
            productItemDataView: ProductItemDataView,
            categoryId: String,
            userId: String,
            quantity: Int,
            cartId: String,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, SearchCategoryTrackingConst.Event.ADD_TO_CART,
                EVENT_ACTION, Action.ADD_TO_CART,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    ADD, DataLayer.mapOf(
                        PRODUCTS, DataLayer.listOf(
                            productItemDataView.getAsATCObjectDataLayer(categoryId, cartId, quantity)
                        )
                    ),
                    CURRENCYCODE, IDR,
                ),
            )
        )
    }

    private fun ProductItemDataView.getAsATCObjectDataLayer(
            categoryId: String,
            cartId: String,
            quantity: Int,
    ): Any {
        return DataLayer.mapOf(
                "brand", NONE_OTHER,
                "category", NONE_OTHER,
                "category_id", categoryId,
                "dimension45", cartId,
                "id", id,
                "name", name,
                "price", priceInt,
                "quantity", quantity,
                "shop_id", shop.id,
                "shop_name", shop.name,
                "shop_type", TOKO_NOW,
                "variant", NONE_OTHER
        )
    }

    fun sendChooseVariantClickEvent(categoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_PILIH_VARIANT_BUTTON,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
        ))
    }



    fun sendIncreaseQtyEvent(categoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, ADD_QUANTITY_ON_BOTTOM_SHEET,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
        ))
    }

    fun sendDecreaseQtyEvent(categoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, REMOVE_QUANTITY_ON_BOTTOM_SHEET,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
        ))
    }

    fun sendRepurchaseWidgetImpressionEvent(
        trackingQueue: TrackingQueue,
        repurchaseProduct: TokoNowProductCardUiModel,
        position: Int,
        userId: String,
    ) {
        val map = DataLayer.mapOf(
            EVENT, PRODUCT_VIEW,
            EVENT_ACTION, IMPRESSION_ON_PAST_PURCHASE_WIDGET,
            EVENT_CATEGORY, TOKONOW_DASH_CATEGORY_PAGE,
            EVENT_LABEL, createEventLabelRepurchaseWidget(repurchaseProduct),
            KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE,
            KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            USER_ID, userId,
            ECOMMERCE, DataLayer.mapOf(
                CURRENCYCODE, IDR,
                IMPRESSIONS, DataLayer.listOf(
                    repurchaseProduct.getAsImpressionObjectDataLayer(position)
                )
            )
        ) as HashMap<String, Any>

        trackingQueue.putEETracking(map)
    }

    private fun createEventLabelRepurchaseWidget(
        repurchaseProduct: TokoNowProductCardUiModel,
    ): String {
        val product = repurchaseProduct.product
        val hasHalalLabel = product.labelGroupList.any { it.title.contains(LABEL_GROUP_HALAL) }
        val hasSlashPrice = product.slashedPrice.isNotBlank()
        val hasVariant = product.labelGroupVariantList.isNotEmpty()

        val halalLabel = if (hasHalalLabel) WITH_HALAL_LABEL else WITHOUT_HALAL_LABEL
        val slashPriceLabel = if (hasSlashPrice) SLASH_PRICE else NORMAL_PRICE
        val hasVariantLabel = if (hasVariant) WITH_VARIANT else WITHOUT_VARIANT

        return "$halalLabel - $slashPriceLabel - $hasVariantLabel"
    }

    private fun TokoNowProductCardUiModel.getAsImpressionObjectDataLayer(position: Int) =
        getAsClickObjectDataLayer(position).also {
            it["list"] = TOKONOW_CATEGORY_PAGE_PAST_PURCHASE_WIDGET
        }

    private fun TokoNowProductCardUiModel.getAsClickObjectDataLayer(position: Int) =
        getAsObjectDataLayer().also {
            it["position"] = position
        }

    private fun TokoNowProductCardUiModel.getAsObjectDataLayer() =
        DataLayer.mapOf(
            "brand", NONE_OTHER,
            "category", NONE_OTHER,
            "id", productId,
            "name", product.productName,
            "price", product.formattedPrice,
            "variant", NONE_OTHER,
        )

    fun sendRepurchaseWidgetClickEvent(
        repurchaseProduct: TokoNowProductCardUiModel,
        position: Int,
        userId: String,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, PRODUCT_CLICK,
                EVENT_ACTION, CLICK_PRODUCT_ON_PAST_PURCHASE_WIDGET,
                EVENT_CATEGORY, TOKONOW_DASH_CATEGORY_PAGE,
                EVENT_LABEL, createEventLabelRepurchaseWidget(repurchaseProduct),
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    CLICK, DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(
                            LIST, TOKONOW_CATEGORY_PAGE_PAST_PURCHASE_WIDGET
                        ),
                        PRODUCTS, DataLayer.listOf(
                            repurchaseProduct.getAsClickObjectDataLayer(position)
                        )
                    )
                ),
            )
        )
    }

    fun sendRepurchaseWidgetAddToCartEvent(
        repurchaseProduct: TokoNowProductCardUiModel,
        quantity: Int,
        cartId: String,
        userId: String,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, SearchCategoryTrackingConst.Event.ADD_TO_CART,
                EVENT_ACTION, CLICK_ATC_ON_PAST_PURCHASE_WIDGET,
                EVENT_CATEGORY, TOKONOW_DASH_CATEGORY_PAGE,
                EVENT_LABEL, createEventLabelRepurchaseWidget(repurchaseProduct),
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    ADD, DataLayer.mapOf(
                        PRODUCTS, DataLayer.listOf(
                            repurchaseProduct.getAsATCObjectDataLayer(quantity, cartId)
                        )
                    ),
                    CURRENCYCODE, IDR,
                ),
            )
        )
    }

    private fun TokoNowProductCardUiModel.getAsATCObjectDataLayer(quantity: Int, cartId: String) =
        getAsObjectDataLayer().also {
            it["category_id"] = ""
            it["dimension40"] = TOKONOW_CATEGORY_PAGE_PAST_PURCHASE_WIDGET
            it["dimension45"] = cartId
            it["dimension79"] = ""
            it["dimension80"] = ""
            it["dimension81"] = ""
            it["dimension82"] = ""
            it["dimension83"] = ""
            it["dimension84"] = ""
            it["dimension96"] = ""
            it["quantity"] = quantity
            it["shop_id"] = shopId
            it["shop_name"] = ""
            it["shop_type"] = ""
        }

    fun sendRecommendationSeeAllClickEvent(categoryIdTracking: String) {
        sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_VIEW_ALL_ON_TOKONOW_CLP_RECOMMENDATION,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryIdTracking,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

}
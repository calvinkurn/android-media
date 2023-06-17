package com.tokopedia.tokopedianow.oldcategory.analytics

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.productcard.compact.similarproduct.analytic.ProductCardCompactSimilarProductAnalyticsConstants.TRACKER_ID_ADD_TO_CART_CATEGORY
import com.tokopedia.productcard.compact.similarproduct.analytic.ProductCardCompactSimilarProductAnalyticsConstants.TRACKER_ID_CLICK_CLOSE_BOTTOMSHEET_CATEGORY
import com.tokopedia.productcard.compact.similarproduct.analytic.ProductCardCompactSimilarProductAnalyticsConstants.TRACKER_ID_CLICK_PRODUCT_CATEGORY
import com.tokopedia.productcard.compact.similarproduct.analytic.ProductCardCompactSimilarProductAnalyticsConstants.TRACKER_ID_CLICK_SIMILAR_PRODUCT_BUTTON_CATEGORY
import com.tokopedia.productcard.compact.similarproduct.analytic.ProductCardCompactSimilarProductAnalyticsConstants.TRACKER_ID_VIEW_EMPTY_STATE_CATEGORY
import com.tokopedia.productcard.compact.similarproduct.analytic.ProductCardCompactSimilarProductAnalyticsConstants.TRACKER_ID_VIEW_SIMILAR_PRODUCT_BOTTOMSHEET_CATEGORY
import com.tokopedia.productcard.compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.ADD_QUANTITY_ON_BOTTOM_SHEET
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.APPLY_CATEGORY_FILTER
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_APPLY_FILTER
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_ATC_ON_PAST_PURCHASE_WIDGET
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_BANNER
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_CART_BUTTON_TOP_NAV
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_CATEGORY_FILTER
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_FILTER
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_LEVEL_2_FILTER_WIDGET
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_LIHAT_CATEGORY_LAINNYA
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_PILIH_VARIANT_BUTTON
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_PRODUCT
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_PRODUCT_ON_PAST_PURCHASE_WIDGET
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_QUICK_FILTER
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_SEARCH_BAR
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_SEMUA_KATEGORI
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.CLICK_VIEW_ALL_ON_TOKONOW_CLP_RECOMMENDATION
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.EVENT_ACTION_CLICK_ADD_TO_CART
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.EVENT_ACTION_CLICK_CLOSE_BOTTOMSHEET
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.EVENT_ACTION_CLICK_PRODUCT
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.EVENT_ACTION_CLICK_SIMILAR_PRODUCT_BTN
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.EVENT_ACTION_IMPRESSION_BOTTOMSHEET
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.EVENT_ACTION_IMPRESSION_EMPTY_STATE
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.IMPRESSION_BANNER
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.IMPRESSION_ON_PAST_PURCHASE_WIDGET
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.IMPRESSION_PRODUCT
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.REMOVE_QUANTITY_ON_BOTTOM_SHEET
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Category.TOKONOW_CATEGORY_PAGE
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Category.TOKONOW_DASH_CATEGORY_PAGE
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Category.TOP_NAV_TOKONOW_CATEGORY_PAGE
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Event.CLICK_TOP_NAV
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.CATEGORY
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.CATEGORY_ID
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.LABEL_GROUP_HALAL
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.NORMAL_PRICE
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.PRODUCT_ID
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.SLASH_PRICE
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.TOKONOW_CATEGORY_ORGANIC
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.TOKONOW_CATEGORY_PAGE_PAST_PURCHASE_WIDGET
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.TOKONOW_CATEGORY_SCREEN
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.TOKONOW_OOC_SCREEN_NAME
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.WITHOUT_HALAL_LABEL
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.WITHOUT_VARIANT
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.WITH_HALAL_LABEL
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.WITH_VARIANT
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.TRACKER_ID.TRACKER_ID_CLICK_CATEGORY_MENU_WIDGET
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.TRACKER_ID.TRACKER_ID_CLICK_SEE_ALL_CATEGORY
import com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.TRACKER_ID.TRACKER_ID_IMPRESSION_CATEGORY_MENU_WIDGET
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_ACCESS_PHOTO_MEDIA_FILES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_ADD_TO_WISHLIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_CATEGORY_MENU_WIDGET
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_CHANNEL_SHARE_BOTTOM_SHEET_SCREENSHOT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_CLOSE_SCREENSHOT_SHARE_BOTTOM_SHEET
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_CLOSE_SHARE_BOTTOM_SHEET
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_REMOVE_FROM_WISHLIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_SEE_ALL_CATEGORY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_SHARE_WIDGET_BUTTON
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_IMPRESSION_CATEGORY_MENU_WIDGET
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_IMPRESSION_CHANNEL_SHARE_BOTTOM_SHEET_SCREENSHOT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_IMPRESSION_SHARING_CHANNEL
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOKOPEDIA_CATEGORY_PAGE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOP_NAV_TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_COMMUNICATION
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_PG_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRODUCT_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_PHYSICAL_GOODS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_SHARING_EXPERIENCE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.DEFAULT_HEADER_CATEGORY_MENU
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.DEFAULT_NULL_VALUE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.PAGE_NAME_TOKOPEDIA_NOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.SCREEN_NAME_TOKONOW_OOC
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getDataLayer
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.common.util.StringUtil.getOrDefaultZeroString
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
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
import com.tokopedia.track.constant.TrackerConstant
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

        const val EVENT_ACTION_IMPRESSION_BOTTOMSHEET = "impression product oos bottomsheet"
        const val EVENT_ACTION_CLICK_SIMILAR_PRODUCT_BTN = "click produk serupa on product card"
        const val EVENT_ACTION_CLICK_ADD_TO_CART = "add to cart product oos bottomsheet"
        const val EVENT_ACTION_CLICK_CLOSE_BOTTOMSHEET = "click close bottomsheet"
        const val EVENT_ACTION_IMPRESSION_EMPTY_STATE = "impression empty state bottomsheet"
        const val EVENT_ACTION_CLICK_PRODUCT = "click product oos bottomsheet"
    }

    object Category {
        const val TOP_NAV_TOKONOW_CATEGORY_PAGE =  "top nav - tokonow category page"
        const val TOKONOW_CATEGORY_PAGE = "tokonow category page"
        const val TOKONOW_DASH_CATEGORY_PAGE = "tokonow - category page"
    }

    object Misc {
        const val CATEGORY = "category"
        const val CATEGORY_ID = "categoryId"
        const val PRODUCT_ID = "productId"
        const val TOKONOW_CATEGORY_ORGANIC = "/tokonow - category - %s"
        const val RECOM_LIST_PAGE = "clp_product"
        const val RECOM_LIST_PAGE_NON_OOC = "/${com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Misc.RECOM_LIST_PAGE} - tokonow - rekomendasi untuk anda"
        const val WITH_HALAL_LABEL = "with halal label"
        const val WITHOUT_HALAL_LABEL = "without halal label"
        const val SLASH_PRICE = "slash price"
        const val NORMAL_PRICE = "normal price"
        const val WITH_VARIANT = "with variant"
        const val WITHOUT_VARIANT = "without variant"
        const val TOKONOW_CATEGORY_PAGE_PAST_PURCHASE_WIDGET =
            "/tokonow - category page - past_purchase_widget"
        const val LABEL_GROUP_HALAL = "Halal"
        const val TOKONOW_CATEGORY_SCREEN = "tokonow/category/%s"
        const val TOKONOW_OOC_SCREEN_NAME = "tokonow/category"
        const val PREFIX_ALL = "Semua"
    }

    object TRACKER_ID {
        const val TRACKER_ID_IMPRESSION_CATEGORY_MENU_WIDGET = "40825"
        const val TRACKER_ID_CLICK_CATEGORY_MENU_WIDGET = "40826"
        const val TRACKER_ID_CLICK_SEE_ALL_CATEGORY = "40827"
    }

    fun sendGeneralEvent(dataLayer: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(dataLayer)
    }

    fun sendSearchBarClickEvent(categoryId: String) {
        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendGeneralEvent(
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
        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendGeneralEvent(
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
            com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.getChannelGridAsObjectDataLayer(
                this,
                channelGrid,
                index
            )
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
        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendGeneralEvent(
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
                PRODUCT_ID, productItemDataView.productCardModel.productId,
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
                "id", productCardModel.productId,
                "name", productCardModel.name,
                "price", productCardModel.price.getDigits(),
                "variant", NONE_OTHER,
                "position", position,
                "dimension98",(!productCardModel.isOos()).toString(),
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
                PRODUCT_ID, productItemDataView.productCardModel.productId,
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
        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_LEVEL_2_FILTER_WIDGET,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, "$categoryId - $filterCategoryId",
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendAisleClickEvent(categoryId: String, aisleCategoryId: String) {
        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_LIHAT_CATEGORY_LAINNYA,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, "$categoryId - $aisleCategoryId",
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendFilterClickEvent(categoryId: String) {
        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendQuickFilterClickEvent(categoryId: String) {
        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_QUICK_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendApplySortFilterEvent(categoryId: String) {
        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_APPLY_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendOpenCategoryL3FilterEvent(categoryId: String) {
        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_CATEGORY_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendApplyCategoryL3FilterEvent(categoryId: String, filterCategoryId: String) {
        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, APPLY_CATEGORY_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, "$categoryId - $filterCategoryId",
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
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
                EVENT_ACTION,
                com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.Action.ADD_TO_CART,
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
                "id", productCardModel.productId,
                "name", productCardModel.name,
                "price", productCardModel.price.getDigits(),
                "quantity", quantity,
                "shop_id", shop.id,
                "shop_name", shop.name,
                "shop_type", TOKO_NOW,
                "variant", NONE_OTHER
        )
    }

    fun sendChooseVariantClickEvent(categoryId: String) {
        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, CLICK_PILIH_VARIANT_BUTTON,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }



    fun sendIncreaseQtyEvent(categoryId: String) {
        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, ADD_QUANTITY_ON_BOTTOM_SHEET,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
    }

    fun sendDecreaseQtyEvent(categoryId: String) {
        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, EVENT_CLICK_TOKONOW,
                EVENT_ACTION, REMOVE_QUANTITY_ON_BOTTOM_SHEET,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                KEY_BUSINESS_UNIT, BUSINESS_UNIT_PHYSICAL_GOODS,
                KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE,
            )
        )
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
            EVENT_LABEL,
            com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.createEventLabelRepurchaseWidget(
                repurchaseProduct
            ),
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
                EVENT_LABEL,
                com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.createEventLabelRepurchaseWidget(
                    repurchaseProduct
                ),
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
                EVENT_LABEL,
                com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.createEventLabelRepurchaseWidget(
                    repurchaseProduct
                ),
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
        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendGeneralEvent(
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

    fun sendOpenScreenTracking(slug: String, id: String, name: String, isLoggedInStatus: Boolean) {
        TokoNowCommonAnalytics.onOpenScreen(
            isLoggedInStatus = isLoggedInStatus,
            screenName =  String.format(TOKONOW_CATEGORY_SCREEN, slug),
            additionalMap = mutableMapOf(
                Pair(CATEGORY, name),
                Pair(CATEGORY_ID, id)
            )
        )
    }

    fun sendOOCOpenScreenTracking(isLoggedInStatus: Boolean) {
        TokoNowCommonAnalytics.onOpenScreen(
            isLoggedInStatus = isLoggedInStatus,
            screenName = SCREEN_NAME_TOKONOW_OOC + TOKONOW_OOC_SCREEN_NAME
        )
    }

    /* Thanos : https://mynakama.tokopedia.com/datatracker/requestdetail/1963 */

    // - 1
    fun trackClickShareButtonTopNav(userId: String, categoryIdLvl1: String, categoryIdLvl2: String, categoryIdLvl3: String, currentCategoryId: String) {
        val label = "${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val pageSource = "$PAGE_NAME_TOKOPEDIA_NOW.$DEFAULT_NULL_VALUE.$DEFAULT_NULL_VALUE.$currentCategoryId"

        val dataLayer = getDataLayer(
            EVENT_CLICK_COMMUNICATION,
            EVENT_ACTION_CLICK_SHARE_WIDGET_BUTTON,
            EVENT_CATEGORY_TOP_NAV_TOKOPEDIA_NOW,
            label
        )

        dataLayer[TokoNowCommonAnalyticConstants.KEY.KEY_PAGE_SOURCE] = pageSource
        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 2
    fun trackClickCloseShareBottomSheet(userId: String, categoryIdLvl1: String, categoryIdLvl2: String, categoryIdLvl3: String) {
        val label = "${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val dataLayer = getDataLayer(
            EVENT_CLICK_COMMUNICATION,
            EVENT_ACTION_CLICK_CLOSE_SHARE_BOTTOM_SHEET,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 3
    fun trackClickChannelShareBottomSheet(channel: String, userId: String, categoryIdLvl1: String, categoryIdLvl2: String, categoryIdLvl3: String) {
        val label = "$channel - ${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val dataLayer = getDataLayer(
            EVENT_CLICK_TOKONOW,
            TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_SHARING_CHANNEL,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 4
    fun trackImpressChannelShareBottomSheet(userId: String, categoryIdLvl1: String, categoryIdLvl2: String, categoryIdLvl3: String) {
        val label = "${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val dataLayer = getDataLayer(
            TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_TOKONOW_IRIS,
            EVENT_ACTION_IMPRESSION_SHARING_CHANNEL,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 5
    fun trackImpressChannelShareBottomSheetScreenShot(userId: String, categoryIdLvl1: String, categoryIdLvl2: String, categoryIdLvl3: String) {
        val label = "${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val dataLayer = getDataLayer(
            TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_TOKONOW_IRIS,
            EVENT_ACTION_IMPRESSION_CHANNEL_SHARE_BOTTOM_SHEET_SCREENSHOT,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 6
    fun trackClickCloseScreenShotShareBottomSheet(userId: String, categoryIdLvl1: String, categoryIdLvl2: String, categoryIdLvl3: String) {
        val label = "${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val dataLayer = getDataLayer(
            EVENT_CLICK_TOKONOW,
            EVENT_ACTION_CLICK_CLOSE_SCREENSHOT_SHARE_BOTTOM_SHEET,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 7
    fun trackClickChannelShareBottomSheetScreenshot(channel: String, userId: String, categoryIdLvl1: String, categoryIdLvl2: String, categoryIdLvl3: String) {
        val label = "$channel - ${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val dataLayer = getDataLayer(
            EVENT_CLICK_TOKONOW,
            EVENT_ACTION_CLICK_CHANNEL_SHARE_BOTTOM_SHEET_SCREENSHOT,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    // - 8
    fun trackClickAccessMediaAndFiles(accessText: String, userId: String, categoryIdLvl1: String, categoryIdLvl2: String, categoryIdLvl3: String) {
        val label = "$accessText - ${categoryIdLvl3.getOrDefaultZeroString()} - ${categoryIdLvl2.getOrDefaultZeroString()} - ${categoryIdLvl1.getOrDefaultZeroString()}"

        val dataLayer = getDataLayer(
            EVENT_CLICK_COMMUNICATION,
            EVENT_ACTION_CLICK_ACCESS_PHOTO_MEDIA_FILES,
            EVENT_CATEGORY_TOKOPEDIA_NOW,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_SHARING_EXPERIENCE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_USER_ID] = userId.getOrDefaultZeroString()

        getTracker().sendGeneralEvent(dataLayer)
    }

    fun trackClickAddToWishlist(warehouseId: String, productId: String){
        val label = "$warehouseId - $productId"

        val dataLayer = getDataLayer(
            EVENT_CLICK_GROCERIES,
            EVENT_ACTION_CLICK_ADD_TO_WISHLIST,
            EVENT_CATEGORY_TOKOPEDIA_CATEGORY_PAGE,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_TRACKER_ID] = TokoNowCommonAnalyticConstants.TRACKER_ID.TRACKER_ID_ADD_TO_WISHLIST_CATEGORY
        dataLayer[KEY_PRODUCT_ID] = productId

        getTracker().sendGeneralEvent(dataLayer)
    }

    fun trackClickRemoveFromWishlist(warehouseId: String, productId: String){
        val label = "$warehouseId - $productId"

        val dataLayer = getDataLayer(
            EVENT_CLICK_GROCERIES,
            EVENT_ACTION_CLICK_REMOVE_FROM_WISHLIST,
            EVENT_CATEGORY_TOKOPEDIA_CATEGORY_PAGE,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_TRACKER_ID] = TokoNowCommonAnalyticConstants.TRACKER_ID.TRACKER_ID_REMOVE_FROM_WISHLIST_CATEGORY
        dataLayer[KEY_PRODUCT_ID] = productId

        getTracker().sendGeneralEvent(dataLayer)
    }

    /* Similar Product Bottomsheet Trackers*/

    fun trackClickSimilarProductBtn(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        val dataLayer = com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.createGeneralDataLayer(
            event = EVENT_CLICK_GROCERIES,
            action = EVENT_ACTION_CLICK_SIMILAR_PRODUCT_BTN,
            label = "$warehouseId - $productIdTriggered",
            userId = userId
        ).apply {
            putString(KEY_PRODUCT_ID, productIdTriggered)
            putString(KEY_TRACKER_ID, TRACKER_ID_CLICK_SIMILAR_PRODUCT_BUTTON_CATEGORY)
        }

        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_GROCERIES,
            dataLayer = dataLayer
        )
    }

    fun trackImpressionBottomSheet(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String
    ) {
        val items = arrayListOf(
            com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.createProductItemDataLayer(
                index = similarProduct.position,
                id = similarProduct.id,
                name = similarProduct.name,
                price = similarProduct.priceFmt.getDigits().orZero().toFloat(),
                category = similarProduct.categoryName
            )
        )

        val dataLayer = com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.createGeneralDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = EVENT_ACTION_IMPRESSION_BOTTOMSHEET,
            label = "$warehouseId - $productIdTriggered - ${similarProduct.id}",
            userId = userId
        ).apply {
            putString(KEY_PRODUCT_ID, similarProduct.id)
            putString(KEY_TRACKER_ID, TRACKER_ID_VIEW_SIMILAR_PRODUCT_BOTTOMSHEET_CATEGORY)
            putString(KEY_ITEM_LIST, "/tokonow - product card - similar product recom")
            putParcelableArrayList(KEY_ITEMS, ArrayList(items))
        }

        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendEnhanceEcommerceEvent(
            eventName = EVENT_VIEW_PG_IRIS,
            dataLayer = dataLayer
        )
    }

    fun trackClickProduct(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String
    ) {
        val items = arrayListOf(
            com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.createProductItemDataLayer(
                index = similarProduct.position,
                id = similarProduct.id,
                name = similarProduct.name,
                price = similarProduct.priceFmt.getDigits().orZero().toFloat(),
                category = similarProduct.categoryName
            )
        )

        val dataLayer = com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.createGeneralDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_PRODUCT,
            label = "$warehouseId - $productIdTriggered - ${similarProduct.id}",
            userId = userId
        ).apply {
            putString(KEY_PRODUCT_ID, similarProduct.id)
            putString(KEY_TRACKER_ID, TRACKER_ID_CLICK_PRODUCT_CATEGORY)
            putString(KEY_ITEM_LIST, "/tokonow - product card - similar product recom")
            putParcelableArrayList(KEY_ITEMS, ArrayList(items))
        }

        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendEnhanceEcommerceEvent(
            eventName = EVENT_SELECT_CONTENT,
            dataLayer = dataLayer
        )
    }

    fun trackClickAddToCart(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String,
        newQuantity: Int
    ) {
        val items = arrayListOf(
            com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.createAtcProductItemDataLayer(
                id = similarProduct.id,
                name = similarProduct.name,
                price = similarProduct.priceFmt,
                categoryName = similarProduct.categoryName,
                categoryId = similarProduct.categoryId,
                quantity = newQuantity.toString(),
                shopId = similarProduct.shopId,
                shopName = similarProduct.shopName
            )
        )

        val dataLayer = com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.createGeneralDataLayer(
            event = TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART,
            action = EVENT_ACTION_CLICK_ADD_TO_CART,
            label = "$warehouseId - $productIdTriggered - ${similarProduct.id}",
            userId = userId
        ).apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_PRODUCT_ID, similarProduct.id)
            putString(KEY_TRACKER_ID, TRACKER_ID_ADD_TO_CART_CATEGORY)
            putParcelableArrayList(KEY_ITEMS, items)
        }

        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendEnhanceEcommerceEvent(
            eventName = TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART,
            dataLayer = dataLayer
        )
    }

    fun trackClickCloseBottomsheet(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        val dataLayer = com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.createGeneralDataLayer(
            event = EVENT_CLICK_GROCERIES,
            action = EVENT_ACTION_CLICK_CLOSE_BOTTOMSHEET,
            label = "$warehouseId - $productIdTriggered",
            userId = userId
        ).apply {
            putString(KEY_TRACKER_ID, TRACKER_ID_CLICK_CLOSE_BOTTOMSHEET_CATEGORY)
        }

        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_GROCERIES,
            dataLayer = dataLayer
        )
    }

    fun trackImpressionEmptyState(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        val dataLayer = com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.createGeneralDataLayer(
            event = TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES,
            action = EVENT_ACTION_IMPRESSION_EMPTY_STATE,
            label = "$warehouseId - $productIdTriggered",
            userId = userId
        ).apply {
            putString(KEY_TRACKER_ID, TRACKER_ID_VIEW_EMPTY_STATE_CATEGORY)
        }

        com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.sendEnhanceEcommerceEvent(
            eventName = TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES,
            dataLayer = dataLayer
        )
    }

    /**
     * NOW! SeeAllCategory Page Tracker
     * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3695
     */

    fun trackImpressCategoryMenu(
        categoryId: String,
        categoryName: String,
        warehouseId: String,
        position: Int,
        userId: String
    ) {
        val newPosition = position.getTrackerPosition()
        val dataLayer = TokoNowCommonAnalytics.getEcommerceDataLayerCategoryMenu(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMPRESSION_CATEGORY_MENU_WIDGET,
            category = TOKONOW_CATEGORY_PAGE,
            label = "",
            trackerId = TRACKER_ID_IMPRESSION_CATEGORY_MENU_WIDGET,
            promotions = arrayListOf(
                TokoNowCommonAnalytics.getEcommerceDataLayerCategoryMenuPromotion(
                    categoryId = categoryId,
                    categoryName = categoryName,
                    warehouseId = warehouseId,
                    position = newPosition,
                    itemName = com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.getCategoryMenuItemName(
                        position = newPosition,
                        headerName = DEFAULT_HEADER_CATEGORY_MENU
                    )
                )
            ),
            warehouseId = warehouseId,
            userId = userId
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun trackClickCategoryMenu(
        categoryId: String,
        categoryName: String,
        warehouseId: String,
        position: Int,
        userId: String
    ) {
        val newPosition = position.getTrackerPosition()
        val dataLayer = TokoNowCommonAnalytics.getEcommerceDataLayerCategoryMenu(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_CATEGORY_MENU_WIDGET,
            category = TOKONOW_CATEGORY_PAGE,
            label = "",
            trackerId = TRACKER_ID_CLICK_CATEGORY_MENU_WIDGET,
            promotions = arrayListOf(
                TokoNowCommonAnalytics.getEcommerceDataLayerCategoryMenuPromotion(
                    categoryId = categoryId,
                    categoryName = categoryName,
                    warehouseId = warehouseId,
                    position = newPosition,
                    itemName = com.tokopedia.tokopedianow.oldcategory.analytics.CategoryTracking.getCategoryMenuItemName(
                        position = newPosition,
                        headerName = DEFAULT_HEADER_CATEGORY_MENU
                    )
                )
            ),
            warehouseId = warehouseId,
            userId = userId
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackClickSeeAllCategory() {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_GROCERIES,
            action = EVENT_ACTION_CLICK_SEE_ALL_CATEGORY,
            category = TOKONOW_CATEGORY_PAGE,
            label = ""
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_TRACKER_ID] = TRACKER_ID_CLICK_SEE_ALL_CATEGORY

        getTracker().sendGeneralEvent(dataLayer)
    }

    private fun getCategoryMenuItemName(position: Int, headerName: String): String {
        return "/ - p$position - now clp - category widget - $headerName"
    }

    private fun createGeneralDataLayer(event: String, action: String, label: String = TokoNowCommonAnalyticConstants.VALUE.DEFAULT_EMPTY_VALUE, userId: String): Bundle {
        return Bundle().apply {
            putString(TrackerConstant.EVENT, event)
            putString(TrackerConstant.EVENT_ACTION, action)
            putString(TrackerConstant.EVENT_CATEGORY, EVENT_CATEGORY_TOKOPEDIA_CATEGORY_PAGE)
            putString(TrackerConstant.EVENT_LABEL, label)
            putString(TrackerConstant.BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(TrackerConstant.CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(TrackerConstant.USERID, userId)
        }
    }

    private fun createProductItemDataLayer(
        index: Int = 0,
        id: String = "",
        name: String = "",
        price: Float = 0f,
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
            putFloat(TokoNowCommonAnalyticConstants.KEY.KEY_PRICE, price)
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

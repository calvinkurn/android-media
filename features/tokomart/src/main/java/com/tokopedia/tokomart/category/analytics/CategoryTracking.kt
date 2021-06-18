package com.tokopedia.tokomart.category.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.APPLY_CATEGORY_FILTER
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.CLICK_APPLY_FILTER
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.CLICK_BANNER
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.CLICK_CART_BUTTON_TOP_NAV
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.CLICK_CATEGORY_FILTER
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.CLICK_FILTER
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.CLICK_LEVEL_2_FILTER_WIDGET
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.CLICK_LIHAT_CATEGORY_LAINNYA
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.CLICK_PRODUCT
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.CLICK_QUICK_FILTER
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.CLICK_SEARCH_BAR
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.CLICK_SEMUA_KATEGORI
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.IMPRESSION_BANNER
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.IMPRESSION_PRODUCT
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Category.TOKONOW_CATEGORY_PAGE
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Category.TOP_NAV_TOKONOW_CATEGORY_PAGE
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Event.CLICK_TOP_NAV
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Misc.PRODUCT_ID
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Misc.TOKONOW_CATEGORY_ORGANIC
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.MISC.BUSINESSUNIT
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.MISC.CURRENTSITE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_VALUE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.CURRENT_SITE_VALUE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.EVENT_CLICK_VALUE
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.ACTION_FIELD
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.CLICK
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.CURRENCYCODE
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.ECOMMERCE
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.IDR
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.IMPRESSIONS
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.LIST
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.PRODUCTS
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.PROMOTIONS
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Event.PRODUCT_CLICK
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Event.PRODUCT_VIEW
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Event.PROMO_CLICK
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Event.PROMO_VIEW
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Misc.HOME_AND_BROWSE
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Misc.NONE_OTHER
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Misc.USER_ID
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap

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
    }

    object Category {
        const val TOP_NAV_TOKONOW_CATEGORY_PAGE =  "top nav - tokonow category page"
        const val TOKONOW_CATEGORY_PAGE = "tokonow category page"
    }

    object Misc {
        const val PRODUCT_ID = "productId"
        const val TOKONOW_CATEGORY_ORGANIC = "/tokonow - category - organic"
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
                        BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                        CURRENTSITE, CURRENT_SITE_VALUE,
                )
        )
    }

    fun sendCartClickEvent(categoryId: String) {
        sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT, EVENT_CLICK_VALUE,
                        EVENT_ACTION, CLICK_CART_BUTTON_TOP_NAV,
                        EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                        EVENT_LABEL, categoryId,
                        BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                        CURRENTSITE, CURRENT_SITE_VALUE,
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
                BUSINESSUNIT, HOME_AND_BROWSE,
                CURRENTSITE, CURRENT_SITE_VALUE,
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
        val name = "/tokonow - category - $promoName"

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
                BUSINESSUNIT, HOME_AND_BROWSE,
                CURRENTSITE, CURRENT_SITE_VALUE,
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
                        EVENT, EVENT_CLICK_VALUE,
                        EVENT_ACTION, CLICK_SEMUA_KATEGORI,
                        EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                        EVENT_LABEL, categoryId,
                        BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                        CURRENTSITE, CURRENT_SITE_VALUE,
                )
        )
    }

    fun sendProductImpressionEvent(
            trackingQueue: TrackingQueue,
            productItemDataView: ProductItemDataView,
            categoryId: String,
            userId: String,
    ) {
        val map = DataLayer.mapOf(
                EVENT, PRODUCT_VIEW,
                EVENT_ACTION, IMPRESSION_PRODUCT,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                CURRENTSITE, CURRENT_SITE_VALUE,
                USER_ID, userId,
                PRODUCT_ID, productItemDataView.id,
                ECOMMERCE, DataLayer.mapOf(
                    CURRENCYCODE, IDR,
                    IMPRESSIONS, DataLayer.listOf(productItemDataView.getAsImpressionClickDataLayer())
                )
        ) as HashMap<String, Any>

        trackingQueue.putEETracking(map)
    }

    private fun ProductItemDataView.getAsImpressionClickDataLayer(): Any {
        return DataLayer.mapOf(
                "brand", NONE_OTHER,
                "category", NONE_OTHER,
                "id", id,
                "name", name,
                "price", priceInt,
                "variant", NONE_OTHER,
                "position", position,
                "list", TOKONOW_CATEGORY_ORGANIC
        )
    }

    fun sendProductClickEvent(
            productItemDataView: ProductItemDataView,
            categoryId: String,
            userId: String,
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                EVENT, PRODUCT_CLICK,
                EVENT_ACTION, CLICK_PRODUCT,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                CURRENTSITE, CURRENT_SITE_VALUE,
                USER_ID, userId,
                PRODUCT_ID, productItemDataView.id,
                ECOMMERCE, DataLayer.mapOf(
                    CLICK, DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(LIST, TOKONOW_CATEGORY_ORGANIC),
                        PRODUCTS, DataLayer.listOf(productItemDataView.getAsImpressionClickDataLayer())
                    )
                ),
            )
        )
    }

    fun sendApplyCategoryL2FilterEvent(categoryId: String, filterCategoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
            EVENT, EVENT_CLICK_VALUE,
            EVENT_ACTION, CLICK_LEVEL_2_FILTER_WIDGET,
            EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
            EVENT_LABEL, "$categoryId - $filterCategoryId",
            BUSINESSUNIT, BUSINESS_UNIT_VALUE,
            CURRENTSITE, CURRENT_SITE_VALUE,
        ))
    }

    fun sendAisleClickEvent(categoryId: String, aisleCategoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
            EVENT, EVENT_CLICK_VALUE,
            EVENT_ACTION, CLICK_LIHAT_CATEGORY_LAINNYA,
            EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
            EVENT_LABEL, "$categoryId - $aisleCategoryId",
            BUSINESSUNIT, BUSINESS_UNIT_VALUE,
            CURRENTSITE, CURRENT_SITE_VALUE,
        ))
    }

    fun sendFilterClickEvent(categoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_VALUE,
                EVENT_ACTION, CLICK_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                CURRENTSITE, CURRENT_SITE_VALUE,
        ))
    }

    fun sendQuickFilterClickEvent(categoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_VALUE,
                EVENT_ACTION, CLICK_QUICK_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                CURRENTSITE, CURRENT_SITE_VALUE,
        ))
    }

    fun sendApplySortFilterEvent(categoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_VALUE,
                EVENT_ACTION, CLICK_APPLY_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                CURRENTSITE, CURRENT_SITE_VALUE,
        ))
    }

    fun sendOpenCategoryL3FilterEvent(categoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_VALUE,
                EVENT_ACTION, CLICK_CATEGORY_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, categoryId,
                BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                CURRENTSITE, CURRENT_SITE_VALUE,
        ))
    }

    fun sendApplyCategoryL3FilterEvent(categoryId: String, filterCategoryId: String) {
        sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_CLICK_VALUE,
                EVENT_ACTION, APPLY_CATEGORY_FILTER,
                EVENT_CATEGORY, TOKONOW_CATEGORY_PAGE,
                EVENT_LABEL, "$categoryId - $filterCategoryId",
                BUSINESSUNIT, BUSINESS_UNIT_VALUE,
                CURRENTSITE, CURRENT_SITE_VALUE,
        ))
    }
}
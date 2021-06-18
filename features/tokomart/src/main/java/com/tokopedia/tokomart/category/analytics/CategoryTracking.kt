package com.tokopedia.tokomart.category.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.CLICK_BANNER
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.CLICK_CART_BUTTON_TOP_NAV
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.CLICK_SEARCH_BAR
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Action.IMPRESSION_BANNER
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Category.TOKONOW_CATEGORY_PAGE
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Category.TOP_NAV_TOKONOW_CATEGORY_PAGE
import com.tokopedia.tokomart.category.analytics.CategoryTracking.Event.CLICK_TOP_NAV
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.MISC.BUSINESSUNIT
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.MISC.CURRENTSITE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_VALUE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.CURRENT_SITE_VALUE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.EVENT_CLICK_VALUE
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.ECOMMERCE
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.ECommerce.PROMOTIONS
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Event.PROMO_CLICK
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Event.PROMO_VIEW
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Misc.HOME_AND_BROWSE
import com.tokopedia.tokomart.searchcategory.analytics.SearchCategoryTrackingConst.Misc.USER_ID
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL

object CategoryTracking {

    object Event {
        const val CLICK_TOP_NAV = "clickTopNav"
    }

    object Action {
        const val CLICK_SEARCH_BAR = "click search bar"
        const val CLICK_CART_BUTTON_TOP_NAV = "click cart button top nav"
        const val IMPRESSION_BANNER = "impression banner"
        const val CLICK_BANNER = "click banner"
    }

    object Category {
        const val TOP_NAV_TOKONOW_CATEGORY_PAGE =  "top nav - tokonow category page"
        const val TOKONOW_CATEGORY_PAGE = "tokonow category page"
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
}
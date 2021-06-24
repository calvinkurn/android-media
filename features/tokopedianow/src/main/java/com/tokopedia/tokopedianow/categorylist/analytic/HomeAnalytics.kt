package com.tokopedia.tokopedianow.categorylist.analytic

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.tokopedianow.categorylist.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_ALL_CATEGORY
import com.tokopedia.tokopedianow.categorylist.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_CART_BUTTON
import com.tokopedia.tokopedianow.categorylist.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_CATEGORY_ON_CATEGORY
import com.tokopedia.tokopedianow.categorylist.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SEARCH_BAR
import com.tokopedia.tokopedianow.categorylist.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SHARE_BUTTON
import com.tokopedia.tokopedianow.categorylist.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SLIDER_BANNER
import com.tokopedia.tokopedianow.categorylist.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_SLIDER_BANNER
import com.tokopedia.tokopedianow.categorylist.analytic.HomeAnalytics.CATEGORY.EVENT_CATEGORY_HOME_PAGE
import com.tokopedia.tokopedianow.categorylist.analytic.HomeAnalytics.VALUE.NAME_PROMOTION
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOP_NAV
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_AFFINITY_LABEL
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_CREATIVE_NAME
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_CREATIVE_SLOT
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_DIMENSION_104
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_DIMENSION_38
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_DIMENSION_49
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_DIMENSION_79
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_DIMENSION_82
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_ITEM_ID
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_ITEM_NAME
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_PROMOTIONS
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokonowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics

class HomeAnalytics {

    object CATEGORY{
        const val EVENT_CATEGORY_HOME_PAGE = "tokonow - homepage"
    }

    object ACTION{
        const val EVENT_ACTION_CLICK_SEARCH_BAR = "click search bar on homepage"
        const val EVENT_ACTION_CLICK_CART_BUTTON = "click cart button on homepage"
        const val EVENT_ACTION_CLICK_SHARE_BUTTON = "click share button on homepage"
        const val EVENT_ACTION_CLICK_SLIDER_BANNER = "click slider banner"
        const val EVENT_ACTION_CLICK_ALL_CATEGORY = "click view all category widget"
        const val EVENT_ACTION_CLICK_CATEGORY_ON_CATEGORY = "click category on category widget"
        const val EVENT_ACTION_IMPRESSION_SLIDER_BANNER = "impression slider banner"
    }

    object VALUE {
        const val NAME_PROMOTION = "tokonow - p1 - promo"
    }

    fun onClickSearchBar() {
        hitCommonHomeTracker(
                getDataLayer(
                        event = EVENT_CLICK_TOKONOW,
                        action = EVENT_ACTION_CLICK_SEARCH_BAR,
                        category = EVENT_CATEGORY_TOP_NAV
                )
        )
    }

    fun onClickCartButton() {
        hitCommonHomeTracker(
                getDataLayer(
                        event = EVENT_CLICK_TOKONOW,
                        action = EVENT_ACTION_CLICK_CART_BUTTON,
                        category = EVENT_CATEGORY_TOP_NAV
                )
        )
    }

    //this is not P0 and right now we will not implement it
    fun onClickShareButton() {
        hitCommonHomeTracker(
                getDataLayer(
                        event = EVENT_CLICK_TOKONOW,
                        action = EVENT_ACTION_CLICK_SHARE_BUTTON,
                        category = EVENT_CATEGORY_TOP_NAV
                )
        )
    }

    fun onClickAllCategory() {
        hitCommonHomeTracker(
                getDataLayer(
                        event = EVENT_CLICK_TOKONOW,
                        action = EVENT_ACTION_CLICK_ALL_CATEGORY,
                        category = EVENT_CATEGORY_HOME_PAGE
                )
        )
    }

    fun onClickBannerPromo(position: Int, userId: String, channelModel: ChannelModel, channelGrid: ChannelGrid) {
        val dataLayer = getEcommerceDataLayer(
                event = EVENT_SELECT_CONTENT,
                action = EVENT_ACTION_CLICK_SLIDER_BANNER,
                category = EVENT_CATEGORY_HOME_PAGE,
                affinityLabel = channelModel.trackingAttributionModel.persona,
                userId = userId,
                promotions = arrayListOf(
                        ecommerceDataLayerBannerClicked(
                            channelModel = channelModel,
                            channelGrid = channelGrid,
                            position = position
                        )
                )
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun onImpressBannerPromo(userId: String, channelModel: ChannelModel) {
        val promotions = arrayListOf<Bundle>()
        channelModel.channelGrids.forEachIndexed { position, channelGrid ->
            promotions.add(
                    ecommerceDataLayerBannerImpressed(
                        channelModel = channelModel,
                        channelGrid = channelGrid,
                        position = position
                    )
            )
        }

        val dataLayer = getEcommerceDataLayer(
                event = EVENT_VIEW_ITEM,
                action = EVENT_ACTION_IMPRESSION_SLIDER_BANNER,
                category = EVENT_CATEGORY_HOME_PAGE,
                affinityLabel = channelModel.trackingAttributionModel.persona,
                userId = userId,
                promotions = promotions

        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun onClickCategory(position: Int, userId: String, categoryId: String) {
        val dataLayer = getEcommerceDataLayer(
                event = EVENT_SELECT_CONTENT,
                action = EVENT_ACTION_CLICK_CATEGORY_ON_CATEGORY,
                category = EVENT_CATEGORY_HOME_PAGE,
                affinityLabel = "null",
                userId = userId,
                promotions = arrayListOf(
                        ecommerceDataLayerCategoryClicked(
                            categoryId = categoryId,
                            position = position
                        )
                )
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    private fun ecommerceDataLayerBannerClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int): Bundle {
        return Bundle().apply {
            putString(KEY_CREATIVE_NAME, channelModel.trackingAttributionModel.galaxyAttribution)
            putString(KEY_CREATIVE_SLOT, (position + 1).toString())
            putString(KEY_DIMENSION_104, channelModel.trackingAttributionModel.campaignCode)
            putString(KEY_DIMENSION_38, channelModel.trackingAttributionModel.galaxyAttribution)
            putString(KEY_DIMENSION_79, channelModel.trackingAttributionModel.brandId)
            putString(KEY_DIMENSION_82, channelModel.trackingAttributionModel.categoryId)
            putString(KEY_ITEM_ID, "0_" + channelGrid.id+ "_" + channelModel.trackingAttributionModel.persoType + "_" + channelModel.trackingAttributionModel.categoryId)
            putString(KEY_ITEM_NAME, NAME_PROMOTION)
        }
    }

    private fun ecommerceDataLayerBannerImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int): Bundle {
        return Bundle().apply {
            putString(KEY_CREATIVE_NAME, channelModel.trackingAttributionModel.galaxyAttribution)
            putString(KEY_CREATIVE_SLOT, (position + 1).toString())
            putString(KEY_ITEM_ID, "0_" + channelGrid.id + "_" + channelModel.trackingAttributionModel.persoType + "_" + channelModel.trackingAttributionModel.categoryId)
            putString(KEY_ITEM_NAME, NAME_PROMOTION)
        }
    }

    private fun ecommerceDataLayerCategoryClicked(position: Int, categoryId: String): Bundle {
        val nullString = "null"
        return Bundle().apply {
            putString(KEY_CREATIVE_NAME, nullString)
            putString(KEY_CREATIVE_SLOT, (position + 1).toString())
            putString(KEY_DIMENSION_49, nullString)
            putString(KEY_DIMENSION_38, nullString)
            putString(KEY_DIMENSION_79, nullString)
            putString(KEY_DIMENSION_82, nullString)
            putString(KEY_ITEM_ID, "0_" + categoryId + "_" + nullString + "_" + nullString)
            putString(KEY_ITEM_NAME, NAME_PROMOTION)
        }
    }

    private fun getDataLayer(event: String, action: String, category: String, label: String = ""): MutableMap<String, Any> {
        return DataLayer.mapOf(
                TrackAppUtils.EVENT, event,
                TrackAppUtils.EVENT_ACTION, action,
                TrackAppUtils.EVENT_CATEGORY, category,
                TrackAppUtils.EVENT_LABEL, label
        )
    }

    private fun getEcommerceDataLayer(event: String, action: String, category: String, label: String = "", affinityLabel: String = "", userId: String, promotions: ArrayList<Bundle>): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, category)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_AFFINITY_LABEL, affinityLabel)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
            putString(KEY_USER_ID, userId)
        }
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun hitCommonHomeTracker(dataLayer: MutableMap<String, Any>) {
        getTracker().sendGeneralEvent(dataLayer.getHomeGeneralTracker())
    }

    private fun MutableMap<String, Any>.getHomeGeneralTracker(): MutableMap<String, Any>? {
        this[TrackAppUtils.EVENT] = EVENT_CLICK_TOKONOW
        this[KEY_CURRENT_SITE] = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
        this[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        return this
    }
}
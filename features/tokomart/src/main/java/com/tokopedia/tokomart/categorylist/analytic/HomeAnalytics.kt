package com.tokopedia.tokomart.categorylist.analytic

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.tokomart.categorylist.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_ALL_CATEGORY
import com.tokopedia.tokomart.categorylist.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_CART_BUTTON
import com.tokopedia.tokomart.categorylist.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_CATEGORY_ON_CATEGORY
import com.tokopedia.tokomart.categorylist.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_CHOOSE_ADDRESS
import com.tokopedia.tokomart.categorylist.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SHARE_BUTTON
import com.tokopedia.tokomart.categorylist.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SLIDER_BANNER
import com.tokopedia.tokomart.categorylist.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_SLIDER_BANNER
import com.tokopedia.tokomart.categorylist.analytic.HomeAnalytics.CATEGORY.EVENT_CATEGORY_CHOOSE_ADDRESS
import com.tokopedia.tokomart.categorylist.analytic.HomeAnalytics.CATEGORY.EVENT_CATEGORY_HOME_PAGE
import com.tokopedia.tokomart.categorylist.analytic.HomeAnalytics.CATEGORY.EVENT_CATEGORY_TOP_NAV
import com.tokopedia.tokomart.categorylist.analytic.HomeAnalytics.PROMOTION.NAME_PROMOTION
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.EVENT.EVENT_CLICK_ADDRESS
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.EVENT.EVENT_PROMO_CLICK
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.EVENT.EVENT_PROMO_VIEW
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_AFFINITY_LABEL
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_CREATIVE_NAME
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_DIMENSION_104
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_DIMENSION_38
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_DIMENSION_49
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_DIMENSION_79
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_DIMENSION_82
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_ECOMMERCE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_ID
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_NAME
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_POSITION
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_PROMOTIONS
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_PROMO_CLICK
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokomart.common.analytics.TokonowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics

class HomeAnalytics {

    object CATEGORY{
        const val EVENT_CATEGORY_TOP_NAV = "tokonow - top nav"
        const val EVENT_CATEGORY_CHOOSE_ADDRESS = "widget choose address"
        const val EVENT_CATEGORY_HOME_PAGE = "tokonow - homepage"
    }

    object ACTION{
        const val EVENT_ACTION_CLICK_CART_BUTTON = "click cart button on homepage"
        const val EVENT_ACTION_CLICK_SHARE_BUTTON = "click share button on homepage"
        const val EVENT_ACTION_CLICK_CHOOSE_ADDRESS = "click widget choose address tokonow"
        const val EVENT_ACTION_CLICK_SLIDER_BANNER = "click slider banner"
        const val EVENT_ACTION_CLICK_ALL_CATEGORY = "click view all category widget"
        const val EVENT_ACTION_CLICK_CATEGORY_ON_CATEGORY = "click category on category widget"
        const val EVENT_ACTION_IMPRESSION_SLIDER_BANNER = "impression slider banner"
    }

    object PROMOTION {
        const val NAME_PROMOTION = "tokonow - p1 - promo"
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

    fun onClickChooseAddress() {
        hitCommonHomeTracker(
                getDataLayer(
                        event = EVENT_CLICK_ADDRESS,
                        action = EVENT_ACTION_CLICK_CHOOSE_ADDRESS,
                        category = EVENT_CATEGORY_CHOOSE_ADDRESS
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

    fun onClickBannerPromo(position: Int, userId: String, channelGrid: ChannelGrid, channelModel: ChannelModel) {
        val dataLayer = getDataLayer(
                event = EVENT_PROMO_CLICK,
                action = EVENT_ACTION_CLICK_SLIDER_BANNER,
                category = EVENT_CATEGORY_HOME_PAGE
        ).apply {
            this[KEY_AFFINITY_LABEL] = channelModel.trackingAttributionModel.persona
            this[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
            this[KEY_CURRENT_SITE] = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
            this[KEY_USER_ID] = userId
            this[KEY_ECOMMERCE] = ecommerceDataLayerBannerClicked(
                    channelModel = channelModel,
                    channelGrid = channelGrid,
                    position = position
            )
        }
        getTracker().sendGeneralEvent(dataLayer)
    }

    fun onImpressBannerPromo(position: Int, userId: String, channelModel: ChannelModel) {
        val dataLayer = getDataLayer(
                event = EVENT_PROMO_VIEW,
                action = EVENT_ACTION_IMPRESSION_SLIDER_BANNER,
                category = EVENT_CATEGORY_HOME_PAGE
        ).apply {
            this[KEY_AFFINITY_LABEL] = channelModel.trackingAttributionModel.persona
            this[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
            this[KEY_CURRENT_SITE] = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
            this[KEY_USER_ID] = userId
            this[KEY_ECOMMERCE] = ecommerceDataLayerBannerImpressed(
                    channelModel = channelModel,
                    channelGrid = channelModel.channelGrids[position],
                    position = position
            )
        }
        getTracker().sendGeneralEvent(dataLayer)
    }

    fun onClickCategory(position: Int, userId: String, categoryId: String, channelModel: ChannelModel? = null) {
        val dataLayer = getDataLayer(
                event = EVENT_PROMO_CLICK,
                action = EVENT_ACTION_CLICK_CATEGORY_ON_CATEGORY,
                category = EVENT_CATEGORY_HOME_PAGE
        ).apply {
            this[KEY_AFFINITY_LABEL] = ""
            this[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
            this[KEY_CURRENT_SITE] = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
            this[KEY_USER_ID] = userId
            this[KEY_ECOMMERCE] = ecommerceDataLayerCategoryClicked(
                    categoryId = categoryId,
                    position = position,
                    channelModel = channelModel!!,
            )
        }
        getTracker().sendGeneralEvent(dataLayer)
    }

    private fun ecommerceDataLayerBannerClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int): MutableMap<String, Any> {
        return DataLayer.mapOf(
                    KEY_PROMO_CLICK, DataLayer.mapOf(
                        KEY_PROMOTIONS, DataLayer.mapOf(
                            KEY_CREATIVE_NAME, channelModel.trackingAttributionModel.galaxyAttribution,
                            KEY_DIMENSION_104, channelModel.trackingAttributionModel.campaignId,
                            KEY_DIMENSION_38, channelModel.trackingAttributionModel.galaxyAttribution,
                            KEY_DIMENSION_79, channelModel.trackingAttributionModel.brandId,
                            KEY_DIMENSION_82, channelModel.trackingAttributionModel.categoryId,
                            KEY_ID, channelModel.id + "_" + channelGrid.id + "_" + channelModel.trackingAttributionModel.persoType + "_" + channelModel.trackingAttributionModel.categoryId,
                            KEY_NAME, NAME_PROMOTION,
                            KEY_POSITION, (position + 1).toString()
                        )
                    )
        )
    }

    private fun ecommerceDataLayerCategoryClicked(categoryId: String, position: Int, channelModel: ChannelModel): MutableMap<String, Any> {
        return DataLayer.mapOf(
                    KEY_PROMO_CLICK, DataLayer.mapOf(
                        KEY_PROMOTIONS, DataLayer.mapOf(
                            KEY_CREATIVE_NAME, channelModel.trackingAttributionModel.galaxyAttribution,
                            KEY_DIMENSION_49, channelModel.trackingAttributionModel.campaignId,
                            KEY_DIMENSION_38, channelModel.trackingAttributionModel.galaxyAttribution,
                            KEY_DIMENSION_79, channelModel.trackingAttributionModel.brandId,
                            KEY_DIMENSION_82, channelModel.trackingAttributionModel.categoryId,
                            KEY_ID, channelModel.id + "_" + categoryId + "_" + channelModel.trackingAttributionModel.persoType + "_" + channelModel.trackingAttributionModel.categoryId,
                            KEY_NAME, NAME_PROMOTION,
                            KEY_POSITION, (position + 1).toString()
                        )
                    )
                )
    }

    private fun ecommerceDataLayerBannerImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int): MutableMap<String, Any> {
        return DataLayer.mapOf(
                    KEY_PROMO_CLICK, DataLayer.mapOf(
                        KEY_PROMOTIONS, DataLayer.mapOf(
                            KEY_CREATIVE_NAME, channelModel.trackingAttributionModel.galaxyAttribution,
                            KEY_ID, channelModel.id + "_" + channelGrid.id + "_" + channelModel.trackingAttributionModel.persoType + "_" + channelModel.trackingAttributionModel.categoryId,
                            KEY_NAME, NAME_PROMOTION,
                            KEY_POSITION, (position + 1).toString()
                        )
                    )
        )
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun getDataLayer(event: String, action: String, category: String, label: String = ""): MutableMap<String, Any> {
        return DataLayer.mapOf(
                TrackAppUtils.EVENT, event,
                TrackAppUtils.EVENT_ACTION, action,
                TrackAppUtils.EVENT_CATEGORY, category,
                TrackAppUtils.EVENT_LABEL, label)
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
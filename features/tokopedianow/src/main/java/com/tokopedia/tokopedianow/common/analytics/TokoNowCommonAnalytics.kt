package com.tokopedia.tokopedianow.common.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_CHANGE_ADDRESS_ON_OOC
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_SHOP_ON_TOKOPEDIA
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_OPEN_SCREEN
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_IS_LOGGED_IN_STATUS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics

object TokoNowCommonAnalytics {

    fun onOpenScreen(isLoggedInStatus : Boolean, screenName: String, additionalMap: MutableMap<String, String>? = null) {
        val map = mutableMapOf(
            Pair(TrackAppUtils.EVENT, EVENT_OPEN_SCREEN),
            Pair(KEY_IS_LOGGED_IN_STATUS, isLoggedInStatus.toString()),
        )
        additionalMap?.apply {
            map.putAll(this)
        }
        hitCommonScreenTracker(
            screenName,
            map,
        )
    }

    fun onClickChangeAddressOnOoc(userId: String, category: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_CHANGE_ADDRESS_ON_OOC,
            category = category
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickShopOnTokopediaOoc(userId: String, category: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_SHOP_ON_TOKOPEDIA,
            category = category
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun hitCommonTracker(dataLayer: MutableMap<String, Any>) {
        getTracker().sendGeneralEvent(dataLayer.getCommonGeneralTracker())
    }

    fun getDataLayer(event: String, action: String, category: String, label: String = ""): MutableMap<String, Any> {
        return DataLayer.mapOf(
            TrackAppUtils.EVENT, event,
            TrackAppUtils.EVENT_ACTION, action,
            TrackAppUtils.EVENT_CATEGORY, category,
            TrackAppUtils.EVENT_LABEL, label,
        )
    }

    private fun hitCommonScreenTracker(screenName: String, dataLayer: MutableMap<String, String>) {
        getTracker().sendScreenAuthenticated(screenName, dataLayer.getCommonScreenTracker())
    }

    private fun MutableMap<String, Any>.getCommonGeneralTracker(): MutableMap<String, Any> {
        this[KEY_CURRENT_SITE] = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
        this[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        return this
    }

    private fun MutableMap<String, String>.getCommonScreenTracker(): MutableMap<String, String> {
        this[KEY_CURRENT_SITE] = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
        this[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        return this
    }
}
package com.tokopedia.tokopedianow.common.analytics

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_CHANGE_ADDRESS_ON_OOC
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_SHOP_ON_TOKOPEDIA
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_OPEN_SCREEN
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CREATIVE_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CREATIVE_SLOT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_56
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_IS_LOGGED_IN_STATUS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PROMOTIONS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.track.interfaces.Analytics

object TokoNowCommonAnalytics {

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

    fun getEcommerceDataLayerCategoryMenu(
        event: String,
        action: String,
        category: String,
        label: String = "",
        trackerId: String,
        promotions: ArrayList<Bundle>,
        warehouseId: String,
        userId: String
    ): Bundle {
        return Bundle().apply {
            putString(EVENT, event)
            putString(EVENT_ACTION, action)
            putString(EVENT_CATEGORY, category)
            putString(EVENT_LABEL, label)
            putString(KEY_TRACKER_ID, trackerId)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
            putString(KEY_USER_ID, userId)
            putString(KEY_WAREHOUSE_ID, warehouseId)
        }
    }

    fun getEcommerceDataLayerCategoryMenuPromotion(
        position: Int,
        categoryName: String,
        categoryId: String,
        warehouseId: String,
        itemName: String
    ): Bundle {
        return Bundle().apply {
            putString(KEY_CREATIVE_NAME, categoryName)
            putString(KEY_CREATIVE_SLOT, position.toString())
            putString(KEY_DIMENSION_56, warehouseId)
            putString(KEY_ITEM_ID, categoryId)
            putString(KEY_ITEM_NAME, itemName)
        }
    }

    fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    fun hitCommonTracker(dataLayer: MutableMap<String, Any>) {
        getTracker().sendGeneralEvent(dataLayer.getCommonGeneralTracker())
    }

    fun getDataLayer(event: String, action: String, category: String, label: String = "", trackerId: String = ""): MutableMap<String, Any> {
        return if (trackerId.isNotBlank()) {
            DataLayer.mapOf(
                EVENT, event,
                EVENT_ACTION, action,
                EVENT_CATEGORY, category,
                EVENT_LABEL, label,
            )
        } else {
            DataLayer.mapOf(
                EVENT, event,
                EVENT_ACTION, action,
                EVENT_CATEGORY, category,
                EVENT_LABEL, label,
                KEY_TRACKER_ID, trackerId
            )
        }
    }
}

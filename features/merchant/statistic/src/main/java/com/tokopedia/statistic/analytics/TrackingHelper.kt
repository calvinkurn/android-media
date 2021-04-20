package com.tokopedia.statistic.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By @ilhamsuaib on 22/07/20
 */

object TrackingHelper {

    fun createMap(event: String, category: String, action: String, label: String): MutableMap<String, Any> {
        return mutableMapOf(
                TrackingConstant.EVENT to event,
                TrackingConstant.EVENT_CATEGORY to category,
                TrackingConstant.EVENT_ACTION to action,
                TrackingConstant.EVENT_LABEL to label
        )
    }

    fun sendGeneralEvent(eventMap: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun sendEnhanceEcommerceEvent(eventMap: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventMap)
    }

    fun getShopStatus(userSession: UserSessionInterface): String {
        return when {
            userSession.isShopOfficialStore -> TrackingConstant.SHOP_OS
            userSession.isPowerMerchantIdle || userSession.isGoldMerchant -> TrackingConstant.SHOP_PM
            else -> TrackingConstant.SHOP_RM
        }
    }
}
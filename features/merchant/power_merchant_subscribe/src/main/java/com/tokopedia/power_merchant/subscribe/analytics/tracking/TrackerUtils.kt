package com.tokopedia.power_merchant.subscribe.analytics.tracking

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by @ilhamsuaib on 30/05/22.
 */

object TrackerUtils {

    fun createEvent(
        userSession: UserSessionInterface,
        event: String, category: String,
        action: String, label: String
    ): MutableMap<String, Any> {
        val map = TrackAppUtils.gtmData(
            event, category, action, label
        )
        map[TrackingConstant.KEY_BUSINESS_UNIT] = TrackingConstant.PHYSICAL_GOODS
        map[TrackingConstant.KEY_CURRENT_SITE] = TrackingConstant.TOKOPEDIA_SELLER
        map[TrackingConstant.KEY_SHOP_ID] = userSession.shopId
        map[TrackingConstant.KEY_USER_ID] = userSession.userId

        return map
    }

    fun sendEvent(map: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }
}
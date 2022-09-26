package com.tokopedia.sellerhome.settings.analytics

import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant.CLICK_PG
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant.TOKOPEDIA_MARKETPLACE
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant.VIEW_PG_IRIS
import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.analytic.TrackingHelper
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface

object SettingTokoMemberTracker {

    private const val EVENT_CATEGORY_OTHERS_TAB = "others tab"
    // ACTION
    private const val EVENT_ACTION_IMPRESSION_TOKOMEMBER = "impression tokomember"
    private const val EVENT_ACTION_CLICK_TOKOMEMBER = "click tokomember"

    private const val PHYSICAL_GOODS = "physical goods"
    private const val KEY_BUSINEESS_UNIT = "businessUnit"
    private const val KEY_CURRENT_SITE = "currentSite"

    fun trackTokoMemberImpression() {
        val event = TrackingHelper.createMap(
            event = VIEW_PG_IRIS,
            category = EVENT_CATEGORY_OTHERS_TAB,
            action = EVENT_ACTION_IMPRESSION_TOKOMEMBER,
            label = "",

        )
        event[KEY_BUSINEESS_UNIT] = PHYSICAL_GOODS
        event[KEY_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        TrackingHelper.sendGeneralEvent(event)
    }

    fun trackTokoMemberClick() {
        val event = TrackingHelper.createMap(
            event = CLICK_PG,
            category = EVENT_CATEGORY_OTHERS_TAB,
            action = EVENT_ACTION_CLICK_TOKOMEMBER,
            label = ""
        )
        event[KEY_BUSINEESS_UNIT] = PHYSICAL_GOODS
        event[KEY_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        TrackingHelper.sendGeneralEvent(event)
    }
}

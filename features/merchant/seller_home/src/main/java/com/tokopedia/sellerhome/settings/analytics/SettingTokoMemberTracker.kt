package com.tokopedia.sellerhome.settings.analytics

import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.analytic.TrackingHelper
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface

object SettingTokoMemberTracker {

    private const val EVENT_CATEGORY_OTHERS_TAB = "others tab"

    // EVENT NAME
    private const val EVENT = "viewPGIris"

    // ACTION
    private const val EVENT_ACTION_IMPRESSION_TOKOMEMBER = "impression tokomember"
    private const val EVENT_ACTION_CLICK_TOKOMEMBER = "click tokomember"

    private const val PHYSICAL_GOODS = "physical goods"
    private const val KEY_BUSINEESS_UNIT = "businessUnit"

    fun trackTokoMemberImpression() {
        val event = TrackingHelper.createMap(
            event = EVENT,
            category = EVENT_CATEGORY_OTHERS_TAB,
            action = EVENT_ACTION_IMPRESSION_TOKOMEMBER,
            label = ""
        )
        event[KEY_BUSINEESS_UNIT] = PHYSICAL_GOODS
        TrackingHelper.sendGeneralEvent(event)
    }

    fun trackTokoMemberClick() {
        val event = TrackingHelper.createMap(
            event = EVENT,
            category = EVENT_CATEGORY_OTHERS_TAB,
            action = EVENT_ACTION_CLICK_TOKOMEMBER,
            label = ""
        )
        event[KEY_BUSINEESS_UNIT] = PHYSICAL_GOODS
        TrackingHelper.sendGeneralEvent(event)
    }
}

package com.tokopedia.home.analytics.v2

import android.content.Context
import com.tokopedia.analyticconstant.DataLayer

/**
 * @author by yoasfs on 26/06/20
 */
object OvoWidgetTracking: BaseTracking() {

    private const val EVENT_CLICK_HOME_PAGE = "clickHomepage"
    private const val CATEGORY_HOME_PAGE = "homepage"

    private const val ACTION_CLICK_ON_OVO = "click on ovo"
    private const val ACTION_CLICK_ON_TOPUP_OVO = "click on top up ovo"

    fun eventTopupOvo(userId: String?) {
        getTracker().sendGeneralEvent( DataLayer.mapOf(
                Event.KEY, EVENT_CLICK_HOME_PAGE,
                Category.KEY, CATEGORY_HOME_PAGE,
                Action.KEY, ACTION_CLICK_ON_TOPUP_OVO,
                Label.KEY, "",
                Screen.KEY, Screen.DEFAULT,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                UserId.KEY, userId)
        )
    }

    fun eventOvo(context: Context?) {
        getTracker().sendGeneralEvent(
                EVENT_CLICK_HOME_PAGE,
                CATEGORY_HOME_PAGE,
                ACTION_CLICK_ON_OVO,
                ""
        )
    }


}
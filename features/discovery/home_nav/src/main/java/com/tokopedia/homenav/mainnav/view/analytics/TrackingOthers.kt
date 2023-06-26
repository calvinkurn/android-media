package com.tokopedia.homenav.mainnav.view.analytics

import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.CLICK_NAVIGATION_DRAWER
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.FORMAT_PAGE_SOURCE
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.GLOBAL_MENU
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.KEY_PAGE_SOURCE
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object TrackingOthers: BaseTrackerConst() {

    /**
     * Tracker ID: 18480
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickBusinessUnitItem(
        title: String,
        pageSource: String
    ){
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_NAVIGATION_DRAWER)
                .appendEventCategory(GLOBAL_MENU)
                .appendEventAction(Action.CLICK.format("business unit list"))
                .appendEventLabel(title)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18480")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
                .build()
        )
    }

    /**
     * Tracker ID: 18480
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickOnUserMenu(
        menuTrackerName: String,
        pageSource: String
    ) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_NAVIGATION_DRAWER)
                .appendEventCategory(GLOBAL_MENU)
                .appendEventAction("click on user menu")
                .appendEventLabel(menuTrackerName)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18485")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
                .build()
        )
    }

    /**
     * Tracker ID: 18477
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickBackToHome(
        pageSource: String
    ){
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_NAVIGATION_DRAWER)
                .appendEventCategory(GLOBAL_MENU)
                .appendEventAction("click back to home")
                .appendEventLabel(Label.NONE)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18477")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
                .build()
        )
    }

    /**
     * Tracker ID: 18476
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickCloseButton(
        pageSource: String
    ){
        TrackingOthers.getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_NAVIGATION_DRAWER)
                .appendEventCategory(GLOBAL_MENU)
                .appendEventAction("click close button")
                .appendEventLabel(Label.NONE)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18476")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
                .build()
        )
    }
}

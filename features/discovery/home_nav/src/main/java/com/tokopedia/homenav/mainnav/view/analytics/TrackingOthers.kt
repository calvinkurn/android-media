package com.tokopedia.homenav.mainnav.view.analytics

import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.GLOBAL_MENU
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.KEY_PAGE_SOURCE
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.asTrackingPageSource
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.CLICK_HOMEPAGE

object TrackingOthers: BaseTrackerConst() {

    /**
     * Tracker ID: 18480
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickBusinessUnitItem(
        title: String,
        pageSource: NavSource,
        pageSourcePath: String = ""
    ){
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_HOMEPAGE)
                .appendEventCategory(GLOBAL_MENU)
                .appendEventAction("click on business unit list")
                .appendEventLabel(title)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18480")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
                .build()
        )
    }

    /**
     * Tracker ID: 18480
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickOnUserMenu(
        menuTrackerName: String,
        pageSource: NavSource,
        pageSourcePath: String = ""
    ) {
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_HOMEPAGE)
                .appendEventCategory(GLOBAL_MENU)
                .appendEventAction("click on user menu")
                .appendEventLabel(menuTrackerName)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18485")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
                .build()
        )
    }

    /**
     * Tracker ID: 18477
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickBackToHome(
        pageSource: NavSource,
        pageSourcePath: String = ""
    ){
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_HOMEPAGE)
                .appendEventCategory(GLOBAL_MENU)
                .appendEventAction("click back to home")
                .appendEventLabel(Label.NONE)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18477")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
                .build()
        )
    }

    /**
     * Tracker ID: 18476
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickCloseButton(
        pageSource: NavSource,
        pageSourcePath: String = ""
    ){
        TrackingOthers.getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .appendEvent(CLICK_HOMEPAGE)
                .appendEventCategory(GLOBAL_MENU)
                .appendEventAction("click close button")
                .appendEventLabel(Label.NONE)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18476")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
                .build()
        )
    }
}

package com.tokopedia.homenav.mainnav.view.analytics

import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.GLOBAL_MENU
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.KEY_PAGE_SOURCE
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.asTrackingPageSource
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.CLICK_HOMEPAGE

object TrackingProfileSection: BaseTrackerConst() {
    private const val CLICK_ON_PROFILE_SECTION_NON_LOGIN = "click on profile section - non login"
    private const val CLICK_LOGIN = "login"
    private const val CLICK_LOGIN_REMINDER = "login reminder"
    private const val CLICK_REGISTER = "register account"
    private const val CLICK_ON_PROFILE_SECTION_LOGIN = "click on profile section - login"
    const val CLICK_USER_ACCOUNT = "user account"
    const val CLICK_SHOP_ACCOUNT = "shop account"
    const val CLICK_OPEN_SHOP = "open shop"
    const val CREATE_AFFILIATE = "create affiliate"
    const val AFFILIATE_ACCOUNT = "affiliate account"

    /**
     * Tracker ID: 18478
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickLoginButton(
        pageSource: NavSource,
        pageSourcePath: String = ""
    ){
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .constructBasicGeneralClick(
                    event = CLICK_HOMEPAGE,
                    eventCategory = GLOBAL_MENU,
                    eventAction = CLICK_ON_PROFILE_SECTION_NON_LOGIN,
                    eventLabel = CLICK_LOGIN
                )
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18478")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
                .build()
        )
    }

    /**
     * Tracker ID: 18478
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickLoginReminderButton(
        pageSource: NavSource,
        pageSourcePath: String = ""
    ){
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .constructBasicGeneralClick(
                    event = CLICK_HOMEPAGE,
                    eventCategory = GLOBAL_MENU,
                    eventAction = CLICK_ON_PROFILE_SECTION_NON_LOGIN,
                    eventLabel = CLICK_LOGIN_REMINDER
                )
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18478")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
                .build()
        )
    }

    /**
     * Tracker ID: 18478
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickRegisterButton(
        pageSource: NavSource,
        pageSourcePath: String = ""
    ){
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .constructBasicGeneralClick(
                    event = CLICK_HOMEPAGE,
                    eventCategory = GLOBAL_MENU,
                    eventAction = CLICK_ON_PROFILE_SECTION_NON_LOGIN,
                    eventLabel = CLICK_REGISTER
                )
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18478")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
                .build()
        )
    }

    /**
     * Tracker ID: 18479
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickProfileSection(
        eventLabel: String,
        pageSource: NavSource,
        pageSourcePath: String = ""
    ){
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .constructBasicGeneralClick(
                    event = CLICK_HOMEPAGE,
                    eventCategory = GLOBAL_MENU,
                    eventAction = CLICK_ON_PROFILE_SECTION_LOGIN,
                    eventLabel = eventLabel
                )
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18479")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
                .build()
        )
    }

    /**
     * Tracker ID: 33341/33342
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickTokopediaPlus(
        isSubscriber: Boolean,
        pageSource: NavSource,
        pageSourcePath: String = ""
    ) {
        val action = if(isSubscriber) "click on gotoplus - subscriber" else "click on gotoplus - non subscriber"
        val trackerId = if(isSubscriber) "33342" else "33341"
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .constructBasicGeneralClick(
                    event = CLICK_HOMEPAGE,
                    eventCategory = GLOBAL_MENU,
                    eventAction = action,
                    eventLabel = Label.NONE
                )
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, trackerId)
                .appendCustomKeyValue(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
                .build()
        )
    }
}

package com.tokopedia.homenav.mainnav.view.analytics

import android.os.Bundle
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.CLICK_NAVIGATION_DRAWER
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.FORMAT_PAGE_SOURCE
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.GLOBAL_MENU
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.KEY_PAGE_SOURCE
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

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
        pageSource: String
    ){
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .constructBasicGeneralClick(
                    event = CLICK_NAVIGATION_DRAWER,
                    eventCategory = GLOBAL_MENU,
                    eventAction = CLICK_ON_PROFILE_SECTION_NON_LOGIN,
                    eventLabel = CLICK_LOGIN
                )
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18478")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
                .build()
        )
    }

    /**
     * Tracker ID: 18478
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickLoginReminderButton(
        pageSource: String
    ){
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .constructBasicGeneralClick(
                    event = CLICK_NAVIGATION_DRAWER,
                    eventCategory = GLOBAL_MENU,
                    eventAction = CLICK_ON_PROFILE_SECTION_NON_LOGIN,
                    eventLabel = CLICK_LOGIN_REMINDER
                )
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18478")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
                .build()
        )
    }

    /**
     * Tracker ID: 18478
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickRegisterButton(
        pageSource: String
    ){
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .constructBasicGeneralClick(
                    event = CLICK_NAVIGATION_DRAWER,
                    eventCategory = GLOBAL_MENU,
                    eventAction = CLICK_ON_PROFILE_SECTION_NON_LOGIN,
                    eventLabel = CLICK_REGISTER
                )
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18478")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
                .build()
        )
    }

    /**
     * Tracker ID: 18479
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickProfileSection(
        eventLabel: String,
        pageSource: String
    ){
        getTracker().sendGeneralEvent(
            BaseTrackerBuilder()
                .constructBasicGeneralClick(
                    event = CLICK_NAVIGATION_DRAWER,
                    eventCategory = GLOBAL_MENU,
                    eventAction = CLICK_ON_PROFILE_SECTION_LOGIN,
                    eventLabel = eventLabel
                )
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, "18479")
                .appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
                .build()
        )
    }

    /**
     * Tracker ID: 33341/33342
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun onClickTokopediaPlus(
        isSubscriber: Boolean,
        pageSource: String
    ){
        val bundle = Bundle().apply {
            putString(Event.KEY, Event.CLICK_HOMEPAGE)
            putString(Category.KEY, GLOBAL_MENU)
            putString(
                Label.KEY,
                Value.EMPTY
            )

            if(isSubscriber){
                putString(
                    Action.KEY,
                    "click on gotoplus - subscriber"
                )
                putString(TrackerId.KEY, "33342")
            } else {
                putString(
                    Action.KEY,
                    "click on gotoplus - non subscriber"
                )
                putString(TrackerId.KEY, "33341")
            }

            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            putString(KEY_PAGE_SOURCE, pageSource)
        }

        getTracker().sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
    }
}

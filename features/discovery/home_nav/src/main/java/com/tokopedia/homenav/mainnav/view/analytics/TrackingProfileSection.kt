package com.tokopedia.homenav.mainnav.view.analytics

import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object TrackingProfileSection: BaseTrackerConst() {
    private const val CLICK_NAVIGATION_DRAWER = "clickNavigationDrawer"
    private const val GLOBAL_MENU = "global menu"
    private const val CLICK_ON_PROFILE_SECTION_NON_LOGIN = "click on profile section - non login"
    private const val CLICK_LOGIN = "login"
    private const val CLICK_LOGIN_REMINDER = "login reminder"
    private const val CLICK_REGISTER = "register account"
    private const val CLICK_ON_PROFILE_SECTION_LOGIN = "click on profile section - login"
    private const val CLICK_USER_ACCOUNT = "user account"
    private const val CLICK_SHOP_ACCOUNT = "shop account"
    private const val CLICK_OPEN_SHOP = "open shop"

    fun onClickLoginButton(userId: String){
        getTracker().sendGeneralEvent(
                BaseTrackerBuilder()
                        .constructBasicGeneralClick(
                                event = CLICK_NAVIGATION_DRAWER,
                                eventCategory = GLOBAL_MENU,
                                eventAction = CLICK_ON_PROFILE_SECTION_NON_LOGIN,
                                eventLabel = CLICK_LOGIN
                        )
                .appendUserId(userId)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .build())
    }
    fun onClickLoginReminderButton(userId: String){
        getTracker().sendGeneralEvent(
                BaseTrackerBuilder()
                        .constructBasicGeneralClick(
                                event = CLICK_NAVIGATION_DRAWER,
                                eventCategory = GLOBAL_MENU,
                                eventAction = CLICK_ON_PROFILE_SECTION_NON_LOGIN,
                                eventLabel = CLICK_LOGIN_REMINDER
                        )
                        .appendUserId(userId)
                        .appendCurrentSite(CurrentSite.DEFAULT)
                        .appendBusinessUnit(BusinessUnit.DEFAULT)
                        .build())
    }
    fun onClickRegisterButton(userId: String){
        getTracker().sendGeneralEvent(
                BaseTrackerBuilder()
                        .constructBasicGeneralClick(
                                event = CLICK_NAVIGATION_DRAWER,
                                eventCategory = GLOBAL_MENU,
                                eventAction = CLICK_ON_PROFILE_SECTION_NON_LOGIN,
                                eventLabel = CLICK_REGISTER
                        )
                        .appendUserId(userId)
                        .appendCurrentSite(CurrentSite.DEFAULT)
                        .appendBusinessUnit(BusinessUnit.DEFAULT)
                        .build())
    }

    fun onClickProfileSection(userId: String){
        getTracker().sendGeneralEvent(
                BaseTrackerBuilder()
                        .constructBasicGeneralClick(
                                event = CLICK_NAVIGATION_DRAWER,
                                eventCategory = GLOBAL_MENU,
                                eventAction = CLICK_ON_PROFILE_SECTION_LOGIN,
                                eventLabel = CLICK_USER_ACCOUNT
                        )
                        .appendUserId(userId)
                        .appendCurrentSite(CurrentSite.DEFAULT)
                        .appendBusinessUnit(BusinessUnit.DEFAULT)
                        .build())
    }
    fun onClickShopProfileSection(userId: String){
        getTracker().sendGeneralEvent(
                BaseTrackerBuilder()
                        .constructBasicGeneralClick(
                                event = CLICK_NAVIGATION_DRAWER,
                                eventCategory = GLOBAL_MENU,
                                eventAction = CLICK_ON_PROFILE_SECTION_LOGIN,
                                eventLabel = CLICK_SHOP_ACCOUNT
                        )
                        .appendUserId(userId)
                        .appendCurrentSite(CurrentSite.DEFAULT)
                        .appendBusinessUnit(BusinessUnit.DEFAULT)
                        .build())
    }
    fun onClickOpenShopSection(userId: String){
        getTracker().sendGeneralEvent(
                BaseTrackerBuilder()
                        .constructBasicGeneralClick(
                                event = CLICK_NAVIGATION_DRAWER,
                                eventCategory = GLOBAL_MENU,
                                eventAction = CLICK_ON_PROFILE_SECTION_LOGIN,
                                eventLabel = CLICK_OPEN_SHOP
                        )
                        .appendUserId(userId)
                        .appendCurrentSite(CurrentSite.DEFAULT)
                        .appendBusinessUnit(BusinessUnit.DEFAULT)
                        .build())
    }
}
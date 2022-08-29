package com.tokopedia.homenav.mainnav.view.analytics

import android.os.Bundle
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
    const val CLICK_SHOP_ACCOUNT = "shop account"
    const val CLICK_OPEN_SHOP = "open shop"
    const val CREATE_AFFILIATE = "create affiliate"
    const val AFFILIATE_ACCOUNT = "affiliate account"
    private const val KEY_TRACKER_ID = "trackerId"
    private const val CLICK_GOTOPLUS_NON_SUBSCRIBER = "click on gotoplus - non subscriber"
    private const val CLICK_GOTOPLUS_NON_SUBSCRIBER_ID = "33341"
    private const val CLICK_GOTOPLUS_SUBSCRIBER = "click on gotoplus - subscriber"
    private const val CLICK_GOTOPLUS_SUBSCRIBER_ID = "33342"

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

    fun onClickShopAndAffiliate(
        eventLabel: String
    ) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(
            Action.KEY,
            CLICK_ON_PROFILE_SECTION_LOGIN
        )
        bundle.putString(Category.KEY, GLOBAL_MENU)
        bundle.putString(
            Label.KEY,
            eventLabel
        )
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)

        getTracker().sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
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

    fun onClickTokopediaPlus(isSubscriber: Boolean){
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
                    CLICK_GOTOPLUS_SUBSCRIBER
                )
                putString(KEY_TRACKER_ID, CLICK_GOTOPLUS_SUBSCRIBER_ID)
            } else {
                putString(
                    Action.KEY,
                    CLICK_GOTOPLUS_NON_SUBSCRIBER
                )
                putString(KEY_TRACKER_ID, CLICK_GOTOPLUS_NON_SUBSCRIBER_ID)
            }

            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        }

        getTracker().sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
    }
}
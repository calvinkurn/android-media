package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp

/**
 * @author by yoasfs on 26/06/20
 */

object OvoWidgetTracking: BaseTracking() {
    private const val ACTION_CLICK_ON_TOKOPOINTS = "click on tokopoints"
    private const val ACTION_CLICK_ON_BBO = "click on bebas ongkir section"
    private const val ACTION_CLICK_ON_OVO = "click on ovo section"
    private const val ACTION_CLICK_ON_COUPON = "click new coupon"
    private const val ACTION_CLICK_ON_REWARDS = "click tier status"
    private const val ACTION_CLICK_NEW_WALLET_APP = "click on gopay section - gopaypoints"

    private const val ACTION_CLICK_ON_TOPUP_OVO = "click on top up ovo"
    private const val FIELD_BALANCE_POINTS = "balancePoints"

    fun eventTopUpOvo(userId: String?) {
        getTracker().sendGeneralEvent( DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, ACTION_CLICK_ON_TOPUP_OVO,
                Label.KEY, Label.NONE,
                Screen.KEY, Screen.DEFAULT,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                UserId.KEY, userId)
        )
    }

    fun sendClickOnCouponBalanceWidgetTracker(isOvoAvailable: Boolean, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE_TOKOPOINTS,
                Action.KEY, ACTION_CLICK_ON_COUPON,
                Label.KEY, Label.NONE,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                UserId.KEY, userId
        ))
    }

    fun sendClickOnRewardsBalanceWidgetTracker(isOvoAvailable: Boolean, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE_TOKOPOINTS,
                Action.KEY, ACTION_CLICK_ON_REWARDS,
                Label.KEY, Label.NONE,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                UserId.KEY, userId
        ))
    }

    fun sendClickOnOVOBalanceWidgetTracker(isOvoAvailable: Boolean, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE_TOKOPOINTS,
                Action.KEY, ACTION_CLICK_ON_OVO,
                Label.KEY, Label.NONE,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                UserId.KEY, userId
        ))
    }

    fun sendClickOnTokopointsBalanceWidgetTracker(isOvoAvailable: Boolean, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE_TOKOPOINTS,
                Action.KEY, ACTION_CLICK_ON_TOKOPOINTS,
                Label.KEY, Label.NONE,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                UserId.KEY, userId
        ))
    }

    fun sendClickOnBBOBalanceWidgetTracker(isOvoAvailable: Boolean, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE_TOKOPOINTS,
                Action.KEY, ACTION_CLICK_ON_BBO,
                Label.KEY, Label.NONE,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                UserId.KEY, userId
        ))
    }

    fun sendClickOnNewWalletAppBalanceWidgetTracker(subtitle: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE_TOKOPOINTS,
            Action.KEY, ACTION_CLICK_NEW_WALLET_APP,
            Label.KEY, Label.NONE,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            UserId.KEY, userId,
            FIELD_BALANCE_POINTS, subtitle
        ))
    }
}
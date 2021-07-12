package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * @author by yoasfs on 26/06/20
 */

object OvoWidgetTracking: BaseTracking() {
    private const val EVENT_USER_INTERACTION_HOMEPAGE = "userInteractionHomePage"

    private const val CATEGORY_HOMEPAGE_TOKOCASH_WIDGET = "homepage tokocash widget"

    private const val ACTION_CLICK_ON_TOKOPOINTS = "click on tokopoints"
    private const val ACTION_CLICK_ON_BBO = "click on bebas ongkir section"
    private const val ACTION_CLICK_ON_OVO = "click on ovo section"
    private const val ACTION_CLICK_ON_COUPON = "click new coupon"
    private const val ACTION_CLICK_ON_REWARDS = "click tier status"

    private const val ACTION_CLICK_ON_TOPUP_OVO = "click on top up ovo"
    private const val ACTION_CLICK_ACTIVATE = "click activate"
    private const val ACTION_CLICK_SALDO = "click saldo"

    private const val LABEL_TOKOPOINTS = "tokopoints"
    private const val LABEL_OVO_STATUS_AVAILABLE = "ovo available"
    private const val LABEL_OVO_STATUS_UNAVAILABLE = "ovo unavailable"

    // {clicked component} while {ovo visibility}
    private const val LABEL_BALANCE_WIDGET = "%s while %s"

    private const val NON_LOGIN = "non login"
    private const val CLICK_ON_QR_CODE = "click on qr code"

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

    fun eventTokopointNonLogin() {
        val tracker = TrackApp.getInstance().gtm
        tracker.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.HOMEPAGE,
                String.format(Action.CLICK_ON + " - %s", LABEL_TOKOPOINTS, NON_LOGIN),
                Label.NONE
        )
    }

    fun eventQrCode() {
        val tracker = TrackApp.getInstance().gtm
        tracker.sendGeneralEvent(
                Event.CLICK_HOMEPAGE,
                Category.HOMEPAGE,
                CLICK_ON_QR_CODE,
                Label.NONE
        )
    }

    fun eventTokoCashActivateClick() {
        val tracker = TrackApp.getInstance().gtm
        tracker.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_USER_INTERACTION_HOMEPAGE,
                CATEGORY_HOMEPAGE_TOKOCASH_WIDGET,
                ACTION_CLICK_ACTIVATE,
                Label.NONE
        ))
    }

    fun eventTokoCashCheckSaldoClick() {
        val tracker = TrackApp.getInstance().gtm
        tracker.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_USER_INTERACTION_HOMEPAGE,
                CATEGORY_HOMEPAGE_TOKOCASH_WIDGET,
                ACTION_CLICK_SALDO,
                Label.NONE
        ))
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

    private fun buildBalanceWidgetLabel(clickedComponent: String, isOvoAvailable: Boolean): String {
        return String.format(
                LABEL_BALANCE_WIDGET,
                clickedComponent,
                if (isOvoAvailable) LABEL_OVO_STATUS_AVAILABLE else LABEL_OVO_STATUS_UNAVAILABLE
        )
    }
}
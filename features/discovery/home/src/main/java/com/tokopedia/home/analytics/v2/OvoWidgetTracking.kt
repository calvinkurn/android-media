package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * @author by yoasfs on 26/06/20
 */

object OvoWidgetTracking: BaseTracking() {
    private const val EVENT_TOKO_POINT = "eventTokopoint"
    private const val EVENT_USER_INTERACTION_HOMEPAGE = "userInteractionHomePage"

    private const val CATEGORY_TOKOPOINTS_USER_PAGE = "tokopoints - user profile page"
    private const val CATEGORY_HOMEPAGE_TOKOPOINTS = "homepage-tokopoints"
    private const val CATEGORY_HOMEPAGE_TOKOCASH_WIDGET = "homepage tokocash widget"

    private const val ACTION_CLICK_ON_BALANCE_WIDGET = "click on balance widget"
    private const val ACTION_CLICK_TOKO_POINTS = "click tokopoints"
    private const val EVENT_ACTION_CLICK_ON_TOKOPOINTS_NEW_COUPON = "click on tokopoints new coupon"
    private const val ACTION_CLICK_ON_OVO = "click on ovo"
    private const val ACTION_CLICK_ON_TOPUP_OVO = "click on top up ovo"
    private const val ACTION_CLICK_ACTIVATE = "click activate"
    private const val ACTION_CLICK_POINT = "click point & tier status"
    private const val ACTION_CLICK_SALDO = "click saldo"

    private const val CLICK_OVO = "ovo"
    private const val CLICK_TOKOPOINT = "tokopoint"
    private const val CLICK_COUPON = "kupon"
    private const val CLICK_FREE_ONGKIR = "bebas ongkir"

    private const val LABEL_TOKOPOINTS = "tokopoints"
    private const val LABEL_OVO_STATUS_AVAILABLE = "ovo available"
    private const val LABEL_OVO_STATUS_UNAVAILABLE = "ovo unavailable"

    // {clicked component} while {ovo visibility}
    private const val LABEL_BALANCE_WIDGET = "%s while %s"

    private const val BEBAS_ONGKIR_KUOTA = "bebas ongkir kuota"
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

    fun eventUserProfileTokopoints() {
        val tracker = TrackApp.getInstance().gtm
        tracker.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_TOKO_POINT,
                CATEGORY_TOKOPOINTS_USER_PAGE,
                ACTION_CLICK_TOKO_POINTS,
                LABEL_TOKOPOINTS
        ))
    }

    fun sendClickOnCouponBalanceWidgetTracker(isOvoAvailable: Boolean, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, ACTION_CLICK_ON_BALANCE_WIDGET,
                Label.KEY, buildBalanceWidgetLabel(CLICK_COUPON, isOvoAvailable),
                Screen.KEY, Screen.DEFAULT,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                UserId.KEY, userId
        ))
    }

    fun sendClickOnOVOBalanceWidgetTracker(isOvoAvailable: Boolean, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, ACTION_CLICK_ON_BALANCE_WIDGET,
                Label.KEY, buildBalanceWidgetLabel(CLICK_OVO, isOvoAvailable),
                Screen.KEY, Screen.DEFAULT,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                UserId.KEY, userId
        ))
    }

    fun sendClickOnTokopointsBalanceWidgetTracker(isOvoAvailable: Boolean, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, ACTION_CLICK_ON_BALANCE_WIDGET,
                Label.KEY, buildBalanceWidgetLabel(CLICK_TOKOPOINT, isOvoAvailable),
                Screen.KEY, Screen.DEFAULT,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                BusinessUnit.KEY, BusinessUnit.DEFAULT,
                UserId.KEY, userId
        ))
    }

    fun sendClickOnBBOBalanceWidgetTracker(isOvoAvailable: Boolean, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, ACTION_CLICK_ON_BALANCE_WIDGET,
                Label.KEY, buildBalanceWidgetLabel(CLICK_FREE_ONGKIR, isOvoAvailable),
                Screen.KEY, Screen.DEFAULT,
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
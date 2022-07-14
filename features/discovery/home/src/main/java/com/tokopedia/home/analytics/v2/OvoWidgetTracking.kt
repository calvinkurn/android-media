package com.tokopedia.home.analytics.v2

import android.os.Bundle
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
    private const val ACTION_CLICK_SUBSCRIPTION = "click on gotoplus section - subscription status"
    private const val ACTION_CLICK_REWARDS = "click tier status"
    private const val ACTION_CLICK_NOT_LINKED = "click on gopay section - sambungkan"
    private const val CATEGORY_BALANCE_WIDGET = "homepage-tokopoints"
    private const val SUBSCRIBER = "subscriber"
    private const val NON_SUBSCRIBER = "non subs"
    private const val TRACKER_ID = "trackerId"
    private const val TRACKER_ID_CLICK_SUBSCRIPTION = "33767"
    private const val DEFAULT_VALUE = ""

    fun sendClickOnRewardsBalanceWidgetTracker(userId: String) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Action.KEY, ACTION_CLICK_REWARDS)
        bundle.putString(Category.KEY, CATEGORY_BALANCE_WIDGET)
        bundle.putString(Label.KEY, DEFAULT_VALUE)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
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

    fun sendClickGopayNotLinkedWidgetTracker(subtitle: String, userId: String) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Action.KEY, ACTION_CLICK_NOT_LINKED)
        bundle.putString(Category.KEY, CATEGORY_BALANCE_WIDGET)
        bundle.putString(Label.KEY, DEFAULT_VALUE)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
    }


    fun sendClickOnGoToPlusSectionSubscriptionStatusEvent (isSubscriber: Boolean, userId: String) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Action.KEY, ACTION_CLICK_SUBSCRIPTION)
        bundle.putString(Category.KEY, CATEGORY_BALANCE_WIDGET)
        bundle.putString(Label.KEY, if (isSubscriber) SUBSCRIBER else NON_SUBSCRIBER)
        bundle.putString(TRACKER_ID, TRACKER_ID_CLICK_SUBSCRIPTION)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
    }
}
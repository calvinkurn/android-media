package com.tokopedia.home.analytics.v2

import android.os.Bundle
import com.tokopedia.track.TrackApp

/**
 * @author by yoasfs on 26/06/20
 */
object BalanceWidgetTracking: BaseTracking() {
    private const val ACTION_CLICK_SUBSCRIPTION = "click on gotoplus section - subscription status"
    private const val ACTION_CLICK_REWARDS = "click tier status"
    private const val ACTION_CLICK_NOT_LINKED = "click on gopay section - sambungkan"
    private const val ACTION_CLICK_GOPAY_POINTS = "click on gopay section - gopaypoints"
    private const val CATEGORY_BALANCE_WIDGET = "homepage-tokopoints"
    private const val SUBSCRIBER = "subscriber"
    private const val NON_SUBSCRIBER = "non subscriber"
    private const val TRACKER_ID = "trackerId"
    private const val TRACKER_ID_CLICK_SUBSCRIPTION = "33767"
    private const val DEFAULT_VALUE = ""
    private const val BALANCE_POINTS_KEY = "balancePoints"

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

    fun sendClickGopayLinkedWidgetTracker(balancePoints: String, userId: String) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Action.KEY, ACTION_CLICK_GOPAY_POINTS)
        bundle.putString(Category.KEY, CATEGORY_BALANCE_WIDGET)
        bundle.putString(Label.KEY, DEFAULT_VALUE)
        bundle.putString(BALANCE_POINTS_KEY, balancePoints)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
    }

    fun sendClickGopayNotLinkedWidgetTracker(userId: String) {
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
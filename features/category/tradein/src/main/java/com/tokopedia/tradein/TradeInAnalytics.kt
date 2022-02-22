package com.tokopedia.tradein

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.tradein.TradeInGTMConstants.ACTION_CLICK
import com.tokopedia.tradein.TradeInGTMConstants.ACTION_CLICK_TRADEIN
import com.tokopedia.tradein.TradeInGTMConstants.ACTION_POP_UP
import com.tokopedia.tradein.TradeInGTMConstants.ACTION_VIEW_TRADEIN_IRIS
import com.tokopedia.tradein.TradeInGTMConstants.BUSINESS_UNIT
import com.tokopedia.tradein.TradeInGTMConstants.CURRENT_SITE
import com.tokopedia.tradein.TradeInGTMConstants.EVENT
import com.tokopedia.tradein.TradeInGTMConstants.EVENT_ACTION
import com.tokopedia.tradein.TradeInGTMConstants.EVENT_CATEGORY
import com.tokopedia.tradein.TradeInGTMConstants.EVENT_LABEL
import com.tokopedia.tradein.TradeInGTMConstants.KEY_BUSINESS_UNIT
import com.tokopedia.tradein.TradeInGTMConstants.KEY_SCREEN_NAME
import com.tokopedia.tradein.TradeInGTMConstants.KEY_CURRENT_SITE
import com.tokopedia.tradein.TradeInGTMConstants.KEY_LOGGED_IN
import com.tokopedia.tradein.TradeInGTMConstants.KEY_USER_ID
import com.tokopedia.tradein.TradeInGTMConstants.OPEN_SCREEN
import com.tokopedia.tradein.TradeInGTMConstants.PRODUCT_ID
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_COVERAGE_PAGE
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_EDUCATIONAL_PAGE

import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TradeInAnalytics @Inject constructor(
        private val userSession: UserSessionInterface) {

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun createGeneralEvent(eventName: String = ACTION_CLICK_TRADEIN, eventCategory: String, eventAction: String,
                                   eventLabel: String = "", screenName: String): MutableMap<String, Any> {
        return mutableMapOf(
                EVENT to eventName,
                EVENT_CATEGORY to eventCategory,
                EVENT_ACTION to eventAction,
                EVENT_LABEL to eventLabel,
                KEY_SCREEN_NAME to screenName,
                KEY_CURRENT_SITE to CURRENT_SITE,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to BUSINESS_UNIT
        )
    }

    fun sendGeneralEvent(event: String?, category: String?, action: String?, label: String?) {
        getTracker().sendGeneralEvent(event,
                category,
                action,
                label)
    }

    //3
    fun openEducationalScreen() {
        val map = mutableMapOf<String, Any>(
                EVENT to OPEN_SCREEN,
                KEY_SCREEN_NAME to TRADE_IN_EDUCATIONAL_PAGE,
                KEY_LOGGED_IN to if(userSession.isLoggedIn) "true" else "false",
                KEY_CURRENT_SITE to CURRENT_SITE,
                KEY_USER_ID to userSession.userId,
                KEY_BUSINESS_UNIT to BUSINESS_UNIT
        )
        getTracker().sendGeneralEvent(map)
    }

    //4
    fun clickEducationalBackButton() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventCategory = TRADE_IN_EDUCATIONAL_PAGE,
                        eventAction = ACTION_CLICK,
                        eventLabel = "back button",
                        screenName = TRADE_IN_EDUCATIONAL_PAGE))
    }

    //5
    fun clickEducationalBlackMarket() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventCategory = TRADE_IN_EDUCATIONAL_PAGE,
                        eventAction = ACTION_CLICK,
                        eventLabel = "black market information",
                        screenName = TRADE_IN_EDUCATIONAL_PAGE))
    }

    //6
    fun clickEducationalTNC() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventCategory = TRADE_IN_EDUCATIONAL_PAGE,
                        eventAction = ACTION_CLICK,
                        eventLabel = "terms and condition",
                        screenName = TRADE_IN_EDUCATIONAL_PAGE))
    }

    //7
    fun viewEducationalBlackMarket() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventName = ACTION_VIEW_TRADEIN_IRIS,
                        eventCategory = TRADE_IN_EDUCATIONAL_PAGE,
                        eventAction = ACTION_POP_UP,
                        eventLabel = "black market information",
                        screenName = TRADE_IN_EDUCATIONAL_PAGE))
    }

    //8
    fun viewEducationalTNC() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventName = ACTION_VIEW_TRADEIN_IRIS,
                        eventCategory = TRADE_IN_EDUCATIONAL_PAGE,
                        eventAction = ACTION_POP_UP,
                        eventLabel = "terms and condition",
                        screenName = TRADE_IN_EDUCATIONAL_PAGE))
    }

    //9
    fun clickEducationalTradeIn() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventCategory = TRADE_IN_EDUCATIONAL_PAGE,
                        eventAction = ACTION_CLICK,
                        eventLabel = "start trade in",
                        screenName = TRADE_IN_EDUCATIONAL_PAGE))
    }

    //13
    fun viewCoverageAreaBottomSheet() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventName = ACTION_VIEW_TRADEIN_IRIS,
                        eventCategory = TRADE_IN_COVERAGE_PAGE,
                        eventAction = "area not covered",
                        eventLabel = ACTION_POP_UP,
                        screenName = TRADE_IN_COVERAGE_PAGE))
    }

    //14
    fun clickCoverageAreaSimilarItems() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventCategory = TRADE_IN_COVERAGE_PAGE,
                        eventAction = "area not covered",
                        eventLabel = "find similar item",
                        screenName = TRADE_IN_COVERAGE_PAGE))
    }

    //15
    fun clickCoverageAreaCloseSheet() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventCategory = TRADE_IN_COVERAGE_PAGE,
                        eventAction = "area not covered",
                        eventLabel = "close pop up",
                        screenName = TRADE_IN_COVERAGE_PAGE))
    }

}
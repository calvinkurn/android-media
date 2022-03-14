package com.tokopedia.tradein

import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.tradein.TradeInGTMConstants.ACTION_CLICK
import com.tokopedia.tradein.TradeInGTMConstants.ACTION_CLICK_TRADEIN
import com.tokopedia.tradein.TradeInGTMConstants.ACTION_POP_UP
import com.tokopedia.tradein.TradeInGTMConstants.ACTION_VIEW_TRADEIN_IRIS
import com.tokopedia.tradein.TradeInGTMConstants.BUSINESS_UNIT
import com.tokopedia.tradein.TradeInGTMConstants.CLICK_PG
import com.tokopedia.tradein.TradeInGTMConstants.CURRENT_SITE
import com.tokopedia.tradein.TradeInGTMConstants.EVENT
import com.tokopedia.tradein.TradeInGTMConstants.EVENT_ACTION
import com.tokopedia.tradein.TradeInGTMConstants.EVENT_CATEGORY
import com.tokopedia.tradein.TradeInGTMConstants.EVENT_LABEL
import com.tokopedia.tradein.TradeInGTMConstants.KEY_BUSINESS_UNIT
import com.tokopedia.tradein.TradeInGTMConstants.KEY_CURRENT_SITE
import com.tokopedia.tradein.TradeInGTMConstants.KEY_LOGGED_IN
import com.tokopedia.tradein.TradeInGTMConstants.KEY_PAGE_PATH
import com.tokopedia.tradein.TradeInGTMConstants.KEY_SCREEN_NAME
import com.tokopedia.tradein.TradeInGTMConstants.KEY_USER_ID
import com.tokopedia.tradein.TradeInGTMConstants.OPEN_SCREEN
import com.tokopedia.tradein.TradeInGTMConstants.START_TRADE_IN
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_CLICK_DROPDOWN
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_CLICK_EDUCATIONAL_PAGE
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_CLICK_EDUCATIONAL_PAGE_BUTTON
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_CLICK_PROMO_BANNER
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_COVERAGE_PAGE
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_EDUCATIONAL_PAGE
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_ERROR_PAGE
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_ERROR_PAGE_EVENT
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_ERROR_PAGE_IMPRESSION
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_IMEI_ATTEMPT
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_IMEI_IMPRESSION
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_IMEI_SUCCESS
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_START_PAGE
import com.tokopedia.tradein.TradeInGTMConstants.VIEW_PG
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TradeInAnalytics @Inject constructor(
        private val userSession: UserSessionInterface) {

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun createGeneralEvent(eventName: String = ACTION_CLICK_TRADEIN, eventCategory: String, eventAction: String,
                                   eventLabel: String = "", screenName: String = ""): MutableMap<String, Any> {
        return mutableMapOf(
                EVENT to eventName,
                EVENT_CATEGORY to eventCategory,
                EVENT_ACTION to eventAction,
                EVENT_LABEL to eventLabel,
                KEY_SCREEN_NAME to screenName,
                KEY_LOGGED_IN to if(userSession.isLoggedIn) "true" else "false",
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
        getTracker().sendGeneralEvent(createGeneralEvent(
            OPEN_SCREEN,
            TRADE_IN_EDUCATIONAL_PAGE,
            "",
            "",
            TRADE_IN_EDUCATIONAL_PAGE))
    }

    //4
    fun openTradeInStartPage(is3PL : Boolean, imei : String, isDiagnosed : Boolean) {
        getTracker().sendGeneralEvent(
            createGeneralEvent(
                eventName = OPEN_SCREEN,
                eventCategory = TRADE_IN_START_PAGE,
                eventAction = "impression",
                eventLabel = (if(isDiagnosed) "with test" else "without test")
                        + " - "
                        + (if(imei.isNotEmpty()) "with IMEI" else "without IMEI")
                        + " - "
                        + (if(is3PL) "ditukar di indomaret" else "ditukar di alamatmu"),
                screenName = "trade in - initial price range - "
                        + (if(isDiagnosed) "with test " else "without test")
                        + " - "
                        + (if(imei.isNotEmpty()) "with IMEI" else "without IMEI")
                        + " - "
                        + (if(is3PL) "ditukar di indomaret" else "ditukar di alamatmu")))
    }



    //5
    fun clickTradeInStartPage(is3PL : Boolean, phoneType : String, priceRange : String, imei : String, isDiagnosed : Boolean) {
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_PG,
            EVENT_CATEGORY to TRADE_IN_START_PAGE,
            EVENT_ACTION to "click mulai test HP",
            EVENT_LABEL to (if(isDiagnosed) "with test" else "without test")
                    + " - "
                    + (if(imei.isNotEmpty()) "with IMEI" else "without IMEI")
                    + " - "
                    + (if(is3PL) "ditukar di indomaret" else "ditukar di alamatmu")
                    + " - $phoneType - $priceRange",
            KEY_CURRENT_SITE to CURRENT_SITE,
            KEY_BUSINESS_UNIT to BUSINESS_UNIT
        )
        getTracker().sendGeneralEvent(map)
    }

    //6
    fun viewIMEIBottomSheet() {
        val map = mutableMapOf<String, Any>(
            EVENT to VIEW_PG,
            EVENT_CATEGORY to TRADE_IN_START_PAGE,
            EVENT_ACTION to TRADE_IN_IMEI_IMPRESSION,
            EVENT_LABEL to "-",
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE)
        getTracker().sendGeneralEvent(map)
    }

    //7
    fun attemptIMEIBottomSheet() {
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_PG,
            EVENT_CATEGORY to TRADE_IN_START_PAGE,
            EVENT_ACTION to TRADE_IN_IMEI_ATTEMPT,
            EVENT_LABEL to "-",
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE)
        getTracker().sendGeneralEvent(map)
    }

    //8
    fun successIMEIBottomSheet(imei : String) {
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_PG,
            EVENT_CATEGORY to TRADE_IN_START_PAGE,
            EVENT_ACTION to TRADE_IN_IMEI_SUCCESS,
            EVENT_LABEL to imei,
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE)
        getTracker().sendGeneralEvent(map)
    }

    //9
    fun clickEducationalButton(is3PL : Boolean, imei : String, isDiagnosed : Boolean) {
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_PG,
            EVENT_CATEGORY to TRADE_IN_CLICK_EDUCATIONAL_PAGE_BUTTON,
            EVENT_ACTION to TRADE_IN_START_PAGE,
            EVENT_LABEL to (if(isDiagnosed) "with test" else "without test")
                    + " - "
                    + (if(imei.isNotEmpty()) "with IMEI" else "without IMEI")
                    + " - "
                    + (if(is3PL) "ditukar di indomaret" else "ditukar di alamatmu"),
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE
        )
        getTracker().sendGeneralEvent(map)
    }

    //10
    fun expandDropDown(is3PL : Boolean, imei : String, isDiagnosed : Boolean) {
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_PG,
            EVENT_CATEGORY to TRADE_IN_CLICK_DROPDOWN,
            EVENT_ACTION to TRADE_IN_START_PAGE,
            EVENT_LABEL to (if(isDiagnosed) "with test" else "without test")
                    + " - "
                    + (if(imei.isNotEmpty()) "with IMEI" else "without IMEI")
                    + " - "
                    + (if(is3PL) "ditukar di indomaret" else "ditukar di alamatmu"),
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE
        )
        getTracker().sendGeneralEvent(map)
    }

    //11
    fun clickPromoBanner(is3PL : Boolean, imei : String, isDiagnosed : Boolean, promo : String) {
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_PG,
            EVENT_CATEGORY to TRADE_IN_START_PAGE,
            EVENT_ACTION to TRADE_IN_CLICK_PROMO_BANNER,
            EVENT_LABEL to (if(isDiagnosed) "with test" else "without test")
                    + " - "
                    + (if(imei.isNotEmpty()) "with IMEI" else "without IMEI")
                    + " - "
                    + (if(is3PL) "ditukar di indomaret" else "ditukar di alamatmu")
                    + " - promoCode: $promo",
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE,
            KEY_USER_ID to userSession.userId)
        getTracker().sendGeneralEvent(map)
    }

    //12
    fun clickAttemptChangeAddress(is3PL : Boolean) {
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_PG,
            EVENT_CATEGORY to TRADE_IN_START_PAGE,
            EVENT_ACTION to "click attempt change address",
            EVENT_LABEL to (if(is3PL) "ditukar di indomaret" else "ditukar di alamatmu"),
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE,
            KEY_USER_ID to userSession.userId)
        getTracker().sendGeneralEvent(map)
    }

    //13
    fun clickSubmitChangeAddress(is3PL : Boolean) {
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_PG,
            EVENT_CATEGORY to TRADE_IN_START_PAGE,
            EVENT_ACTION to "click submit change address",
            EVENT_LABEL to (if(is3PL) "ditukar di indomaret" else "ditukar di alamatmu"),
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE,
            KEY_USER_ID to userSession.userId)
        getTracker().sendGeneralEvent(map)
    }

    //14
    fun impressionExchangeMethod(is1PLAvailable : Boolean, price1PL : String, is3PLAvailable : Boolean, price3Pl : String) {
        val map = mutableMapOf<String, Any>(
            EVENT to VIEW_PG,
            EVENT_CATEGORY to TRADE_IN_START_PAGE,
            EVENT_ACTION to "impression exchange methods",
            EVENT_LABEL to "alamatmu: " + (if(is1PLAvailable) "available" else "unavailable")
                    + " - price_range_alamatmu: $price1PL"
                    + " - indomaret: " + (if(is3PLAvailable) "available" else "unavailable")
                    + " - price_range_indomaret: $price3Pl",
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE,
            KEY_USER_ID to userSession.userId)
        getTracker().sendGeneralEvent(map)
    }

    //15
    fun clickExchangeMethods(is1PLAvailable : Boolean, price1PL : String, is3PLAvailable : Boolean, price3Pl : String) {
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_PG,
            EVENT_CATEGORY to TRADE_IN_START_PAGE,
            EVENT_ACTION to "click exchange methods",
            EVENT_LABEL to "alamatmu: " + (if(is1PLAvailable) "available" else "unavailable")
                    + " - price_range_alamatmu: $price1PL"
                    + " - indomaret: " + (if(is3PLAvailable) "available" else "unavailable")
                    + " - price_range_indomaret: $price3Pl",
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE,
            KEY_USER_ID to userSession.userId)
        getTracker().sendGeneralEvent(map)
    }

    //16
    fun errorScreen(errorCode : String) {
        getTracker().sendGeneralEvent(
            createGeneralEvent(
                eventName = OPEN_SCREEN,
                eventCategory = TRADE_IN_ERROR_PAGE_EVENT,
                eventAction = TRADE_IN_ERROR_PAGE_IMPRESSION,
                eventLabel = errorCode,
                screenName = TRADE_IN_ERROR_PAGE))

    }

    //17
    fun clickEducationalPage() {
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_PG,
            EVENT_CATEGORY to TRADE_IN_EDUCATIONAL_PAGE,
            EVENT_ACTION to TRADE_IN_CLICK_EDUCATIONAL_PAGE,
            EVENT_LABEL to START_TRADE_IN,
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE
        )
        getTracker().sendGeneralEvent(map)
    }

    //old 5
    fun clickEducationalBlackMarket() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventCategory = TRADE_IN_EDUCATIONAL_PAGE,
                        eventAction = ACTION_CLICK,
                        eventLabel = "black market information",
                        screenName = TRADE_IN_EDUCATIONAL_PAGE))
    }

    //old 6
    fun clickEducationalTNC() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventCategory = TRADE_IN_EDUCATIONAL_PAGE,
                        eventAction = ACTION_CLICK,
                        eventLabel = "terms and condition",
                        screenName = TRADE_IN_EDUCATIONAL_PAGE))
    }

    //old 7
    fun viewEducationalBlackMarket() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventName = ACTION_VIEW_TRADEIN_IRIS,
                        eventCategory = TRADE_IN_EDUCATIONAL_PAGE,
                        eventAction = ACTION_POP_UP,
                        eventLabel = "black market information",
                        screenName = TRADE_IN_EDUCATIONAL_PAGE))
    }

    //old 8
    fun viewEducationalTNC() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventName = ACTION_VIEW_TRADEIN_IRIS,
                        eventCategory = TRADE_IN_EDUCATIONAL_PAGE,
                        eventAction = ACTION_POP_UP,
                        eventLabel = "terms and condition",
                        screenName = TRADE_IN_EDUCATIONAL_PAGE))
    }

    //old 13
    fun viewCoverageAreaBottomSheet() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventName = ACTION_VIEW_TRADEIN_IRIS,
                        eventCategory = TRADE_IN_COVERAGE_PAGE,
                        eventAction = "area not covered",
                        eventLabel = ACTION_POP_UP,
                        screenName = TRADE_IN_COVERAGE_PAGE))
    }

    //old 14
    fun clickCoverageAreaSimilarItems() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventCategory = TRADE_IN_COVERAGE_PAGE,
                        eventAction = "area not covered",
                        eventLabel = "find similar item",
                        screenName = TRADE_IN_COVERAGE_PAGE))
    }

    //old 15
    fun clickCoverageAreaCloseSheet() {
        getTracker().sendGeneralEvent(
                createGeneralEvent(eventCategory = TRADE_IN_COVERAGE_PAGE,
                        eventAction = "area not covered",
                        eventLabel = "close pop up",
                        screenName = TRADE_IN_COVERAGE_PAGE))
    }

}
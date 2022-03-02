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
import com.tokopedia.tradein.TradeInGTMConstants.EVENT_SELECT_CONTENT
import com.tokopedia.tradein.TradeInGTMConstants.EVENT_VIEW_ITEM
import com.tokopedia.tradein.TradeInGTMConstants.KEY_BUSINESS_UNIT
import com.tokopedia.tradein.TradeInGTMConstants.KEY_CREATIVE_NAME
import com.tokopedia.tradein.TradeInGTMConstants.KEY_CURRENT_SITE
import com.tokopedia.tradein.TradeInGTMConstants.KEY_ECOMMERCE_EVENT
import com.tokopedia.tradein.TradeInGTMConstants.KEY_ITEM_NAME
import com.tokopedia.tradein.TradeInGTMConstants.KEY_LOGGED_IN
import com.tokopedia.tradein.TradeInGTMConstants.KEY_PAGE_PATH
import com.tokopedia.tradein.TradeInGTMConstants.KEY_PROMOTIONS
import com.tokopedia.tradein.TradeInGTMConstants.KEY_PROMO_CLICK
import com.tokopedia.tradein.TradeInGTMConstants.KEY_PROMO_VIEW
import com.tokopedia.tradein.TradeInGTMConstants.KEY_SCREEN_NAME
import com.tokopedia.tradein.TradeInGTMConstants.KEY_USER_ID
import com.tokopedia.tradein.TradeInGTMConstants.OPEN_SCREEN
import com.tokopedia.tradein.TradeInGTMConstants.START_TRADE_IN
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_CLICK_DROPDOWN
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_CLICK_EDUCATIONAL_PAGE
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_CLICK_EDUCATIONAL_PAGE_BUTTON
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_COVERAGE_PAGE
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_EDUCATIONAL_PAGE
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_ERROR_PAGE
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_ERROR_PAGE_EVENT
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_ERROR_PAGE_IMPRESSION
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_IMEI_ATTEMPT
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_IMEI_IMPRESSION
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_IMEI_SUCCESS
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_INITIAL_WITHOUT_IMEI
import com.tokopedia.tradein.TradeInGTMConstants.TRADE_IN_START_PAGE
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
                KEY_PAGE_PATH to "",
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
                KEY_BUSINESS_UNIT to BUSINESS_UNIT,
                KEY_CURRENT_SITE to CURRENT_SITE,
                KEY_LOGGED_IN to if(userSession.isLoggedIn) "true" else "false",
                KEY_PAGE_PATH to "",
                KEY_SCREEN_NAME to TRADE_IN_EDUCATIONAL_PAGE,
                KEY_USER_ID to userSession.userId
        )
        getTracker().sendGeneralEvent(map)
    }

    //4
    fun openTradeInStartPageWithoutImei(is3PL : Boolean, phoneType : String, priceRange : String, ) {
        getTracker().sendGeneralEvent(
            createGeneralEvent(
                eventName = OPEN_SCREEN,
                eventCategory = TRADE_IN_START_PAGE,
                eventAction = "without IMEI - " + (if(is3PL) "ditukar di indomaret" else "ditukar di alamatmu") + " - impression",
                eventLabel = "$phoneType - $priceRange - - " + (if(is3PL) "ditukar di indomaret" else "ditukar di alamatmu"),
                screenName = TRADE_IN_INITIAL_WITHOUT_IMEI))
    }


    //5
    fun clickCTAWithoutImei(is3PL : Boolean, phoneType : String, priceRange : String) {
        getTracker().sendGeneralEvent(
            createGeneralEvent(
                eventName = CLICK_PG,
                eventCategory = TRADE_IN_START_PAGE,
                eventAction = "without IMEI - " + (if(is3PL) "ditukar di indomaret" else "ditukar di alamatmu") + " - impression",
                eventLabel = "$phoneType - $priceRange - - " + (if(is3PL) "ditukar di indomaret" else "ditukar di alamatmu")))
    }


    //6
    fun viewIMEIBottomSheet() {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        productMap[KEY_CREATIVE_NAME] = "trade in - input IMEI"
        productMap[KEY_ITEM_NAME] = "trade in - start page"
        list.add(productMap)
        val map = mutableMapOf<String, Any>(
            EVENT to EVENT_VIEW_ITEM,
            EVENT_CATEGORY to TRADE_IN_START_PAGE,
            EVENT_ACTION to TRADE_IN_IMEI_IMPRESSION,
            EVENT_LABEL to "-",
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE,
            KEY_USER_ID to userSession.userId,
            KEY_ECOMMERCE_EVENT to mapOf(
                KEY_PROMO_VIEW to mapOf(
                    KEY_PROMOTIONS to list
                ))
        )
        getTracker().sendGeneralEvent(map)
    }

    //7
    fun attemptIMEIBottomSheet() {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        productMap[KEY_CREATIVE_NAME] = "trade in - input IMEI"
        productMap[KEY_ITEM_NAME] = "trade in - start page"
        list.add(productMap)
        val map = mutableMapOf<String, Any>(
            EVENT to EVENT_SELECT_CONTENT,
            EVENT_CATEGORY to TRADE_IN_START_PAGE,
            EVENT_ACTION to TRADE_IN_IMEI_ATTEMPT,
            EVENT_LABEL to "-",
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE,
            KEY_USER_ID to userSession.userId,
            KEY_ECOMMERCE_EVENT to mapOf(
                KEY_PROMO_CLICK to mapOf(
                    KEY_PROMOTIONS to list
                ))
        )
        getTracker().sendGeneralEvent(map)
    }

    //8
    fun successIMEIBottomSheet() {
        val list = ArrayList<Map<String, Any>>()
        val productMap = HashMap<String, Any>()
        productMap[KEY_CREATIVE_NAME] = "trade in - input IMEI"
        productMap[KEY_ITEM_NAME] = "trade in - start page"
        list.add(productMap)
        val map = mutableMapOf<String, Any>(
            EVENT to EVENT_SELECT_CONTENT,
            EVENT_CATEGORY to TRADE_IN_START_PAGE,
            EVENT_ACTION to TRADE_IN_IMEI_SUCCESS,
            EVENT_LABEL to "-",
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE,
            KEY_USER_ID to userSession.userId,
            KEY_ECOMMERCE_EVENT to mapOf(
                KEY_PROMO_CLICK to mapOf(
                    KEY_PROMOTIONS to list
                ))
        )
        getTracker().sendGeneralEvent(map)
    }

    //21
    fun clickEducationalButton(is3PL : Boolean, phoneType : String, priceRange : String) {
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_PG,
            EVENT_CATEGORY to TRADE_IN_CLICK_EDUCATIONAL_PAGE_BUTTON,
            EVENT_ACTION to TRADE_IN_START_PAGE,
            EVENT_LABEL to "$phoneType - $priceRange - - " + (if(is3PL) "ditukar di indomaret" else "ditukar di alamatmu"),
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE
        )
        getTracker().sendGeneralEvent(map)
    }

    //22
    fun expandDropDown(is3PL : Boolean, phoneType : String, priceRange : String) {
        val map = mutableMapOf<String, Any>(
            EVENT to CLICK_PG,
            EVENT_CATEGORY to TRADE_IN_CLICK_DROPDOWN,
            EVENT_ACTION to TRADE_IN_START_PAGE,
            EVENT_LABEL to "$phoneType - $priceRange - - " + (if(is3PL) "ditukar di indomaret" else "ditukar di alamatmu"),
            KEY_BUSINESS_UNIT to BUSINESS_UNIT,
            KEY_CURRENT_SITE to CURRENT_SITE
        )
        getTracker().sendGeneralEvent(map)
    }

    //29
    fun errorScreen(errorCode : String) {
        getTracker().sendGeneralEvent(
            createGeneralEvent(
                eventName = OPEN_SCREEN,
                eventCategory = TRADE_IN_ERROR_PAGE_EVENT,
                eventAction = TRADE_IN_ERROR_PAGE_IMPRESSION,
                eventLabel = errorCode,
                screenName = TRADE_IN_ERROR_PAGE))

    }


    //30
    fun clickEducationalPage(is3PL : Boolean, phoneType : String, priceRange : String) {
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
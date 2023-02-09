package com.tokopedia.sellerpersona.analytics

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.track.builder.Tracker

/**
 * Created by @ilhamsuaib on 08/02/23.
 */

object SellerPersonaTracking {

    fun sendSettingsClickSellerPersonaEvent() {
        Tracker.Builder()
            .setEvent(TrackingConst.Event.CLICK_PG)
            .setEventAction(TrackingConst.SETTINGS_CLICK_SELLER_PERSONA)
            .setEventCategory(TrackingConst.Category.OTHER_TAB)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(TrackingConst.TRACKER_ID, "40032")
            .setBusinessUnit(TrackingConst.BUSINESS_UNIT)
            .setCurrentSite(TrackingConst.TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    fun sendImpressionSellerPersonaEvent() {
        Tracker.Builder()
            .setEvent(TrackingConst.Event.VIEW_PG_IRIS)
            .setEventAction(TrackingConst.IMPRESSION_SELLER_PERSONA)
            .setEventCategory(TrackingConst.Category.OTHER_TAB)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(TrackingConst.TRACKER_ID, "40033")
            .setBusinessUnit(TrackingConst.BUSINESS_UNIT)
            .setCurrentSite(TrackingConst.TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    fun sendClickSellerPersonaLaterEvent() {
        Tracker.Builder()
            .setEvent(TrackingConst.Event.CLICK_PG)
            .setEventAction(TrackingConst.CLICK_SELLER_PERSONA_LATER)
            .setEventCategory(TrackingConst.Category.OTHER_TAB)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(TrackingConst.TRACKER_ID, "40034")
            .setBusinessUnit(TrackingConst.BUSINESS_UNIT)
            .setCurrentSite(TrackingConst.TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    fun sendClickSellerPersonaStartQuizEvent() {
        Tracker.Builder()
            .setEvent(TrackingConst.Event.CLICK_PG)
            .setEventAction(TrackingConst.CLICK_SELLER_PERSONA_START_QUIZ)
            .setEventCategory(TrackingConst.Category.OTHER_TAB)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(TrackingConst.TRACKER_ID, "40035")
            .setBusinessUnit(TrackingConst.BUSINESS_UNIT)
            .setCurrentSite(TrackingConst.TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    fun sendImpressionSellerPersonaResultEvent() {
        Tracker.Builder()
            .setEvent(TrackingConst.Event.VIEW_PG_IRIS)
            .setEventAction(TrackingConst.IMPRESSION_SELLER_PERSONA_RESULT)
            .setEventCategory(TrackingConst.Category.OTHER_TAB)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(TrackingConst.TRACKER_ID, "40036")
            .setBusinessUnit(TrackingConst.BUSINESS_UNIT)
            .setCurrentSite(TrackingConst.TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    fun sendClickSellerPersonaResultToggleActiveEvent() {
        Tracker.Builder()
            .setEvent(TrackingConst.Event.CLICK_PG)
            .setEventAction(TrackingConst.CLICK_SELLER_PERSONA_TOGGLE_ACTIVE)
            .setEventCategory(TrackingConst.Category.OTHER_TAB)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(TrackingConst.TRACKER_ID, "40037")
            .setBusinessUnit(TrackingConst.BUSINESS_UNIT)
            .setCurrentSite(TrackingConst.TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    fun sendClickSellerPersonaResultSelectPersonaEvent() {
        Tracker.Builder()
            .setEvent(TrackingConst.Event.CLICK_PG)
            .setEventAction(TrackingConst.CLICK_SELLER_PERSONA_SELECT_PERSONA)
            .setEventCategory(TrackingConst.Category.OTHER_TAB)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(TrackingConst.TRACKER_ID, "40038")
            .setBusinessUnit(TrackingConst.BUSINESS_UNIT)
            .setCurrentSite(TrackingConst.TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    fun sendClickSellerPersonaResultRetakeQuizEvent() {
        Tracker.Builder()
            .setEvent(TrackingConst.Event.CLICK_PG)
            .setEventAction(TrackingConst.CLICK_SELLER_PERSONA_TAKE_QUIZ)
            .setEventCategory(TrackingConst.Category.OTHER_TAB)
            .setEventLabel(String.EMPTY)
            .setCustomProperty(TrackingConst.TRACKER_ID, "40039")
            .setBusinessUnit(TrackingConst.BUSINESS_UNIT)
            .setCurrentSite(TrackingConst.TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }

    fun sendClickSellerPersonaResultSavePersonaEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(TrackingConst.Event.CLICK_PG)
            .setEventAction(TrackingConst.CLICK_SELLER_PERSONA_SAVE_PERSONA)
            .setEventCategory(TrackingConst.Category.OTHER_TAB)
            .setEventLabel(eventLabel)
            .setCustomProperty(TrackingConst.TRACKER_ID, "40040")
            .setBusinessUnit(TrackingConst.BUSINESS_UNIT)
            .setCurrentSite(TrackingConst.TOKOPEDIA_MARKET_PLACE)
            .build()
            .send()
    }
}
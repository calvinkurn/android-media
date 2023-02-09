package com.tokopedia.sellerpersona.analytics

import com.tokopedia.track.builder.Tracker

/**
 * Created by @ilhamsuaib on 08/02/23.
 */

object SellerPersonaTracking {


    fun sendSettingsClickSellerPersonaEvent() {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("settings - click seller persona")
            .setEventCategory("others tab")
            .setEventLabel("")
            .setCustomProperty("trackerId", "40032")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendImpressionSellerPersonaEvent() {
        Tracker.Builder()
            .setEvent("viewPGIris")
            .setEventAction("impression seller persona")
            .setEventCategory("others tab")
            .setEventLabel("")
            .setCustomProperty("trackerId", "40033")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickSellerPersonaNantiSajaEvent() {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("click seller persona - nanti saja")
            .setEventCategory("others tab")
            .setEventLabel("")
            .setCustomProperty("trackerId", "40034")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickSellerPersonaMulaiKuisEvent() {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("click seller persona - mulai kuis")
            .setEventCategory("others tab")
            .setEventLabel("")
            .setCustomProperty("trackerId", "40035")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendImpressionSellerPersonaResultEvent() {
        Tracker.Builder()
            .setEvent("viewPGIris")
            .setEventAction("impression seller persona result")
            .setEventCategory("others tab")
            .setEventLabel("")
            .setCustomProperty("trackerId", "40036")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickSellerPersonaResultToggleActiveEvent() {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("click seller persona result - toggle active")
            .setEventCategory("others tab")
            .setEventLabel("")
            .setCustomProperty("trackerId", "40037")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickSellerPersonaResultSelectPersonaEvent() {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("click seller persona result - select persona")
            .setEventCategory("others tab")
            .setEventLabel("")
            .setCustomProperty("trackerId", "40038")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickSellerPersonaResultRetakeQuizEvent() {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("click seller persona result - retake quiz")
            .setEventCategory("others tab")
            .setEventLabel("")
            .setCustomProperty("trackerId", "40039")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    fun sendClickSellerPersonaResultSavePersonaEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("click seller persona result - save persona")
            .setEventCategory("others tab")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "40040")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }
}
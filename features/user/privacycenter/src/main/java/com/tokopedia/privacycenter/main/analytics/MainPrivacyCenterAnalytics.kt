package com.tokopedia.privacycenter.main.analytics

import com.tokopedia.track.builder.Tracker

//List tracker on MyNakama: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3571
object MainPrivacyCenterAnalytics {

    const val LABEL_SHARE_WISHLIST = "share wishlist"
    const val LABEL_SEARCH_HISTORY = "search history"
    const val LABEL_TOGGLE_SHAKE_SHAKE = "toggle shake shake"
    const val LABEL_TOGGLE_GEOLOCATION = "toggle geolocation"
    const val LABEL_TOGGLE_RECOMMENDATION_FRIEND = "toggle recommendation friend in feed"
    const val LABEL_LINK = "link"
    const val LABEL_UNLINK = "unlink"
    const val LABEL_ACCOUNT_LINKED = "account linking"
    const val LABEL_ACCOUNT_NOT_LINKED = "non account linking"
    private const val VALUE_BUSINESS_UNIT = "user platform"
    private const val VALUE_CURRENT_SITE = "tokopediamarketplace"

    fun sendClickOnButtonAccountLinkingEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button account linking")
            .setEventCategory("privacy center page")
            .setEventLabel("click - $eventLabel")
            .setCustomProperty("trackerId", "38785")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendViewAccountLinkingSectionEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("viewAccountIris")
            .setEventAction("view account linking section")
            .setEventCategory("privacy center page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "38786")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnTrackingSectionEvent(eventLabel: String, isEnable: Boolean) {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on tracking section")
            .setEventCategory("privacy center page")
            .setEventLabel("$eventLabel - ${if (isEnable) "enable" else "disable"}")
            .setCustomProperty("trackerId", "38791")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnConsentSectionEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on consent section")
            .setEventCategory("privacy center page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "38795")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonBacaKebijakanPrivasiEvent() {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button baca kebijakan privasi")
            .setEventCategory("privacy center page")
            .setEventLabel("")
            .setCustomProperty("trackerId", "38809")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonLihatSemuaEvent() {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button lihat semua")
            .setEventCategory("privacy center page")
            .setEventLabel("")
            .setCustomProperty("trackerId", "38811")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonTokopediaCareEvent() {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button tokopedia care")
            .setEventCategory("privacy center page")
            .setEventLabel("")
            .setCustomProperty("trackerId", "38813")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnRiwayatKebijakanPrivasiEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on riwayat kebijakan privasi")
            .setEventCategory("privacy center page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "38931")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendScrollPrivacyCenterEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("scroll privacy center")
            .setEventCategory("privacy center page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "38972")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonAktivitasEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button aktivitas")
            .setEventCategory("privacy center page")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "39377")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonDownloadDataPribadiEvent() {
        Tracker.Builder()
            .setEvent("clickAccount")
            .setEventAction("click on button download data pribadi")
            .setEventCategory("privacy center page")
            .setEventLabel("")
            .setCustomProperty("trackerId", "39381")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendViewPrivacyCenterEvent() {
        Tracker.Builder()
            .setEvent("viewAccountIris")
            .setEventAction("view privacy center")
            .setEventCategory("privacy center page")
            .setEventLabel("")
            .setCustomProperty("trackerId", "39462")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }
}

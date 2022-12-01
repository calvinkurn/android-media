package com.tokopedia.privacycenter.ui.main.analytics

import com.tokopedia.track.builder.Tracker

// List tracker on MyNakama: https://mynakama.tokopedia.com/datatracker/requestdetail/view/3571
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
    private const val EVENT_CLICK_ACCOUNT = "clickAccount"
    private const val KEY_PROJECT_ID = "trackerId"
    private const val VALUE_PROJECT_ID_38785 = "38785"
    private const val VALUE_PROJECT_ID_38786 = "38786"
    private const val VALUE_PROJECT_ID_38791 = "38791"
    private const val VALUE_PROJECT_ID_38795 = "38795"
    private const val VALUE_PROJECT_ID_38809 = "38809"
    private const val VALUE_PROJECT_ID_38811 = "38811"
    private const val VALUE_PROJECT_ID_38813 = "38813"
    private const val VALUE_PROJECT_ID_38931 = "38931"
    private const val VALUE_PROJECT_ID_38972 = "38972"
    private const val VALUE_PROJECT_ID_39377 = "39377"
    private const val VALUE_PROJECT_ID_39381 = "39381"
    private const val VALUE_PROJECT_ID_39462 = "39462"
    private const val CATEGORY_PRIVACY_CENTER_PAGE = "privacy center page"
    private const val ENABLE = "enable"
    private const val DISABLE = "disable"
    private const val VIEW_ACCOUNT_IRIS = "viewAccountIris"
    private const val CLICK_ON = "click on"
    private const val CLICK = "click"
    private const val VIEW_PRIVACY_CENTER = "view privacy center"
    private const val SCROLL_PRIVACY_CENTER = "scroll privacy center"
    private const val VIEW_ACCOUNT_LINKING_SECTION = "view account linking section"
    private const val TRACKING_SECTION = "tracking section"
    private const val CONSENT_SECTION = "consent section"
    private const val RIWAYAT_KEBIJAKAN_PRIVASI = "riwayat kebijakan privasi"
    private const val BUTTON_ACCOUNT_LINKING = "button account linking"
    private const val BUTTON_BACA_KEBIJAKAN_PRIVACY = "button baca kebijakan privasi"
    private const val BUTTON_LIHAT_SEMUA = "button lihat semua"
    private const val BUTTON_TOKOPEDIA_CARE = "button tokopedia care"
    private const val BUTTON_AKTIVITAS = "button aktivitas"
    private const val BUTTON_DOWNLOAD_DATA_PRIBADI = "button download data pribadi"

    fun sendClickOnButtonAccountLinkingEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction("$CLICK_ON $BUTTON_ACCOUNT_LINKING")
            .setEventCategory(CATEGORY_PRIVACY_CENTER_PAGE)
            .setEventLabel("$CLICK - $eventLabel")
            .setCustomProperty(KEY_PROJECT_ID, VALUE_PROJECT_ID_38785)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendViewAccountLinkingSectionEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(VIEW_ACCOUNT_IRIS)
            .setEventAction(VIEW_ACCOUNT_LINKING_SECTION)
            .setEventCategory(CATEGORY_PRIVACY_CENTER_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_PROJECT_ID, VALUE_PROJECT_ID_38786)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnTrackingSectionEvent(eventLabel: String, isEnable: Boolean) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction("$CLICK_ON $TRACKING_SECTION")
            .setEventCategory(CATEGORY_PRIVACY_CENTER_PAGE)
            .setEventLabel("$eventLabel - ${if (isEnable) ENABLE else DISABLE}")
            .setCustomProperty(KEY_PROJECT_ID, VALUE_PROJECT_ID_38791)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnConsentSectionEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction("$CLICK_ON $CONSENT_SECTION")
            .setEventCategory(CATEGORY_PRIVACY_CENTER_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_PROJECT_ID, VALUE_PROJECT_ID_38795)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonBacaKebijakanPrivasiEvent() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction("$CLICK_ON $BUTTON_BACA_KEBIJAKAN_PRIVACY")
            .setEventCategory(CATEGORY_PRIVACY_CENTER_PAGE)
            .setEventLabel("")
            .setCustomProperty(KEY_PROJECT_ID, VALUE_PROJECT_ID_38809)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonLihatSemuaEvent() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction("$CLICK_ON $BUTTON_LIHAT_SEMUA")
            .setEventCategory(CATEGORY_PRIVACY_CENTER_PAGE)
            .setEventLabel("")
            .setCustomProperty(KEY_PROJECT_ID, VALUE_PROJECT_ID_38811)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonTokopediaCareEvent() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction("$CLICK_ON $BUTTON_TOKOPEDIA_CARE")
            .setEventCategory(CATEGORY_PRIVACY_CENTER_PAGE)
            .setEventLabel("")
            .setCustomProperty(KEY_PROJECT_ID, VALUE_PROJECT_ID_38813)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnRiwayatKebijakanPrivasiEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction("$CLICK_ON $RIWAYAT_KEBIJAKAN_PRIVASI")
            .setEventCategory(CATEGORY_PRIVACY_CENTER_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_PROJECT_ID, VALUE_PROJECT_ID_38931)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendScrollPrivacyCenterEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(SCROLL_PRIVACY_CENTER)
            .setEventCategory(CATEGORY_PRIVACY_CENTER_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_PROJECT_ID, VALUE_PROJECT_ID_38972)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonAktivitasEvent(eventLabel: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction("$CLICK_ON $BUTTON_AKTIVITAS")
            .setEventCategory(CATEGORY_PRIVACY_CENTER_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_PROJECT_ID, VALUE_PROJECT_ID_39377)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonDownloadDataPribadiEvent() {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction("$CLICK_ON $BUTTON_DOWNLOAD_DATA_PRIBADI")
            .setEventCategory(CATEGORY_PRIVACY_CENTER_PAGE)
            .setEventLabel("")
            .setCustomProperty(KEY_PROJECT_ID, VALUE_PROJECT_ID_39381)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendViewPrivacyCenterEvent() {
        Tracker.Builder()
            .setEvent(VIEW_ACCOUNT_IRIS)
            .setEventAction(VIEW_PRIVACY_CENTER)
            .setEventCategory(CATEGORY_PRIVACY_CENTER_PAGE)
            .setEventLabel("")
            .setCustomProperty(KEY_PROJECT_ID, VALUE_PROJECT_ID_39462)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }
}

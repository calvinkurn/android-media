package com.tokopedia.loginregister.common.analytics

import com.tokopedia.track.builder.Tracker
import javax.inject.Inject

class RedefineInitialRegisterAnalytics @Inject constructor() {

    fun sendViewRegisterPageEvent(variant: String) {
        Tracker.Builder()
            .setEvent(VIEW_ACCOUNT_IRIS)
            .setEventAction(VIEW_REGISTER_PAGE)
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel("$VIEW - ${getVariant(variant)}")
            .setCustomProperty(TRACKER_ID, ID_36093)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnMasukEvent(variant: String) {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction(CLICK_ON_MASUK)
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel("$ACTION_CLICK - ${getVariant(variant)}")
            .setCustomProperty(TRACKER_ID, ID_36096)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonMetodeLainEvent(variant: String) {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction(CLICK_ON_METODE_LAIN)
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel("$ACTION_CLICK - ${getVariant(variant)}")
            .setCustomProperty(TRACKER_ID, ID_36097)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonDaftarPhoneNumberEvent(
        action: String,
        variant: String,
        errorMessage: String = ""
    ) {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction("$CLICK_ON_BUTTON_DAFTAR - $PHONE_NUMBER")
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel(getActionLabel(action, variant, errorMessage))
            .setCustomProperty(TRACKER_ID, ID_36098)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonDaftarEmailEvent(
        action: String,
        variant: String,
        errorMessage: String = ""
    ) {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction("$CLICK_ON_BUTTON_DAFTAR - $EMAIL")
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel(getActionLabel(action, variant, errorMessage))
            .setCustomProperty(TRACKER_ID, ID_36099)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonGoogleEvent(action: String, variant: String, errorMessage: String = "") {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction(CLICK_ON_BUTTON_GOOGLE)
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel(getActionLabel(action, variant, errorMessage))
            .setCustomProperty(TRACKER_ID, ID_36100)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    private fun getActionLabel(action: String, variant: String, errorMessage: String): String {
        return "$action - ${getVariant(variant)}${if (action == ACTION_FAILED) " - $errorMessage" else ""}"
    }

    private fun getVariant(variant: String): String {
        return when {
            variant.contains(VARIANT_MANDATORY) -> VARIANT_MANDATORY
            variant.contains(VARIANT_OPTIONAL) -> VARIANT_OPTIONAL
            else -> VARIANT_CONTROL
        }
    }

    companion object {

        private const val VARIANT_CONTROL = "control"
        private const val VARIANT_MANDATORY = "mandatory"
        private const val VARIANT_OPTIONAL = "optional"

        private const val REGISTER_PAGE = "register page"
        private const val VIEW = "view"
        private const val TRACKER_ID = "trackerId"

        const val ACTION_CLICK = "click"
        const val ACTION_SUCCESS = "success"
        const val ACTION_FAILED = "failed"
        private const val EMAIL = "email"
        private const val PHONE_NUMBER = "phone number"
        private const val VIEW_ACCOUNT_IRIS = "viewAccountIris"
        private const val BUSINESS_UNIT = "user platform"
        private const val CURRENT_SITE = "tokopediamarketplace"
        private const val CLICK_ACCOUNT = "clickAccount"
        private const val VIEW_REGISTER_PAGE = "view register page"
        private const val CLICK_ON_MASUK = "click on masuk"
        private const val CLICK_ON_METODE_LAIN = "click on button metode lain"
        private const val CLICK_ON_BUTTON_DAFTAR = "click on button daftar"
        private const val CLICK_ON_BUTTON_GOOGLE = "click on button google"

        private const val ID_36093 = "36093"
        private const val ID_36096 = "36096"
        private const val ID_36097 = "36097"
        private const val ID_36098 = "36098"
        private const val ID_36099 = "36099"
        private const val ID_36100 = "36100"
    }

}

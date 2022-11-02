package com.tokopedia.loginregister.redefineregisteremail.common.analytics

import com.tokopedia.track.builder.Tracker
import javax.inject.Inject

class RedefineRegisterEmailAnalytics @Inject constructor() {

    fun sendClickOnButtonDaftarEmailEvent(
        action: String,
        requiredInputPhone: Boolean,
        errorMessage: String = ""
    ) {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction("$CLICK_ON_BUTTON_DAFTAR - $EMAIL")
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel(getActionLabel(action, requiredInputPhone, errorMessage))
            .setCustomProperty(TRACKER_ID, ID_36099)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonLanjutEvent(
        action: String,
        requiredInputPhone: Boolean,
        errorMessage: String = ""
    ) {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction(CLICK_ON_BUTTON_LANJUT)
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel(getActionLabel(action, requiredInputPhone, errorMessage))
            .setCustomProperty(TRACKER_ID, ID_36101)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendViewRegisterPageEnterDataEvent(requiredInputPhone: Boolean) {
        Tracker.Builder()
            .setEvent(VIEW_ACCOUNT_IRIS)
            .setEventAction("$VIEW_REGISTER_PAGE - $ENTER_DATA")
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel("$VIEW - ${getVariant(requiredInputPhone)}")
            .setCustomProperty(TRACKER_ID, ID_36104)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendViewRegisterPageAddPhoneNumberEvent(requiredInputPhone: Boolean) {
        Tracker.Builder()
            .setEvent(VIEW_ACCOUNT_IRIS)
            .setEventAction("$VIEW_REGISTER_PAGE - $ADD_PHONE_NUMBER")
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel("$VIEW - ${getVariant(requiredInputPhone)}")
            .setCustomProperty(TRACKER_ID, ID_36105)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonLanjutAddPhoneNumberEvent(
        action: String,
        requiredInputPhone: Boolean,
        errorMessage: String = ""
    ) {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction("$CLICK_ON_BUTTON_LANJUT - $ADD_PHONE_NUMBER")
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel(getActionLabel(action, requiredInputPhone, errorMessage))
            .setCustomProperty(TRACKER_ID, ID_36106)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonBackEvent(requiredInputPhone: Boolean) {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction(CLICK_ON_BUTTON_BACK)
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel("$ACTION_CLICK - $ADD_PHONE_NUMBER_PAGE - ${getVariant(requiredInputPhone)}")
            .setCustomProperty(TRACKER_ID, ID_36107)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendViewAddPhoneNumberOptionalPageEvent(requiredInputPhone: Boolean) {
        Tracker.Builder()
            .setEvent(VIEW_ACCOUNT_IRIS)
            .setEventAction(VIEW_ADD_PHONE_NUMBER_OPTIONAL_PAGE)
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel("$VIEW - ${getVariant(requiredInputPhone)}")
            .setCustomProperty(TRACKER_ID, ID_36108)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonLanjutAddPnPageOptionalEvent(
        action: String,
        requiredInputPhone: Boolean,
        errorMessage: String = ""
    ) {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction("$CLICK_ON_BUTTON_LANJUT - $ADD_PN_PAGE_OPTIONAL")
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel(getActionLabel(action, requiredInputPhone, errorMessage))
            .setCustomProperty(TRACKER_ID, ID_36109)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonLewatiEvent(requiredInputPhone: Boolean) {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction(CLICK_ON_BUTTON_LEWATI)
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel("$ACTION_CLICK - ${getVariant(requiredInputPhone)}")
            .setCustomProperty(TRACKER_ID, ID_36110)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickUbahPhoneNumberEvent(requiredInputPhone: Boolean) {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction("$CLICK_UBAH_BENAR - $PHONE_NUMBER")
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel("$ACTION_CLICK - ${getVariant(requiredInputPhone)}")
            .setCustomProperty(TRACKER_ID, ID_36111)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickYaBenarPhoneNumberEvent(
        action: String,
        requiredInputPhone: Boolean,
        errorMessage: String = ""
    ) {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction("$CLICK_YA_BENAR - $PHONE_NUMBER")
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel(getActionLabel(action, requiredInputPhone, errorMessage))
            .setCustomProperty(TRACKER_ID, ID_36112)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendViewPopUpPhoneNumberRegisteredPageEvent(requiredInputPhone: Boolean) {
        Tracker.Builder()
            .setEvent(VIEW_ACCOUNT_IRIS)
            .setEventAction(VIEW_POP_UP_PHONE_NUMBER_REGISTERED_PAGE)
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel("$VIEW - ${getVariant(requiredInputPhone)}")
            .setCustomProperty(TRACKER_ID, ID_36113)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickUbahTerdaftarPhoneNumberEvent(requiredInputPhone: Boolean) {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction("$CLICK_UBAH_TERDAFTAR - $PHONE_NUMBER")
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel("$ACTION_CLICK - ${getVariant(requiredInputPhone)}")
            .setCustomProperty(TRACKER_ID, ID_36114)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickYaMasukTerdaftarPhoneNumberEvent(requiredInputPhone: Boolean) {
        Tracker.Builder()
            .setEvent(CLICK_ACCOUNT)
            .setEventAction("$CLICK_YA_MASUK_TERDAFTAR - $PHONE_NUMBER")
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel("$ACTION_CLICK - ${getVariant(requiredInputPhone)}")
            .setCustomProperty(TRACKER_ID, ID_36115)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    fun sendViewPopUpEmailRegisteredPageEvent(requiredInputPhone: Boolean) {
        Tracker.Builder()
            .setEvent(VIEW_ACCOUNT_IRIS)
            .setEventAction(VIEW_POP_UP_EMAIL_REGISTERED_PAGE)
            .setEventCategory(REGISTER_PAGE)
            .setEventLabel("$VIEW - ${getVariant(requiredInputPhone)}")
            .setCustomProperty(TRACKER_ID, ID_36122)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    private fun getActionLabel(
        action: String,
        requiredInputPhone: Boolean,
        errorMessage: String
    ): String {
        return "$action - ${getVariant(requiredInputPhone)}${if (action == ACTION_FAILED) " - $errorMessage" else ""}"
    }

    private fun getVariant(requiredInputPhone: Boolean): String {
        return if (requiredInputPhone) {
            VARIANT_MANDATORY
        } else {
            VARIANT_OPTIONAL
        }
    }

    companion object {
        private const val VARIANT_MANDATORY = "mandatory"
        private const val VARIANT_OPTIONAL = "optional"

        private const val REGISTER_PAGE = "register page"
        private const val VIEW = "view"
        private const val TRACKER_ID = "trackerId"

        const val ACTION_CLICK = "click"
        const val ACTION_SUCCESS = "success"
        const val ACTION_FAILED = "failed"
        private const val ENTER_DATA = "enter data"
        private const val EMAIL = "email"
        private const val ADD_PN_PAGE_OPTIONAL = "add pn page optional"
        private const val ADD_PHONE_NUMBER = "add phone number"
        private const val PHONE_NUMBER = "phone number"
        private const val VIEW_ACCOUNT_IRIS = "viewAccountIris"
        private const val BUSINESS_UNIT = "user platform"
        private const val CURRENT_SITE = "tokopediamarketplace"
        private const val CLICK_ACCOUNT = "clickAccount"
        private const val VIEW_REGISTER_PAGE = "view register page"
        private const val ADD_PHONE_NUMBER_PAGE = "add phone number page"
        private const val VIEW_ADD_PHONE_NUMBER_OPTIONAL_PAGE =
            "view add phone number optional page"
        private const val CLICK_ON_BUTTON_DAFTAR = "click on button daftar"
        private const val CLICK_ON_BUTTON_LANJUT = "click on button lanjut"
        private const val CLICK_ON_BUTTON_BACK = "click on button back"
        private const val CLICK_ON_BUTTON_LEWATI = "click on button lewati"
        private const val CLICK_UBAH_BENAR = "click ubah benar"
        private const val CLICK_UBAH_TERDAFTAR = "click ubah terdaftar"
        private const val CLICK_YA_BENAR = "click ya, benar"
        private const val CLICK_YA_MASUK_TERDAFTAR = "click ya, masuk terdaftar"
        private const val VIEW_POP_UP_PHONE_NUMBER_REGISTERED_PAGE =
            "view pop up phone number registered page"
        private const val VIEW_POP_UP_EMAIL_REGISTERED_PAGE = "view pop up email registered page"

        private const val ID_36099 = "36099"
        private const val ID_36101 = "36101"
        private const val ID_36104 = "36104"
        private const val ID_36105 = "36105"
        private const val ID_36106 = "36106"
        private const val ID_36107 = "36107"
        private const val ID_36108 = "36108"
        private const val ID_36109 = "36109"
        private const val ID_36110 = "36110"
        private const val ID_36111 = "36111"
        private const val ID_36112 = "36112"
        private const val ID_36113 = "36113"
        private const val ID_36114 = "36114"
        private const val ID_36115 = "36115"
        private const val ID_36122 = "36122"

        const val MESSAGE_REGISTERED_PHONE = "nomor telepon sudah terdaftar"
        const val MESSAGE_FAILED_OTP = "gagal verifikasi OTP"
    }

}

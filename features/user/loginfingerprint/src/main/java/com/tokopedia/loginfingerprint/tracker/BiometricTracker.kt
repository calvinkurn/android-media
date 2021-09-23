package com.tokopedia.loginfingerprint.tracker

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class BiometricTracker {

    fun trackOpenVerifyFingerprint(){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_BIOMETRIC,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                EVENT_LABEL_CLICK)
        )
    }

    /* Tracker no. 9 - failed - fp unavailable */
    fun trackOpenVerifyFingerprintBiometricUnavailable(){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_BIOMETRIC,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                "$EVENT_LABEL_FAILED - Biometric not available")
        )
    }

    /* Tracker no. 9 - failed - error */
    fun trackOpenVerifyFingerprintFailed(errorMsg: String){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_BIOMETRIC,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                "$EVENT_LABEL_FAILED - $errorMsg")
        )
    }

    /* Tracker no.2 */
    fun trackButtonCloseVerify(){
       track(
           TrackAppUtils.gtmData(
           EVENT_CLICK_BIOMETRIC,
           CATEGORY_INPUT_BIOMETRIC_PAGE,
           ACTION_CLICK_BTN_CLOSE_VERIFY,
           "")
       )
    }

    /* Tracker no.3 */
    fun trackClickOnLoginWithFingerprintSuccessDevice(){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_BIOMETRIC,
                CATEGORY_INPUT_BIOMETRIC_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                "$EVENT_LABEL_SUCCESS - device")
        )
    }

    /* Tracker no.3 */
    fun trackClickOnLoginWithFingerprintFailedDevice(errorMsg: String){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_BIOMETRIC,
                CATEGORY_INPUT_BIOMETRIC_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                "$EVENT_LABEL_FAILED - device - $errorMsg")
        )
    }

    /* Tracker no.4 */
    fun trackClickOnLoginWithFingerprintSuccessBackend(){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_BIOMETRIC,
                CATEGORY_INPUT_BIOMETRIC_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                "$EVENT_LABEL_SUCCESS - backend")
        )
    }

    /* Tracker no.4 */
    fun trackClickOnLoginWithFingerprintFailedBackend(errorMsg: String){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_BIOMETRIC,
                CATEGORY_INPUT_BIOMETRIC_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                "$EVENT_LABEL_FAILED - backend - $errorMsg")
        )
    }

    fun trackClickOnBiometricMenu(){
        track(
            TrackAppUtils.gtmData(
                EVENT_BIOMETRIC_SETTING,
                CATEGORY_ACCOUNT_SETTING_PAGE,
                ACTION_CLICK_MENU_BIOMETRIC,
                "$EVENT_LABEL_CLICK - $EVENT_LABEL_FINGERPRINT")
        )
    }

    /* Tracker no.7 */
    fun trackRegisterFpSuccess(){
        track(
            TrackAppUtils.gtmData(
                EVENT_BIOMETRIC_SETTING,
                CATEGORY_ACCOUNT_SETTING_PAGE,
                ACTION_CLICK_MENU_BIOMETRIC,
                "$EVENT_LABEL_CLICK - $EVENT_LABEL_FINGERPRINT - enable")
        )
    }

    fun trackRegisterFpFailed(errorMsg: String){
        track(
            TrackAppUtils.gtmData(
                EVENT_BIOMETRIC_SETTING,
                CATEGORY_ACCOUNT_SETTING_PAGE,
                ACTION_CLICK_MENU_BIOMETRIC,
                "$EVENT_LABEL_CLICK - $EVENT_LABEL_FINGERPRINT - enable - $EVENT_LABEL_FAILED - $errorMsg")
        )
    }

    /* Tracker no.7 */
    fun trackRemoveFingerprintSuccess(){
        track(
            TrackAppUtils.gtmData(
                EVENT_BIOMETRIC_SETTING,
                CATEGORY_ACCOUNT_SETTING_PAGE,
                ACTION_CLICK_MENU_BIOMETRIC,
                "$EVENT_LABEL_CLICK - $EVENT_LABEL_FINGERPRINT - disable")
        )
    }

    /* Tracker no.7 */
    fun trackRemoveFingerprintFailed(errorMsg: String){
        track(
            TrackAppUtils.gtmData(
                EVENT_BIOMETRIC_SETTING,
                CATEGORY_ACCOUNT_SETTING_PAGE,
                ACTION_CLICK_MENU_BIOMETRIC,
                "$EVENT_LABEL_CLICK - $EVENT_LABEL_FINGERPRINT - disable - $EVENT_LABEL_FAILED - $errorMsg")
        )
    }

    fun trackClickBtnBackAccountSetting(){
        track(
            TrackAppUtils.gtmData(
                EVENT_BIOMETRIC_SETTING,
                CATEGORY_ACCOUNT_SETTING_PAGE,
                ACTION_CLICK_BTN_BACK,
                "")
        )
    }

    /* Tracker no. 9 - success */
    fun trackOpenVerifyPage() {
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_BIOMETRIC,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_BIOMETRIC_LOGIN,
                "$EVENT_LABEL_SUCCESS - fingerprint")
        )
    }

    private fun track(map: MutableMap<String, Any>) {
        map[KEY_BUSINESS_UNIT] = BUSSINESS_UNIT
        map[KEY_CURRENT_SITE] = CURRENT_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    companion object {
        const val EVENT_CLICK_BIOMETRIC = "clickBiometrics"
        const val EVENT_BIOMETRIC_SETTING = "clickAccountSetting"

        const val ACTION_CLICK_LOGIN_FINGERPRINT = "click on masuk dengan fingerprint"
        const val ACTION_CLICK_BTN_CLOSE_VERIFY = "click on button close widget biometric"
        const val ACTION_CLICK_MENU_BIOMETRIC = "click on button biometric"
        const val ACTION_CLICK_BTN_BACK = "click on button back biometric"
        const val ACTION_CLICK_BIOMETRIC_LOGIN = "click on metode biometric"

        const val CATEGORY_LOGIN_PAGE = "login page"
        const val CATEGORY_ACCOUNT_SETTING_PAGE = "account page"
        const val CATEGORY_INPUT_BIOMETRIC_PAGE = "input biometric page"

        const val EVENT_LABEL_SUCCESS = "success"

        const val EVENT_LABEL_CLICK = "click"
        const val EVENT_LABEL_FAILED = "failed"

        const val EVENT_LABEL_FINGERPRINT= "fingerprint"


        private const val KEY_BUSINESS_UNIT = "businessUnit"
        private const val KEY_CURRENT_SITE = "currentSite"

        private const val BUSSINESS_UNIT = "user platform"
        private const val CURRENT_SITE = "tokopediamarketplace"

    }
}
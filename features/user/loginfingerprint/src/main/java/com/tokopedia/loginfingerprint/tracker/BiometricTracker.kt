package com.tokopedia.loginfingerprint.tracker

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class BiometricTracker {

    fun trackOpenVerifyFingerprint(){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                EVENT_LABEL_CLICK)
        )
    }

    fun trackOpenVerifyFingerprintBiometricUnavailable(){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                "$EVENT_LABEL_FAILED - Biometric not available")
        )
    }

    fun trackOpenVerifyFingerprintFailed(errorMsg: String){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                "$EVENT_LABEL_FAILED - $errorMsg")
        )
    }

    fun trackButtonCloseVerify(){
       track(
           TrackAppUtils.gtmData(
           EVENT_CLICK_LOGIN,
           CATEGORY_LOGIN_PAGE,
           ACTION_CLICK_BTN_CLOSE_VERIFY,
           "")
       )
    }

    fun trackClickOnLoginWithFingerprintSuccessDevice(){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                "$EVENT_LABEL_SUCCESS - device")
        )
    }

    fun trackClickOnLoginWithFingerprintFailedDevice(errorMsg: String){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                "$EVENT_LABEL_FAILED - device - $errorMsg")
        )
    }

    fun trackClickOnLoginWithFingerprintSuccessBackend(){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                "$EVENT_LABEL_SUCCESS - backend")
        )
    }

    fun trackClickOnLoginWithFingerprintFailedBackend(errorMsg: String){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                "$EVENT_LABEL_FAILED - backend - $errorMsg")
        )
    }

    fun trackOnLoginFingerprintSuccess(){
        track(
            TrackAppUtils.gtmData(
                EVENT_CLICK_LOGIN,
                CATEGORY_LOGIN_PAGE,
                ACTION_CLICK_LOGIN_FINGERPRINT,
                EVENT_LABEL_SUCCESS)
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

    fun trackClickBiometricSwitch(isEnable: Boolean){
        track(
            TrackAppUtils.gtmData(
                EVENT_BIOMETRIC_SETTING,
                CATEGORY_ACCOUNT_SETTING_PAGE,
                ACTION_CLICK_MENU_BIOMETRIC,
                "$EVENT_LABEL_CLICK - $EVENT_LABEL_FINGERPRINT - $isEnable")
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


    private fun track(map: MutableMap<String, Any>) {
        map[KEY_BUSINESS_UNIT] = BUSSINESS_UNIT
        map[KEY_CURRENT_SITE] = CURRENT_SITE
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    companion object {
        const val EVENT_CLICK_LOGIN = "clickLogin"
        const val EVENT_BIOMETRIC_SETTING = "clickAccountSetting"

        const val ACTION_CLICK_LOGIN_FINGERPRINT = "click on masuk dengan fingerprint"
        const val ACTION_CLICK_BTN_CLOSE_VERIFY = "click on button close widget biometric"
        const val ACTION_CLICK_MENU_BIOMETRIC = "click on button biometric"
        const val ACTION_CLICK_BTN_BACK = "click on button back biometric"
        const val ACTION_CLICK_BIOMETRIC_LOGIN = "click on metode biometric"

        const val CATEGORY_LOGIN_PAGE = "login page"
        const val CATEGORY_ACCOUNT_SETTING_PAGE = "login page"

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
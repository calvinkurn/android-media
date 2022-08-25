package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalUserPlatform {

    private const val HOST_USER = "user"

    private const val INTERNAL_USER = "${DeeplinkConstant.SCHEME_INTERNAL}://${ApplinkConstInternalGlobal.HOST_GLOBAL}"

    const val NEW_INTERNAL_USER = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_USER}"

    const val METHOD_LOGIN_EMAIL = "email"
    const val METHOD_LOGIN_PHONE = "phone"
    const val METHOD_LOGIN_GOOGLE = "google"
    const val METHOD_LOGIN_FACEBOOK = "facebook"
    const val METHOD_LOGIN_FINGERPRINT = "fingerprint"
    const val PAGE_EDIT_INFO_PROFILE_BIO = "bio"
    const val PAGE_EDIT_INFO_PROFILE_USERNAME = "username"
    const val PAGE_EDIT_INFO_PARAM = "page"

    const val PARAM_IS_RETURN_HOME = "return_to_home"
    const val PARAM_IS_CLEAR_DATA_ONLY = "is_clear_data_only"

    //LoginActivity
    const val LOGIN = "$INTERNAL_USER/login"
    const val LOGIN_EMAIL = "${LOGIN}?method=$METHOD_LOGIN_EMAIL&e={email}&source={source}"
    const val LOGIN_PHONE = "${LOGIN}?method=$METHOD_LOGIN_PHONE&p={phone}&source={source}"
    const val LOGIN_THIRD_PARTY = "${LOGIN}?method={method}&source={source}"

    // SilentVerificationActivity
    // tokopedia-android-internal://user/silent-verification
    const val SILENT_VERIFICAITON = "${NEW_INTERNAL_USER}/silent-verification"

    // PinOnboardingActivity
    const val ADD_PIN_ONBOARDING = "${NEW_INTERNAL_USER}/add-pin-onboarding"

    // PinCompleteActivity
    const val ADD_PIN_COMPLETE = "${NEW_INTERNAL_USER}/add-pin-complete"

    // SettingFingerprintActivity
    const val BIOMETRIC_SETTING = "${NEW_INTERNAL_USER}/biometric-setting"

    // VerifyFingerprintActivity
    const val VERIFY_BIOMETRIC = "${NEW_INTERNAL_USER}/verify-fingerprint"

    // RegisterFingerprintActivity
    const val REGISTER_BIOMETRIC = "${NEW_INTERNAL_USER}/register-fingerprint"

    // InactivePhoneActivity
    const val CHANGE_INACTIVE_PHONE = "${NEW_INTERNAL_USER}/change-inactive-phone"

    //InputOldPhoneNumberActivity
    const val INPUT_OLD_PHONE_NUMBER = "${NEW_INTERNAL_USER}/input-old-phone-number"

    // HomeAccountUserActivity
    const val NEW_HOME_ACCOUNT = "${NEW_INTERNAL_USER}/new-home-account"

    // BiometricOfferingActivity
    const val BIOMETRIC_OFFERING = "${NEW_INTERNAL_USER}/biometric-offering"

    /**
     * LogoutActivity
     * @applink : tokopedia-android-internal://user/logout
     * @param   : [PARAM_IS_RETURN_HOME]
     * default is 'true', set 'false' if you need the logout result
     **/
    const val LOGOUT = "${NEW_INTERNAL_USER}/logout"

    // TwoFactorActivity
    // tokopedia-android-internal://user/two-factor-register
    const val TWO_FACTOR_REGISTER = "${NEW_INTERNAL_USER}/two-factor-register"

    // LinkAccountReminderActivity
    // tokopedia-android-internal://user/link-acc-reminder
    const val LINK_ACC_REMINDER = "${NEW_INTERNAL_USER}/link-acc-reminder"

    const val NEW_PROFILE_INFO = "${NEW_INTERNAL_USER}/profile-info"
    const val EDIT_PROFILE_INFO = "${NEW_INTERNAL_USER}/edit-profile-info"

    // SettingProfileActivity
    const val SETTING_PROFILE = "${NEW_INTERNAL_USER}/setting-profile"

    /**
     * ExplicitProfileActivity
     * @Applink : tokopedia-android-internal://user/explicit-profile
     **/
    const val EXPLICIT_PROFILE = "${NEW_INTERNAL_USER}/explicit-profile"

    /**
     * GotoSeamlessLandingActivity
     * @Applink : tokopedia-android-internal://user/goto-seamless-login
     **/
    const val GOTO_SEAMLESS_LOGIN = "${NEW_INTERNAL_USER}/goto-seamless-login"

    /**
    * PrivacyAccountActivity
    * @Applink : tokopedia-android-internal://user/privacy-account
    **/
    const val PRIVACY_ACCOUNT = "${NEW_INTERNAL_USER}/privacy-account"


    /**
     * ChooseAccountActivity
     * @Applink : tokopedia-android-internal://user/choose-account
     **/
    const val CHOOSE_ACCOUNT = "${NEW_INTERNAL_USER}/choose-account"

    /**
     * ChooseAccountActivity
     * @Applink : tokopedia-android-internal://user/choose-account-fingerprint
     **/
    const val CHOOSE_ACCOUNT_FINGERPRINT = "${NEW_INTERNAL_USER}/choose-account-fingerprint"

    /**
     * VerificationActivity
     * @Applink : tokopedia-android-internal://user/cotp
     **/
    const val COTP = "${NEW_INTERNAL_USER}/cotp"

    /**
     * LoginByQrResultActivity
     * @Applink : tokopedia-android-internal://user/qr-login-result
     **/
    const val QR_LOGIN_RESULT = "${NEW_INTERNAL_USER}/qr-login-result"

    /**
     * LoginByQrActivity
     * @Applink : tokopedia-android-internal://user/qr-login
     **/
    const val QR_LOGIN = "${NEW_INTERNAL_USER}/qr-login"

    /**
     * ReceiverNotifActivity
     * @Applink : tokopedia-android-internal://global/otp-push-notif-receiver
     **/
    const val OTP_PUSH_NOTIF_RECEIVER = "${NEW_INTERNAL_USER}/otp-push-notif-receiver"

    /**
     * SettingNotifActivity
     * @Applink : tokopedia-android-internal://global/otp-push-notif-receiver
     **/
    const val OTP_PUSH_NOTIF_SETTING = "${NEW_INTERNAL_USER}/otp-push-notif-setting"

    /**
     * com.tokopedia.loginregister.register_email.view.activity.RedefineRegisterEmailActivity
     * @Applink : tokopedia-android-internal://user/redefine-register-email
     **/
    const val REDEFINE_REGISTER_EMAIL = "$NEW_INTERNAL_USER/redefine-register-email"

}
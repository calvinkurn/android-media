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
     * TkpdPaySettingActivity
     * @Applink : tokopedia-android-internal://user/payment-setting
     **/
    const val PAYMENT_SETTING = "${NEW_INTERNAL_USER}/payment-setting"

    /**
     * AccountSettingActivity
     * @Applink : tokopedia-android-internal://user/account-setting
     **/
    const val ACCOUNT_SETTING = "${NEW_INTERNAL_USER}/account-setting"

    /**
     * MediaQualitySettingActivity
     * @Applink : tokopedia-android-internal://user/media-quality-setting
     **/
    const val MEDIA_QUALITY_SETTING = "${NEW_INTERNAL_USER}/media-quality-setting"

    /**
     * FundsAndInvestmentActivity
     * @Applink : tokopedia-android-internal://user/funds-and-investment
     **/
    const val FUNDS_AND_INVESTMENT = "${NEW_INTERNAL_USER}/funds-and-investment"

    /**
     * LinkAccountWebViewActivity
     * @Applink : tokopedia-android-internal://user/link-account-webview
     * @param   : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_LD]
     **/
    const val LINK_ACCOUNT_WEBVIEW = "${NEW_INTERNAL_USER}/link-account-webview"

    /**
     * LinkAccountActivity
     * @Applink : tokopedia-android-internal://user/link-account
     **/
    const val LINK_ACCOUNT = "${NEW_INTERNAL_USER}/link-account"

    /**
     * AddNameRegisterPhoneActivity
     * @Applink : tokopedia-android-internal://user/add-name-register
     **/
    const val ADD_NAME_REGISTER = "${NEW_INTERNAL_USER}/add-name-register"

    /**
     * AddNameRegisterPhoneActivity
     * @Applink : tokopedia-android-internal://user/add-name-register/clean-view
     **/
    const val ADD_NAME_REGISTER_CLEAN_VIEW = "${NEW_INTERNAL_USER}/add-name-register/clean-view"

    /**
     * ChangeGenderActivity
     * @Applink : tokopedia-android-internal://user/change-gender
     **/
    const val CHANGE_GENDER = "${NEW_INTERNAL_USER}/change-gender"

    /**
     * AddEmailActivity
     * @Applink : tokopedia-android-internal://user/add-email
     **/
    const val ADD_EMAIL = "${NEW_INTERNAL_USER}/add-email"

    /**
     * AddPhoneActivity
     * @Applink : tokopedia-android-internal://user/add-phone
     **/
    const val ADD_PHONE = "${NEW_INTERNAL_USER}/add-phone"

    /**
     * AddPhoneActivity
     * @Applink : tokopedia-android-internal://user/add-phone?phone={phone-number}
     **/
    const val ADD_PHONE_WITH = "${NEW_INTERNAL_USER}/add-phone?phone={phone-number}"

    /**
     * AddBodActivity
     * @Applink : tokopedia-android-internal://user/add-bod
     * @param   : [PARAM_BOD_TITLE]
     * @param   : [PARAM_BOD]
     **/
    const val ADD_BOD = "${NEW_INTERNAL_USER}/add-bod"
    const val PARAM_BOD_TITLE = "bodTitle"
    const val PARAM_BOD = "bod"

    /**
     * AddPinActivity
     * @Applink : tokopedia-android-internal://user/add-pin
     **/
    const val ADD_PIN = "${NEW_INTERNAL_USER}/add-pin"

    /**
     * AddPinFrom2FAActivity
     * @Applink : tokopedia-android-internal://user/add-pin-from-2fa
     **/
    const val ADD_PIN_FROM_2FA = "${NEW_INTERNAL_USER}/add-pin-from-2fa"

    /**
     * ChangePinActivity
     * @Applink : tokopedia-android-internal://user/change-pin
     **/
    const val CHANGE_PIN = "${NEW_INTERNAL_USER}/change-pin"

    /**
     * ChangeNameActivity
     * @Applink : tokopedia-android-internal://user/change-name
     *
     **/
    const val CHANGE_NAME = "${NEW_INTERNAL_USER}/change-name?oldName={oldName}&chances={chances}"
    const val PARAM_FULL_NAME = "oldName"
    const val PARAM_CHANCE_CHANGE_NAME = "chances"

    /**
     * ProfileCompletionActivity
     * @Applink : tokopedia-android-internal://user/profile-completion
     **/
    const val PROFILE_COMPLETION = "${NEW_INTERNAL_USER}/profile-completion"

    /**
     * AddNameActivity
     * @Applink : tokopedia-android-internal://user/manage-name
     **/
    const val MANAGE_NAME = "${NEW_INTERNAL_USER}/manage-name"

    /**
     * HasPasswordActivity
     * @Applink : tokopedia-android-internal://user/has-password
     **/
    const val HAS_PASSWORD = "${NEW_INTERNAL_USER}/has-password"

    /**
     * AddPasswordActivity
     * @Applink : tokopedia-android-internal://user/add-password
     **/
    const val ADD_PASSWORD = "${NEW_INTERNAL_USER}/add-password"

    /**
     * ForgotPasswordActivity
     * @applink : tokopedia-android-internal://user/forgot-password
     * @param
     * required : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_EMAIL]
     * optional : [PARAM_AUTO_RESET]
     * optional : [PARAM_REMOVE_FOOTER]
     **/
    const val FORGOT_PASSWORD = "${NEW_INTERNAL_USER}/forgot-password"
    const val PARAM_AUTO_RESET = "auto_reset"
    const val PARAM_REMOVE_FOOTER = "remove_footer"

}
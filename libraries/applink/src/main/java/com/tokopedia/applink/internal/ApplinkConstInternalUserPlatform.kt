package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_IS_RETURN_HOME

object ApplinkConstInternalUserPlatform {

    private const val HOST_USER = "user"

    private const val INTERNAL_USER = "${DeeplinkConstant.SCHEME_INTERNAL}://${ApplinkConstInternalGlobal.HOST_GLOBAL}"

    const val NEW_INTERNAL_USER = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_USER}"

    const val METHOD_LOGIN_EMAIL = "email"
    const val METHOD_LOGIN_PHONE = "phone"
    const val METHOD_LOGIN_GOOGLE = "google"
    const val METHOD_LOGIN_FACEBOOK = "facebook"
    const val METHOD_LOGIN_FINGERPRINT = "fingerprint"

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

    // HomeAccountUserActivity
    const val NEW_HOME_ACCOUNT = "${NEW_INTERNAL_USER}/new-home-account"

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

}
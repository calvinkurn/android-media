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
     * ExplicitProfileActivity
     * @Applink : tokopedia-android-internal://global/explicit-profile
     **/
    const val EXPLICIT_PROFILE = "${ApplinkConstInternalGlobal.INTERNAL_GLOBAL}/explicit-profile"
}
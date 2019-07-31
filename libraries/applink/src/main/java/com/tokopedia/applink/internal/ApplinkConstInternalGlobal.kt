package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://global".
 */
object ApplinkConstInternalGlobal {

    @JvmField
    val HOST_GLOBAL = "global"

    //Extras
    @JvmField
    val PARAM_UUID = "uuid"
    @JvmField
    val PARAM_MSISDN = "msisdn"
    @JvmField
    val PARAM_PHONE = "phone"
    @JvmField
    val PARAM_BOD = "bod"
    @JvmField
    val PARAM_CIPF_USER_ID = "userId"
    @JvmField
    val PARAM_CIPF_OLD_PHONE = "oldPhone"
    @JvmField
    val PARAM_EMAIL = "email"
    @JvmField
    val PARAM_SOURCE = "source"

    //VerificationActivity Param
    @JvmField
    val PARAM_CAN_USE_OTHER_METHOD = "can_use_other_method"
    @JvmField
    val PARAM_IS_SHOW_CHOOSE_METHOD = "is_show_choose_method"
    @JvmField
    val PARAM_OTP_TYPE = "otp_type"
    @JvmField
    val PARAM_REQUEST_OTP_MODE = "request_otp_mode"
    @JvmField
    val PARAM_OTP_CODE = "otp_code"

    @JvmField
    val INTERNAL_GLOBAL = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_GLOBAL}"

    // WithdrawActivity
    // tokopedia-android-internal://global/withdraw
    @JvmField
    val WITHDRAW = "$INTERNAL_GLOBAL/withdraw"

    // ChangeInactiveFormRequestActivity
    // tokopedia-android-internal://global/change-inactive-phone-form
    @JvmField
    val CHANGE_INACTIVE_PHONE_FORM = "$INTERNAL_GLOBAL/change-inactive-phone-form"

    //ChooseTokocashAccountActivity
    // tokopedia-android-internal://global/choose-account
    @JvmField
    val CHOOSE_ACCOUNT = "$INTERNAL_GLOBAL/choose-account"

    // AddNameRegisterActivity
    // tokopedia-android-internal://global/add-name-register
    @JvmField
    val ADD_NAME_REGISTER = "$INTERNAL_GLOBAL/add-name-register"

    // ForgotPasswordActivity
    // tokopedia-android-internal://global/add-name-register
    @JvmField
    val FORGOT_PASSWORD = "$INTERNAL_GLOBAL/forgot-password"

    // ProfileCompletionActivity
    // tokopedia-android-internal://global/profile-completion
    @JvmField
    val PROFILE_COMPLETION = "$INTERNAL_GLOBAL/profile-completion"

    // SettingProfileActivity
    // tokopedia-android-internal://global/setting-profile
    @JvmField
    val SETTING_PROFILE = "$INTERNAL_GLOBAL/setting-profile"

    // ChangeGenderActivity
    // tokopedia-android-internal://global/change-gender
    @JvmField
    val CHANGE_GENDER = "$INTERNAL_GLOBAL/change-gender"

    // AddEmailActivity
    // tokopedia-android-internal://global/add-email
    @JvmField
    val ADD_EMAIL = "$INTERNAL_GLOBAL/add-email"

    // AddEmailActivity
    // tokopedia-android-internal://global/add-phone
    @JvmField
    val ADD_PHONE = "$INTERNAL_GLOBAL/add-phone"

    // AddBodActivity
    // tokopedia-android-internal://global/add-bod
    @JvmField
    val ADD_BOD = "$INTERNAL_GLOBAL/add-bod"

    // VerificationActivity
    // tokopedia-android-internal://global/cotp
    @JvmField
    val COTP = "$INTERNAL_GLOBAL/cotp"

    // ChangePhoneNumberWarningActivity
    // tokopedia-android-internal://global/change-phone-number
    @JvmField
    val CHANGE_PHONE_NUMBER = "$INTERNAL_GLOBAL/change-phone-number"

    // WebViewActivity (Web View in library)
    // Solution for sellerapp that does not have AppLinkWebsiteActivity
    // Activity can have title by putting "title=.."
    @JvmField
    val WEBVIEW = "$INTERNAL_GLOBAL/webview?url={url}"

    // PhoneVerificationProfileActivity
    // tokopedia-android-internal://global/setting-profile-phone-verification
    @JvmField
    val SETTING_PROFILE_PHONE_VERIFICATION = "$INTERNAL_GLOBAL/setting-profile-phone-verification"

}

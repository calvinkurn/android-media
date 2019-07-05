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
    val PARAM_CIPF_USER_ID = "userId"
    @JvmField
    val PARAM_CIPF_OLD_PHONE = "oldPhone"
    @JvmField
    val PARAM_EMAIL = "email"

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

    // VerificationActivity
    // tokopedia-android-internal://global/cotp
    @JvmField
    val COTP = "$INTERNAL_GLOBAL/cotp"
}

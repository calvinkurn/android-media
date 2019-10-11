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
    val PARAM_BOD_TITLE = "bodTitle"
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

    // UserIdentificationInfoActivity
    // tokopedia-android-internal://global/user-identification-info
    @JvmField
    val USER_IDENTIFICATION_INFO = "$INTERNAL_GLOBAL/user-identification-info"
    @JvmField
    val PARAM_SOURCE_KYC_SELLER = "seller"

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

    // AddPinActivity
    // tokopedia-android-internal://global/add-pin
    @JvmField
    val ADD_PIN = "$INTERNAL_GLOBAL/add-pin"

    // PinOnboardingActivity
    // tokopedia-android-internal://global/add-pin-onboarding
    @JvmField
    val ADD_PIN_ONBOARDING = "$INTERNAL_GLOBAL/add-pin-onboarding"

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

    // SettingBankActivity
    // tokopedia-android-internal://global/setting-bank
    @JvmField
    val SETTING_BANK = "$INTERNAL_GLOBAL/setting-bank"

    @JvmField
    val GLOBAL_INTERNAL_DIGITAL_DEAL = "$INTERNAL_GLOBAL/deals"

    @JvmField
    val GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG_BASE = "$INTERNAL_GLOBAL/deals-slug/"

    @JvmField
    val GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG = "$GLOBAL_INTERNAL_DIGITAL_DEAL_SLUG_BASE{slug}/"

    @JvmField
    val GLOBAL_INTERNAL_DIGITAL_DEAL_CATEGORY = "$INTERNAL_GLOBAL/deals/category/page"

    @JvmField
    val GLOBAL_INTERNAL_DIGITAL_DEAL_ALL_BRANDS_BASE = "$INTERNAL_GLOBAL/deals-allbrands/"

    @JvmField
    val GLOBAL_INTERNAL_DIGITAL_DEAL_ALL_BRANDS = "$GLOBAL_INTERNAL_DIGITAL_DEAL_ALL_BRANDS_BASE{isVoucher}/"

    @JvmField
    val GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL_BASE = "$INTERNAL_GLOBAL/deals-brand/"

    @JvmField
    val GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL = "$GLOBAL_INTERNAL_DIGITAL_DEAL_BRAND_DETAIL_BASE{slug}/"

    // PhoneVerificationProfileActivity
    // tokopedia-android-internal://global/setting-bank
    @JvmField
    val ADD_BANK = "$INTERNAL_GLOBAL/add-bank"
    @JvmField
    val PARAM_ACCOUNT_ID = "account_id"
    @JvmField
    val PARAM_ACCOUNT_NAME = "account_name"
    @JvmField
    val PARAM_ACCOUNT_NO = "account_number"
    @JvmField
    val PARAM_BANK_ID = "bank_id"
    @JvmField
    val PARAM_BANK_NAME = "bank_name"

    @JvmField
    val DYNAMIC_FEATURE_INSTALL_BASE= "$INTERNAL_GLOBAL/dynamic-features-install/"

    // DFInstallerActivity
    // tokopedia-android-internal://global/dynamic-features-install/hotel/?
    // auto = true will download when activity is open
    // applink, if provided will launch the applink after the module is installed
    // imageUrl, is the placeholder for the background
    @JvmField
    val DYNAMIC_FEATURE_INSTALL= DYNAMIC_FEATURE_INSTALL_BASE + "{module}/?" +
        "dfname={moduleTranslate}&" +
        "dfapplink={encodedApplink}&" +
        "dfauto={isAutoDownload}&" +
        "dfimage={imageUrl}"

}

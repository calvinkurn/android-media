package com.tokopedia.applink.internal

import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalUserPlatform {

    private const val HOST_USER = "user"

//    private const val INTERNAL_USER = "${DeeplinkConstant.SCHEME_INTERNAL}://${ApplinkConstInternalGlobal.HOST_GLOBAL}"

    const val NEW_INTERNAL_USER = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_USER"

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
    const val PARAM_IS_FROM_STICKY_LOGIN = "from_sticky_login"
    const val PARAM_IS_FROM_OCL_LOGIN = "from_ocl_page"

    const val PARAM_IS_SAVE_SESSION = "is_save_session"

    // LoginActivity
    const val LOGIN = "$NEW_INTERNAL_USER/login"
    const val LOGIN_EMAIL = "$LOGIN?method=$METHOD_LOGIN_EMAIL&e={email}&source={source}"
    const val LOGIN_PHONE = "$LOGIN?method=$METHOD_LOGIN_PHONE&p={phone}&source={source}"
    const val LOGIN_THIRD_PARTY = "$LOGIN?method={method}&source={source}"

    // SilentVerificationActivity
    // tokopedia-android-internal://user/silent-verification
    const val SILENT_VERIFICAITON = "$NEW_INTERNAL_USER/silent-verification"

    // PinOnboardingActivity
    const val ADD_PIN_ONBOARDING = "$NEW_INTERNAL_USER/add-pin-onboarding"

    // PinCompleteActivity
    const val ADD_PIN_COMPLETE = "$NEW_INTERNAL_USER/add-pin-complete"

    // SettingFingerprintActivity
    const val BIOMETRIC_SETTING = "$NEW_INTERNAL_USER/biometric-setting"

    // VerifyFingerprintActivity
    const val VERIFY_BIOMETRIC = "$NEW_INTERNAL_USER/verify-fingerprint"

    // RegisterFingerprintActivity
    const val REGISTER_BIOMETRIC = "$NEW_INTERNAL_USER/register-fingerprint"

    // InactivePhoneActivity
    const val CHANGE_INACTIVE_PHONE = "$NEW_INTERNAL_USER/change-inactive-phone"

    // InputOldPhoneNumberActivity
    const val INPUT_OLD_PHONE_NUMBER = "$NEW_INTERNAL_USER/input-old-phone-number"

    // HomeAccountUserActivity
    const val NEW_HOME_ACCOUNT = "$NEW_INTERNAL_USER/new-home-account"

    // BiometricOfferingActivity
    const val BIOMETRIC_OFFERING = "$NEW_INTERNAL_USER/biometric-offering"

    /**
     * LogoutActivity
     * @applink : tokopedia-android-internal://user/logout
     * @param : [PARAM_IS_RETURN_HOME]
     * default is 'true', set 'false' if you need the logout result
     **/
    const val LOGOUT = "$NEW_INTERNAL_USER/logout"

    // TwoFactorActivity
    // tokopedia-android-internal://user/two-factor-register
    const val TWO_FACTOR_REGISTER = "$NEW_INTERNAL_USER/two-factor-register"

    // LinkAccountReminderActivity
    // tokopedia-android-internal://user/link-acc-reminder
    const val LINK_ACC_REMINDER = "$NEW_INTERNAL_USER/link-acc-reminder"

    const val EDIT_PROFILE_INFO = "$NEW_INTERNAL_USER/edit-profile-info"

    // ProfileInfoActivity
    const val SETTING_PROFILE = "$NEW_INTERNAL_USER/setting-profile"

    /**
     * ExplicitProfileActivity
     * @Applink : tokopedia-android-internal://user/explicit-profile
     **/
    const val EXPLICIT_PROFILE = "$NEW_INTERNAL_USER/explicit-profile"

    /**
     * GotoSeamlessLandingActivity
     * @Applink : tokopedia-android-internal://user/goto-seamless-login
     **/
    const val GOTO_SEAMLESS_LOGIN = "$NEW_INTERNAL_USER/goto-seamless-login"

    /**
     * PrivacyAccountActivity
     * @Applink : tokopedia-android-internal://user/privacy-account
     **/
    const val PRIVACY_ACCOUNT = "$NEW_INTERNAL_USER/privacy-account"

    /**
     * ChooseAccountActivity
     * @Applink : tokopedia-android-internal://user/choose-account
     **/
    const val CHOOSE_ACCOUNT = "$NEW_INTERNAL_USER/choose-account"

    /**
     * ChooseAccountActivity
     * @Applink : tokopedia-android-internal://user/choose-account-fingerprint
     **/
    const val CHOOSE_ACCOUNT_FINGERPRINT = "$NEW_INTERNAL_USER/choose-account-fingerprint"

    /**
     * OclChooseAccountActivity
     * @Applink : tokopedia-android-internal://user/choose-account-fingerprint
     **/
    const val CHOOSE_ACCOUNT_OCL = "${NEW_INTERNAL_USER}/choose-account-ocl"

    /**
     * VerificationActivity
     * @Applink : tokopedia-android-internal://user/cotp
     **/
    const val COTP = "$NEW_INTERNAL_USER/cotp"

    /**
     * LoginByQrResultActivity
     * @Applink : tokopedia-android-internal://user/qr-login-result
     **/
    const val QR_LOGIN_RESULT = "$NEW_INTERNAL_USER/qr-login-result"

    /**
     * LoginByQrActivity
     * @Applink : tokopedia-android-internal://user/qr-login
     **/
    const val QR_LOGIN = "$NEW_INTERNAL_USER/qr-login"

    /**
     * ReceiverNotifActivity
     * @Applink : tokopedia-android-internal://user/otp-push-notif-receiver
     **/
    const val OTP_PUSH_NOTIF_RECEIVER = "$NEW_INTERNAL_USER/otp-push-notif-receiver"

    /**
     * SettingNotifActivity
     * @Applink : tokopedia-android-internal://user/otp-push-notif-receiver
     **/
    const val OTP_PUSH_NOTIF_SETTING = "$NEW_INTERNAL_USER/otp-push-notif-setting"

    /**
     * SeamlessLoginEmailPhoneActivity
     * @Applink : tokopedia-android-internal://user/login-seamless
     **/
    const val SEAMLESS_LOGIN = "$NEW_INTERNAL_USER/login-seamless"

    /**
     * TermPrivacyActivity
     * @applink : tokopedia-android-internal://user/term-privacy/{page}/
     **/
    const val TERM_PRIVACY = "$NEW_INTERNAL_USER/term-privacy/{page}/"

    // LandingShopCreationActivity
    // tokopedia-android-internal://user/landing-shop-creation
    const val LANDING_SHOP_CREATION = "$NEW_INTERNAL_USER/landing-shop-creation"

    // PhoneShopCreationActivity
    // tokopedia-android-internal://user/phone-shop-creation
    const val PHONE_SHOP_CREATION = "$NEW_INTERNAL_USER/phone-shop-creation"

    // NameShopCreationActivity
    // tokopedia-android-internal://user/name-shop-creation
    const val NAME_SHOP_CREATION = "$NEW_INTERNAL_USER/name-shop-creation"

    // RegisterInitialActivity
    // tokopedia-android-internal://user/init-register
    const val INIT_REGISTER = "$NEW_INTERNAL_USER/init-register"

    // RegisterEmailActivity
    // tokopedia-android-internal://user/email-register
    const val EMAIL_REGISTER = "$NEW_INTERNAL_USER/email-register"

    /**
     * TkpdPaySettingActivity
     * @Applink : tokopedia-android-internal://user/payment-setting
     **/
    const val PAYMENT_SETTING = "$NEW_INTERNAL_USER/payment-setting"

    /**
     * AccountSettingActivity
     * @Applink : tokopedia-android-internal://user/account-setting
     **/
    const val ACCOUNT_SETTING = "$NEW_INTERNAL_USER/account-setting"

    /**
     * MediaQualitySettingActivity
     * @Applink : tokopedia-android-internal://user/media-quality-setting
     **/
    const val MEDIA_QUALITY_SETTING = "$NEW_INTERNAL_USER/media-quality-setting"

    /**
     * FundsAndInvestmentActivity
     * @Applink : tokopedia-android-internal://user/funds-and-investment
     **/
    const val FUNDS_AND_INVESTMENT = "$NEW_INTERNAL_USER/funds-and-investment"

    /**
     * LinkAccountWebViewActivity
     * @Applink : tokopedia-android-internal://user/link-account-webview
     * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_LD]
     **/
    @Deprecated("Remove this class after integrating SCP Login to Tokopedia")
    const val LINK_ACCOUNT_WEBVIEW = "$NEW_INTERNAL_USER/link-account-webview"

    /**
     * LinkAccountActivity
     * @Applink : tokopedia-android-internal://user/link-account
     **/
    @Deprecated("Remove this class after integrating SCP Login to Tokopedia")
    const val LINK_ACCOUNT = "$NEW_INTERNAL_USER/link-account"

    /**
     * AddNameRegisterPhoneActivity
     * @Applink : tokopedia-android-internal://user/add-name-register
     **/
    const val ADD_NAME_REGISTER = "$NEW_INTERNAL_USER/add-name-register"

    /**
     * ChangeGenderActivity
     * @Applink : tokopedia-android-internal://user/change-gender
     **/
    const val CHANGE_GENDER = "$NEW_INTERNAL_USER/change-gender"

    /**
     * AddEmailActivity
     * @Applink : tokopedia-android-internal://user/add-email
     **/
    const val ADD_EMAIL = "$NEW_INTERNAL_USER/add-email"

    /**
     * AddPhoneActivity
     * @Applink : tokopedia-android-internal://user/add-phone
     **/
    const val ADD_PHONE = "$NEW_INTERNAL_USER/add-phone"

    /**
     * NewAddPhoneActivity
     * @Applink : tokopedia-android-internal://user/new-add-phone
     **/
    const val NEW_ADD_PHONE = "$NEW_INTERNAL_USER/new-add-phone"

    /**
     * AddPhoneActivity
     * @Applink : tokopedia-android-internal://user/add-phone?phone={phone-number}
     **/
    const val ADD_PHONE_WITH = "$NEW_INTERNAL_USER/add-phone?phone={phone-number}"

    /**
     * AddBodActivity
     * @Applink : tokopedia-android-internal://user/add-bod
     * @param : [PARAM_BOD_TITLE]
     * @param : [PARAM_BOD]
     **/
    const val ADD_BOD = "$NEW_INTERNAL_USER/add-bod"
    const val PARAM_BOD_TITLE = "bodTitle"
    const val PARAM_BOD = "bod"

    /**
     * AddPinActivity
     * @Applink : tokopedia-android-internal://user/add-pin
     **/
    const val ADD_PIN = "$NEW_INTERNAL_USER/add-pin"

    /**
     * AddPinFrom2FAActivity
     * @Applink : tokopedia-android-internal://user/add-pin-from-2fa
     **/
    const val ADD_PIN_FROM_2FA = "$NEW_INTERNAL_USER/add-pin-from-2fa"

    /**
     * ChangePinActivity
     * @Applink : tokopedia-android-internal://user/change-pin
     **/
    const val CHANGE_PIN = "$NEW_INTERNAL_USER/change-pin"

    /**
     * ChangeNameActivity
     * @Applink : tokopedia-android-internal://user/change-name
     *
     **/
    const val CHANGE_NAME = "$NEW_INTERNAL_USER/change-name?oldName={oldName}&chances={chances}"
    const val PARAM_FULL_NAME = "oldName"
    const val PARAM_CHANCE_CHANGE_NAME = "chances"

    /**
     * ProfileCompletionActivity
     * @Applink : tokopedia-android-internal://user/profile-completion
     **/
    const val PROFILE_COMPLETION = "$NEW_INTERNAL_USER/profile-completion"

    /**
     * AddNameActivity
     * @Applink : tokopedia-android-internal://user/manage-name
     **/
    const val MANAGE_NAME = "$NEW_INTERNAL_USER/manage-name"

    /**
     * HasPasswordActivity
     * @Applink : tokopedia-android-internal://user/has-password
     **/
    const val HAS_PASSWORD = "$NEW_INTERNAL_USER/has-password"

    /**
     * AddPasswordActivity
     * @Applink : tokopedia-android-internal://user/add-password
     **/
    const val ADD_PASSWORD = "$NEW_INTERNAL_USER/add-password"

    /**
     * ForgotPasswordActivity
     * @applink : tokopedia-android-internal://user/forgot-password
     * @param
     * required : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_EMAIL]
     * optional : [PARAM_AUTO_RESET]
     * optional : [PARAM_REMOVE_FOOTER]
     **/
    const val FORGOT_PASSWORD = "$NEW_INTERNAL_USER/forgot-password"
    const val PARAM_AUTO_RESET = "auto_reset"
    const val PARAM_REMOVE_FOOTER = "remove_footer"

    /**
     * com.tokopedia.loginregister.redefineregisteremail.view.activity.RedefineRegisterEmailActivity
     * @Applink : tokopedia-android-internal://user/redefine-register-email
     * @param
     * Required : [ApplinkConstInternalGlobal.PARAM_SOURCE] (type: String)
     * Required : [PARAM_IS_REGISTER_REQUIRED_INPUT_PHONE] (type: Boolean)
     **/
    const val REDEFINE_REGISTER_EMAIL = "$NEW_INTERNAL_USER/redefine-register-email"
    const val PARAM_IS_REGISTER_REQUIRED_INPUT_PHONE = "register_required_input_phone"

    /**
     * ConsentWithdrawalActivity
     * @Applink : tokopedia-android-internal://user/consent/withdrawal?groupId={groupId}
     * @param : [GROUP_ID] data type Int
     * */
    const val GROUP_ID = "groupId"
    const val CONSENT_WITHDRAWAL = "$NEW_INTERNAL_USER/consent/withdrawal?$GROUP_ID={groupId}"
    const val CONSENT_WITHDRAWAL_NEW = "$NEW_INTERNAL_USER/consent/withdrawal/new?$GROUP_ID={groupId}"

    /**
     * com.tokopedia.troubleshooter.notification.ui.activity.TroubleshootActivity
     * @Applink : tokopedia-android-internal://user/push-notification-troubleshooter
     **/
    const val PUSH_NOTIFICATION_TROUBLESHOOTER = "$NEW_INTERNAL_USER/push-notification-troubleshooter"

    /**
     * com.tokopedia.telephony_masking.view.TelephonyActivity
     * @Applink : tokopedia-android-internal://user/telephony-masking
     **/
    const val TELEPHONY_MASKING = "$NEW_INTERNAL_USER/telephony-masking"

    /**
     * This param is used to identify the source page
     **/
    const val PARAM_SOURCE = "source"

    /**
     * ## KYC Param | projectId
     * This param is used to identify the source
     **/
    const val PARAM_PROJECT_ID = "projectId"

    /**
     * ## KYC Param | showIntro
     * This param is used for show intro & thank you page (wrapper)
     **/
    const val PARAM_SHOW_INTRO = "showIntro"

    /**
     * ## KYC Param | redirectUrl
     * This param is used to redirect webview after user success KYC
     **/
    const val PARAM_REDIRECT_URL = "redirectUrl"

    /**
     * ## KYC Param | callBack
     * This param is used to add applink callback when user is verified
     **/
    const val PARAM_CALL_BACK = "callBack"

    /**
     * ## GoTo KYC
     * ### Open GoTo KYC
     *
     * @class       : GotoKycTransparentActivity
     * @Applink     : tokopedia-android-internal://user/goto-kyc?projectId={projectId}&source={source}
     * @param
     *  - projectId    : required | String | ref: [PARAM_PROJECT_ID]
     *  - source       : required only for BU | String | ref: [PARAM_SOURCE]
     **/

    const val GOTO_KYC = "$NEW_INTERNAL_USER/goto-kyc?" +
        "$PARAM_PROJECT_ID={$PARAM_PROJECT_ID}&" +
        "$PARAM_SOURCE={$PARAM_SOURCE}&" +
        "$PARAM_CALL_BACK={$PARAM_CALL_BACK}"

    /**
     * ## KYC Param | type
     * This param used for specify the flow of kyc
     * @value:
     *  - ktpOnly = [KYC_TYPE_KTP_ONLY] | not ready yet
     *  - selfieOnly = [KYC_TYPE_SELFIE_ONLY]  | not ready yet
     *  - livenessOnly = [KYC_TYPE_LIVENESS_ONLY]  | not ready yet
     *  - ktpWithSelfie = [KYC_TYPE_KTP_WITH_SELFIE]
     *  - ktpWithLiveness = [KYC_TYPE_KTP_WITH_LIVENESS]  | not ready yet, but this is default flow
     **/
    const val PARAM_KYC_TYPE = "type"

    /** Param value for [PARAM_KYC_TYPE] **/
    const val KYC_TYPE_KTP_ONLY = "ktpOnly"
    const val KYC_TYPE_SELFIE_ONLY = "selfieOnly"
    const val KYC_TYPE_LIVENESS_ONLY = "livenessOnly"
    const val KYC_TYPE_KTP_WITH_SELFIE = "ktpWithSelfie"
    const val KYC_TYPE_KTP_WITH_LIVENESS = "ktpWithLiveness"

    /**
     * KYC Internal Base Applink
     */
    private const val KYC_BASE_APPLINK = "$NEW_INTERNAL_USER/user-identification"
    private const val KYC_ONLY_BASE = "$KYC_BASE_APPLINK-only"

    /**
     * ## KYC - Info
     * ### Open KYC Info page
     *
     * @class : UserIdentificationInfoActivity
     * @Applink : tokopedia-android-internal://user/user-identification-info?projectId={projectId}
     * @param
     *  - projectId    : required | String | ref: [PARAM_PROJECT_ID]
     **/
    internal const val KYC_INFO_BASE = "$KYC_BASE_APPLINK-info"
    const val KYC_INFO = "$KYC_INFO_BASE?$PARAM_PROJECT_ID={$PARAM_PROJECT_ID}"

    /**
     * ## KYC - Form
     * ### Open KYC submission form
     *
     * @class : UserIdentificationFormActivity
     * @Applink : tokopedia-android-internal://user/user-identification-form?projectId={projectId}&redirectUrl={redirectUrl}
     * @param
     *  - projectId    : required | String | ref: [PARAM_PROJECT_ID]
     *  - redirectUrl  : optional | String | ref: [PARAM_REDIRECT_URL]
     **/
    internal const val KYC_FORM_BASE = "$KYC_BASE_APPLINK-form"
    const val KYC_FORM = "$KYC_FORM_BASE?" +
        "$PARAM_PROJECT_ID={$PARAM_PROJECT_ID}&" +
        "$PARAM_REDIRECT_URL={$PARAM_REDIRECT_URL}"

    /**
     * ## KYC - Ala carte
     * ### Open KYC Ala Carte features depend on param
     *
     * @class : UserIdentificationFormActivity
     * @Applink : tokopedia-android-internal://user/user-identification-only?projectId={projectId}&showIntro={showIntro}&redirectUrl={redirectUrl}
     * @param
     *  - projectId    : required | String | ref: [PARAM_PROJECT_ID]
     *  - showIntro    : required | Boolean | false | ref: [PARAM_SHOW_INTRO]
     *  - redirectUrl  : optional | String | ref: [PARAM_REDIRECT_URL]
     *  - type         : optional | String | ref: [PARAM_KYC_TYPE]
     **/
    const val KYC_ALA_CARTE = "$KYC_ONLY_BASE?" +
        "$PARAM_PROJECT_ID={$PARAM_PROJECT_ID}&" +
        "$PARAM_SHOW_INTRO={$PARAM_SHOW_INTRO}&" +
        "$PARAM_REDIRECT_URL={$PARAM_REDIRECT_URL}&" +
        "$PARAM_KYC_TYPE={$PARAM_KYC_TYPE}"

    /**
     * ## KYC - Liveness
     * ### Open Liveness page directly
     *
     * @class : LivenessActivity
     * @Applink : tokopedia-android-internal://user/liveness-detection?projectId={projectId}
     * @param
     *  - projectId    : required | String | ref: [PARAM_PROJECT_ID]
     **/
    const val KYC_LIVENESS_BASE = "$NEW_INTERNAL_USER/liveness-detection"
    const val KYC_LIVENESS = "$KYC_LIVENESS_BASE?$PARAM_PROJECT_ID={$PARAM_PROJECT_ID}"

    /**
     * com.tokopedia.privacyaccount.PrivacyCenterActivity
     * @Applink : tokopedia-android-internal://user/privacy-center
     **/
    const val PRIVACY_CENTER = "$NEW_INTERNAL_USER/privacy-center"

    /**
     * com.tokopedia.privacyaccount.accountlinking.LinkAccountWebViewActivity
     * @Applink : tokopedia-android-internal://user/account-linking-webview
     * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_LD]
     **/
    @Deprecated("Remove this class after integrating SCP Login to Tokopedia")
    const val ACCOUNT_LINKING_WEBVIEW = "$NEW_INTERNAL_USER/account-linking-webview"

    /**
     * com.tokopedia.privacycenter.dsar.ui.DsarActivity
     * @Applink : tokopedia-android-internal://user/dsar
     **/
    const val DSAR = "$NEW_INTERNAL_USER/dsar"

    /**
     * com.tokopedia.privacycenter.dsar.ui.DsarAddEmailActivity
     * @Applink : tokopedia-android-internal://user/dsar/add-email
     **/
    const val DSAR_ADD_EMAIL = "$NEW_INTERNAL_USER/dsar/add-email"

    /**
     * com.tokopedia.privacyaccount.searchhistory.SearchHistoryActivity
     * @Applink : tokopedia-android-internal://user/search-history
     **/
    const val SEARCH_HISTORY = "$NEW_INTERNAL_USER/search-history"

    /**
     * # Sharing wishlist
     *
     * @class : SharingWishlistActivity
     * @Applink : tokopedia-android-internal://user/sharing-wishlist?tab={tab}
     * @param
     *  - tab       : optional | string | ref: [PARAM_TAB]
     */
    const val PARAM_TAB = "tab"

    /** param value for [PARAM_TAB] */
    const val TAB_PUBLIC = "public"
    const val TAB_PRIVATE = "private"

    const val SHARING_WISHLIST = "$NEW_INTERNAL_USER/sharing-wishlist?tab={$PARAM_TAB}"

    fun getGotoKYCApplink(projectId: String, source: String, callback: String = ""): String {
       return  UriUtil.buildUri(GOTO_KYC, projectId, source, callback)
    }
}

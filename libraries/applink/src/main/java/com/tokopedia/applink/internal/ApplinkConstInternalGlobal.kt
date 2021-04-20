package com.tokopedia.applink.internal

import com.tokopedia.applink.ApplinkConst.DFFALLBACKURL_KEY
import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://global".
 */
object ApplinkConstInternalGlobal {

    const val HOST_GLOBAL = "global"
    const val INTERNAL_GLOBAL = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_GLOBAL}"

    //Extras
    const val PARAM_UUID = "uuid"
    const val PARAM_MSISDN = "msisdn"
    const val PARAM_PHONE = "phone"
    const val PARAM_BOD = "bod"
    const val PARAM_BOD_TITLE = "bodTitle"
    const val PARAM_EMAIL = "email"
    const val PARAM_SOURCE = "source"
    const val PARAM_NAME = "name"
    const val PARAM_TOKEN = "token"
    const val PARAM_ACTION = "action"
    const val PARAM_IS_SMART_LOGIN = "isSmartLogin"
    const val PARAM_IS_PENDING = "isPending"
    const val PARAM_LOGIN_TYPE = "loginType"
    const val PARAM_IS_SQ_CHECK = "isSqCheck"
    const val PARAM_KTP_PATH = "ktpPath"
    const val PARAM_FACE_PATH = "facePath"
    const val PARAM_CTA_TYPE = "ctaType"
    const val PARAM_IMG_LINK = "imglink"
    const val PARAM_MESSAGE_TITLE = "messageTitle"
    const val PARAM_MESSAGE_BODY = "messageBody"
    const val PARAM_STATUS = "status"
    const val PARAM_IS_RESET_PIN = "isResetPin"
    const val PARAM_IS_FROM_REGISTER = "isFromRegister"
    const val PARAM_IS_FACEBOOK = "isFacebook"
    const val PARAM_NEW_HOME_ACCOUNT = "fromNewAccount"
    const val PARAM_USER_ID_ENC = "userIdEncrypted"
    const val PARAM_USER_ACCESS_TOKEN = "accessToken"
    const val PARAM_USER_ID = "userId"
    const val PARAM_CAN_USE_OTHER_METHOD = "can_use_other_method"
    const val PARAM_IS_SHOW_CHOOSE_METHOD = "is_show_choose_method"
    const val PARAM_OTP_TYPE = "otp_type"
    const val PARAM_REQUEST_OTP_MODE = "request_otp_mode"
    const val PARAM_OTP_CODE = "otp_code"
    const val PARAM_IS_SKIP_OTP = "is_skip_otp"
    const val PARAM_ENABLE_2FA = "enable_2fa"
    const val PARAM_ENABLE_SKIP_2FA = "enable_skip_2fa"
    const val PARAM_IS_LOGIN_REGISTER_FLOW = "isLoginRegisterFlow"
    const val PARAM_IS_SUCCESS_REGISTER = "isSuccessRegister"

    // WithdrawActivity
    // tokopedia-android-internal://global/withdraw
    @JvmField
    val WITHDRAW = "$INTERNAL_GLOBAL/withdraw"
    // WithdrawActivity
    // tokopedia-android-internal://global/withdraw
    @JvmField
    val AUTO_WITHDRAW_SETTING = "$INTERNAL_GLOBAL/autoWithdrawSettings"

    // InactivePhoneOnboardingActivity
    // tokopedia-android-internal://global/change-inactive-phone
    @JvmField
    val CHANGE_INACTIVE_PHONE = "$INTERNAL_GLOBAL/change-inactive-phone"

    // TkpdPaySettingActivity
    // tokopedia-android-internal://global/payment-setting
    @JvmField
    val PAYMENT_SETTING = "$INTERNAL_GLOBAL/payment-setting"

    //ChooseTokocashAccountActivity
    // tokopedia-android-internal://global/choose-account
    @JvmField
    val CHOOSE_ACCOUNT = "$INTERNAL_GLOBAL/choose-account"

    // AddNameRegisterActivity
    // tokopedia-android-internal://global/add-name-register
    @JvmField
    val ADD_NAME_REGISTER = "$INTERNAL_GLOBAL/add-name-register"
    @JvmField
    val ADD_NAME_REGISTER_CLEAN_VIEW = "$INTERNAL_GLOBAL/add-name-register/clean-view"

    /**
     * ForgotPasswordAcitivity
     * @applink : tokopedia-android-internal://global/forgot-password
     * @param
     * required : [PARAM_EMAIL]
     * optional : [PARAM_AUTO_RESET]
     * optional : [PARAM_REMOVE_FOOTER]
     **/
    @JvmField
    val FORGOT_PASSWORD = "$INTERNAL_GLOBAL/forgot-password"
    @JvmField
    val PARAM_AUTO_RESET = "auto_reset"
    val PARAM_REMOVE_FOOTER = "remove_footer"

    // AddPasswordActivity
    // tokopedia-android-internal://global/add-password
    @JvmField
    val ADD_PASSWORD = "$INTERNAL_GLOBAL/add-password"

    // ProfileCompletionActivity
    // tokopedia-android-internal://global/profile-completion
    @JvmField
    val PROFILE_COMPLETION = "$INTERNAL_GLOBAL/profile-completion"

    // SettingProfileActivity
    // tokopedia-android-internal://global/setting-profile
    @JvmField
    val SETTING_PROFILE = "$INTERNAL_GLOBAL/setting-profile"

    // LandingShopCreationActivity
    // tokopedia-android-internal://global/landing-shop-creation
    @JvmField
    val LANDING_SHOP_CREATION = "$INTERNAL_GLOBAL/landing-shop-creation"

    // PhoneShopCreationActivity
    // tokopedia-android-internal://global/phone-shop-creation
    @JvmField
    val PHONE_SHOP_CREATION = "$INTERNAL_GLOBAL/phone-shop-creation"

    // NameShopCreationActivity
    // tokopedia-android-internal://global/name-shop-creation
    @JvmField
    val NAME_SHOP_CREATION = "$INTERNAL_GLOBAL/name-shop-creation"

    @JvmField
    val LIVENESS_DETECTION = "$INTERNAL_GLOBAL/liveness-detection"

    @JvmField
    val USER_IDENTIFICATION_INFO_BASE = "$INTERNAL_GLOBAL/user-identification-info"

    // UserIdentificationInfoActivity
    // tokopedia-android-internal://global/user-identification-info
    @JvmField
    val USER_IDENTIFICATION_INFO = "$USER_IDENTIFICATION_INFO_BASE?projectId={projectId}"

    // AddNameActivity
    // tokopedia-android-internal://global/manage-name
    @JvmField
    val MANAGE_NAME = "$INTERNAL_GLOBAL/manage-name"

    @JvmField
    val PARAM_SOURCE_KYC_SELLER = "seller"

    @JvmField
    val USER_IDENTIFICATION_FORM_BASE = "$INTERNAL_GLOBAL/user-identification-form"

    // UserIdentificationFormActivity
    // tokopedia-android-internal://global/user-identification-form
    @JvmField
    val USER_IDENTIFICATION_FORM = "$USER_IDENTIFICATION_FORM_BASE?projectId={projectId}"
    @JvmField
    val PARAM_PROJECT_ID = "projectId"

    @JvmField
    val PARAM_CALL_BACK = "callBack"

    // InboxTalkActivity
    // tokopedia-android-internal://global/inbox-talk
    @JvmField
    val INBOX_TALK = "$INTERNAL_GLOBAL/inbox-talk"

    // TalkProductActivity
    // tokopedia-android-internal://global/product-talk
    @JvmField
    val SHOP_TALK_BASE = "$INTERNAL_GLOBAL/shop-talk/"
    @JvmField
    val SHOP_TALK = "$SHOP_TALK_BASE{shop_id}/"
    @JvmField
    val PARAM_SHOP_ID = "shop_id"

    // TalkReadingActivity
    // tokopedia-android-internal://talk/product-talk
    @JvmField
    val PRODUCT_TALK_BASE = "$INTERNAL_GLOBAL/product-talk/"
    @JvmField
    val PRODUCT_TALK = "$PRODUCT_TALK_BASE{product_id}/"
    @JvmField
    val PARAM_PRODUCT_ID = "product_id"

    // TalkReplyActivity
    // tokopedia-android-internal://talk/reply-talk
    @JvmField
    val TALK_REPLY_BASE = "$INTERNAL_GLOBAL/reply-talk/"
    @JvmField
    val TALK_REPLY = "$TALK_REPLY_BASE{question_id}/"

    // TalkDetailsActivity
    // tokopedia-android-internal://global/detail-talk
    @JvmField
    val DETAIL_TALK_BASE = "$INTERNAL_GLOBAL/detail-talk/"
    @JvmField
    val DETAIL_TALK = "$DETAIL_TALK_BASE{talk_id}/?" +
            "shop_id={shop_id}&" +
            "comment_id={comment_id}&" +
            "source={source}"

    // AddTalkActivity
    // tokopedia-android-internal://global/add-talk
    @JvmField
    val ADD_TALK = "$INTERNAL_GLOBAL/add-talk"

    @JvmField
    val TALK_SELLER_SETTINGS = "$INTERNAL_GLOBAL/talk-seller-settings/"

    // ChangeGenderActivity
    // tokopedia-android-internal://global/change-gender
    @JvmField
    val CHANGE_GENDER = "$INTERNAL_GLOBAL/change-gender"

    // ChangeNameActivity
    // tokopedia-android-internal://global/change-name
    @JvmField
    val CHANGE_NAME = "$INTERNAL_GLOBAL/change-name?oldName={oldName}&chances={chances}"
    @JvmField
    val PARAM_FULL_NAME = "oldName"
    @JvmField
    val PARAM_CHANCE_CHANGE_NAME = "chances"

    // AddEmailActivity
    // tokopedia-android-internal://global/add-email
    @JvmField
    val ADD_EMAIL = "$INTERNAL_GLOBAL/add-email"

    // AddPhoneActivity
    // tokopedia-android-internal://global/add-phone
    @JvmField
    val ADD_PHONE = "$INTERNAL_GLOBAL/add-phone"

    // AddPhoneActivity
    // tokopedia-android-internal://global/add-phone
    @JvmField
    val ADD_PHONE_WITH = "$INTERNAL_GLOBAL/add-phone?phone={phone-number}"

    // AddBodActivity
    // tokopedia-android-internal://global/add-bod
    @JvmField
    val ADD_BOD = "$INTERNAL_GLOBAL/add-bod"

    // AddPinActivity
    // tokopedia-android-internal://global/add-pin
    @JvmField
    val ADD_PIN = "$INTERNAL_GLOBAL/add-pin"

    // AddPinActivity
    // tokopedia-android-internal://global/add-pin-from-2fa
    @JvmField
    val ADD_PIN_FROM_2FA = "$INTERNAL_GLOBAL/add-pin-from-2fa"

    // PinOnboardingActivity
    // tokopedia-android-internal://global/add-pin-onboarding
    @JvmField
    val ADD_PIN_ONBOARDING = "$INTERNAL_GLOBAL/add-pin-onboarding"

    // RegisterFingerprintOnboardingActivity
    // tokopedia-android-internal://global/add-fingerprint-onboarding
    @JvmField
    val ADD_FINGERPRINT_ONBOARDING = "$INTERNAL_GLOBAL/add-fingerprint-onboarding"

    // PinCompleteActivity
    // tokopedia-android-internal://global/add-pin-complete
    @JvmField
    val ADD_PIN_COMPLETE = "$INTERNAL_GLOBAL/add-pin-complete"

    // VerificationActivity
    // tokopedia-android-internal://global/cotp
    @JvmField
    val COTP = "$INTERNAL_GLOBAL/cotp"

    // ReceiverNotifActivity
    // tokopedia-android-internal://global/otp-push-notif-receiver
    @JvmField
    val OTP_PUSH_NOTIF_RECEIVER = "$INTERNAL_GLOBAL/otp-push-notif-receiver"

    // SettingNotifActivity
    // tokopedia-android-internal://global/otp-push-notif-setting
    @JvmField
    val OTP_PUSH_NOTIF_SETTING = "$INTERNAL_GLOBAL/otp-push-notif-setting"

    // ChangePhoneNumberWarningActivity
    // tokopedia-android-internal://global/change-phone-number
    @JvmField
    val CHANGE_PHONE_NUMBER = "$INTERNAL_GLOBAL/change-phone-number"

    // ChangePasswordActivity
    // tokopedia-android-internal://global/change-password
    @JvmField
    val CHANGE_PASSWORD = "$INTERNAL_GLOBAL/change-password"

    // HasPasswordActivity
    // tokopedia-android-internal://global/has-password
    @JvmField
    val HAS_PASSWORD = "$INTERNAL_GLOBAL/has-password"

    // ChangePinActivity
    // tokopedia-android-internal://global/change-pin
    @JvmField
    val CHANGE_PIN = "$INTERNAL_GLOBAL/change-pin"

    // WebViewActivity (Web View in library)
    // Solution for sellerapp that does not have AppLinkWebsiteActivity
    // Activity can have title by putting "title=.."
    @JvmField
    val WEBVIEW = "$INTERNAL_GLOBAL/webview?url={url}"

    @JvmField
    val IMAGE_PICKER = "$INTERNAL_GLOBAL/image-picker"

    @JvmField
    val IMAGE_EDITOR = "$INTERNAL_GLOBAL/image-editor"

    @JvmField
    val WEBVIEW_TITLE = "$INTERNAL_GLOBAL/webview?title={title}&url={url}"

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

    // SaldoDepositActivity
    // tokopedia-android-internal://global/saldo
    @JvmField
    val SALDO_DEPOSIT = "$INTERNAL_GLOBAL/saldo"

    // SaldoIntroActivity
    // tokopedia-android-internal://global/saldo-intro
    @JvmField
    val SALDO_INTRO = "$INTERNAL_GLOBAL/saldo-intro"

    // RegisterInitialActivity
    // tokopedia-android-internal://global/init-register
    @JvmField
    val INIT_REGISTER = "$INTERNAL_GLOBAL/init-register"

    // RegisterEmailActivity
    // tokopedia-android-internal://global/email-register
    @JvmField
    val EMAIL_REGISTER = "$INTERNAL_GLOBAL/email-register"
    // ChatbotActivity
    // tokopedia-android-internal://global/chatbot
    @JvmField
    val CHAT_BOT = "$INTERNAL_GLOBAL/chatbot"

    // PaymentQRSummaryActivity
    // tokopedia-android-internal://global/ovo-pay-with-qr
    @JvmField
    val OVO_PAY_WITH_QR_ENTRY = "$INTERNAL_GLOBAL/ovo-pay-with-qr"

    // QrOvoPayTxDetailActivity
    // tokopedia-android-internal://global/ovoqrthanks/
    @JvmField
    val OQR_PIN_URL_ENTRY = "$INTERNAL_GLOBAL/ovoqrthanks/"

    // QrOvoPayTxDetailActivity
    // tokopedia-android-internal://global/ovoqrthanks/{transfer_id}
    @JvmField
    val OQR_PIN_URL_ENTRY_PATTERN = "$INTERNAL_GLOBAL/ovoqrthanks/{transfer_id}/"

    // InstantDebitBcaActivity
    // tokopedia-android-internal://global/instantdebitbca?callbackUrl={callbackUrl}
    @JvmField
    val INSTANT_DEBIT_BCA_ENTRY_PATTERN = "$INTERNAL_GLOBAL/instantdebitbca"

    // BcaEditLimitActivity
    // tokopedia-android-internal://global/editbcaoneklik?callbackUrl={callbackUrl}&xcoid={xcoid}
    @JvmField
    val EDIT_BCA_ONE_KLICK_ENTRY_PATTERN = "$INTERNAL_GLOBAL/editbcaoneklik"

    @JvmField
    val DISCOVERY = "$INTERNAL_GLOBAL/discovery"


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
        DFFALLBACKURL_KEY +"={fallbackUrl}"

    /**
     * LogoutActivity
     * @applink : tokopedia-android-internal://global/logout
     * @param   : [PARAM_IS_RETURN_HOME]
     * default is 'true', set 'false' if you wan get activity result
     **/
    @JvmField
    val LOGOUT = "$INTERNAL_GLOBAL/logout"
    /** for param logout */
    const val PARAM_IS_RETURN_HOME = "return_to_home"
    const val PARAM_IS_CLEAR_DATA_ONLY = "is_clear_data_only"

    /**
     * TermPrivacyActivity
     * @applink : tokopedia-android-internal://global/term-privacy/{page}/
     **/
    @JvmField
    val TERM_PRIVACY = "$INTERNAL_GLOBAL/term-privacy/{page}/"
    /** for param term privacy */
    const val PAGE_TERM_AND_CONDITION = "term-condition"
    const val PAGE_PRIVACY_POLICY = "privacy-policy"

    // AdvancedSettingActivity
    // tokopedia-android-internal://global/advanced-setting
    @JvmField
    val ADVANCED_SETTING = "$INTERNAL_GLOBAL/advanced-setting"

    // AccountSettingActivity
    // tokopedia-android-internal://global/account-setting
    @JvmField
    val ACCOUNT_SETTING = "$INTERNAL_GLOBAL/account-setting"

    // GeneralSettingActivity
    // tokopedia-android-internal://global/general-setting
    @JvmField
    val GENERAL_SETTING = "$INTERNAL_GLOBAL/general-setting"

    // PushNotificationCheckerActivity
    // tokopedia-android-internal://global/push-notification-troubleshooter
    @JvmField
    val PUSH_NOTIFICATION_TROUBLESHOOTER = "$INTERNAL_GLOBAL/push-notification-troubleshooter"

    /**
     * Go to chat list
     */
    @JvmField
    val TOPCHAT = "$INTERNAL_GLOBAL/topchat"

    /**
     * Go to chatroom with the provided {message_id}
     * If you want to use {shopId} to chatroom use external applink
     */
    val TOPCHAT_ROOM = "$INTERNAL_GLOBAL/topchat/{message_id}"



    //ReferralPhoneNumberVerificationActivity
    // tokopedia-android-internal://global/setting-referral-phone-verification
    @JvmField
    val SETTING_REFERRAL_PHONE_VERIFICATION = "$INTERNAL_GLOBAL/setting-referral-phone-verification"

    @JvmField
    val REFERRAL_WELCOME_FRIENDS = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://referral/{code}/{owner}"

    // CrackTokenActivity
    // tokopedia-android-internal://global/gamification
    @JvmField
    val GAMIFICATION = "$INTERNAL_GLOBAL/gamification"

    // TapTapTokenActivity
    // tokopedia-android-internal://global/gamification2
    @JvmField
    val GAMIFICATION_TAP_TAP_MANTAP = "$INTERNAL_GLOBAL/gamification2"
    val GAMIFICATION_DAILY_GIFT = "$INTERNAL_GLOBAL/gamification_gift_daily"
    val GAMIFICATION_TAP_TAP_GIFT = "$INTERNAL_GLOBAL/gamification_gift_60s"

    //ManageNotificationActivity
    @JvmField
    val MANAGE_NOTIFICATION = "$INTERNAL_GLOBAL/manage-notification"

    // ScreenRecorderActivity
    // tokopedia-android-internal://global/screen-recorder
    @JvmField
    val SCREEN_RECORDER = "$INTERNAL_GLOBAL/screen-recorder"

    // TwoFactorActivity
    // tokopedia-android-internal://global/two-factor-register
    @JvmField
    val TWO_FACTOR_REGISTER = "$INTERNAL_GLOBAL/two-factor-register"

    // AccountHomeActivity
    // tokopedia-android-internal://global/account-home-old
    @JvmField
    val OLD_HOME_ACCOUNT = "$INTERNAL_GLOBAL/old-home-account"

    // HomeAccountUserActivity
    // tokopedia-android-internal://global/new-home-account
    @JvmField
    val NEW_HOME_ACCOUNT = "$INTERNAL_GLOBAL/new-home-account"

    //SeamlessActivity
    @JvmField
    val SEAMLESS_LOGIN = "${INTERNAL_GLOBAL}/login-seamless"

    //Image Quality Setting Activity
    // tokopedia-android-internal://global/media-quality-setting
    @JvmField
    val MEDIA_QUALITY_SETTING = "${INTERNAL_GLOBAL}/media-quality-setting"

    //FeedbackPageActivity
    @JvmField
    val FEEDBACK_FORM = "$INTERNAL_GLOBAL/internal-feedback"

    // OvoAddNameActivity
    // tokopedia-android-internal://global/ovo-add-name
    @JvmField
    val OVO_ADD_NAME = "$INTERNAL_GLOBAL/ovo-add-name"

    // OvoFinalPageActivity
    // tokopedia-android-internal://global/ovo-final-page
    @JvmField
    val OVO_FINAL_PAGE = "$INTERNAL_GLOBAL/ovo-final-page"

    // OvoRegisterInitialActivity
    // tokopedia-android-internal://global/ovo-reg-init
    @JvmField
    val OVO_REG_INIT = "$INTERNAL_GLOBAL/ovo-reg-init"
}

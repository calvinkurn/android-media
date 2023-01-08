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
    const val PARAM_EMAIL = "email"
    const val PARAM_SOURCE = "source"
    const val PARAM_NAME = "name"
    const val PARAM_TOKEN = "token"
    const val PARAM_ACTION = "action"
    const val PARAM_IS_SMART_LOGIN = "isSmartLogin"
    const val PARAM_IS_SMART_REGISTER = "isSmartRegister"
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
    const val PARAM_LD = "redirectionApplink"
    const val PARAM_PROJECT_ID = "projectId"
    const val PARAM_REDIRECT_URL = "redirectUrl"

    // WithdrawActivity
    // tokopedia-android-internal://global/withdraw
    const val WITHDRAW = "$INTERNAL_GLOBAL/withdraw"
    // WithdrawActivity
    // tokopedia-android-internal://global/withdraw
    const val AUTO_WITHDRAW_SETTING = "$INTERNAL_GLOBAL/autoWithdrawSettings"


    // InboxTalkActivity
    // tokopedia-android-internal://global/inbox-talk
    const val INBOX_TALK = "$INTERNAL_GLOBAL/inbox-talk"

    // TalkProductActivity
    // tokopedia-android-internal://global/product-talk
    const val SHOP_TALK_BASE = "$INTERNAL_GLOBAL/shop-talk/"
    const val SHOP_TALK = "$SHOP_TALK_BASE{shop_id}/"
    const val PARAM_SHOP_ID = "shop_id"

    // TalkReadingActivity
    // tokopedia-android-internal://talk/product-talk
    const val PRODUCT_TALK_BASE = "$INTERNAL_GLOBAL/product-talk/"
    const val PRODUCT_TALK = "$PRODUCT_TALK_BASE{product_id}/"
    const val PARAM_PRODUCT_ID = "product_id"

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

    const val TALK_SELLER_SETTINGS = "$INTERNAL_GLOBAL/talk-seller-settings/"

    // ChangePhoneNumberWarningActivity
    // tokopedia-android-internal://global/change-phone-number
    const val CHANGE_PHONE_NUMBER = "$INTERNAL_GLOBAL/change-phone-number"

    // WebViewActivity (Web View in library)
    // Solution for sellerapp that does not have AppLinkWebsiteActivity
    // Activity can have title by putting "title=.."
    const val WEBVIEW_BASE = "$INTERNAL_GLOBAL/webview"
    const val BROWSER = "$INTERNAL_GLOBAL/browser"

    const val WEBVIEW = "$INTERNAL_GLOBAL/webview?url={url}"

    const val WEBVIEW_DOWNLOAD = "$INTERNAL_GLOBAL/webviewdownload"
    const val WEBVIEW_BACK_HOME = "$INTERNAL_GLOBAL/webviewbackhome"

    const val IMAGE_PICKER = "$INTERNAL_GLOBAL/image-picker"
    const val IMAGE_PICKER_V2 = "$INTERNAL_GLOBAL/image-picker/v2/"
    const val USER_PROFILE_LANDING = "$INTERNAL_GLOBAL/people/"
    const val USER_PROFILE_FOLLOWERS = "$INTERNAL_GLOBAL/people/followers/"

    const val IMAGE_EDITOR = "$INTERNAL_GLOBAL/image-editor"

    const val VIDEO_PICKER = "$INTERNAL_GLOBAL/video-picker"

    const val WEBVIEW_TITLE = "$INTERNAL_GLOBAL/webview?title={title}&url={url}"

    // PhoneVerificationProfileActivity
    // tokopedia-android-internal://global/setting-profile-phone-verification
    const val SETTING_PROFILE_PHONE_VERIFICATION = "$INTERNAL_GLOBAL/setting-profile-phone-verification"

    // SettingBankActivity
    // tokopedia-android-internal://global/setting-bank
    const val SETTING_BANK = "$INTERNAL_GLOBAL/setting-bank"

    // PhoneVerificationProfileActivity
    // tokopedia-android-internal://global/setting-bank
    const val ADD_BANK = "$INTERNAL_GLOBAL/add-bank"

    // SaldoDepositActivity
    // tokopedia-android-internal://global/saldo
    const val SALDO_DEPOSIT = "$INTERNAL_GLOBAL/saldo"

    // SaldoIntroActivity
    // tokopedia-android-internal://global/saldo-intro
    const val SALDO_INTRO = "$INTERNAL_GLOBAL/saldo-intro"

    // ChatbotActivity
    // tokopedia-android-internal://global/chatbot
    const val CHAT_BOT = "$INTERNAL_GLOBAL/chatbot"

    // PaymentQRSummaryActivity
    // tokopedia-android-internal://global/ovo-pay-with-qr
    const val OVO_PAY_WITH_QR_ENTRY = "$INTERNAL_GLOBAL/ovo-pay-with-qr"

    // QrOvoPayTxDetailActivity
    // tokopedia-android-internal://global/ovoqrthanks/
    const val OQR_PIN_URL_ENTRY = "$INTERNAL_GLOBAL/ovoqrthanks/"

    // QrOvoPayTxDetailActivity
    // tokopedia-android-internal://global/ovoqrthanks/{transfer_id}
    const val OQR_PIN_URL_ENTRY_PATTERN = "$INTERNAL_GLOBAL/ovoqrthanks/{transfer_id}/"

    const val DISCOVERY = "$INTERNAL_GLOBAL/discovery"


    const val PARAM_ACCOUNT_ID = "account_id"
    const val PARAM_ACCOUNT_NAME = "account_name"
    const val PARAM_ACCOUNT_NO = "account_number"
    const val PARAM_BANK_ID = "bank_id"
    const val PARAM_BANK_NAME = "bank_name"

    const val DYNAMIC_FEATURE_INSTALL_BASE= "$INTERNAL_GLOBAL/dynamic-features-install/"

    // DFInstallerActivity
    // tokopedia-android-internal://global/dynamic-features-install/hotel/?
    // auto = true will download when activity is open
    // applink, if provided will launch the applink after the module is installed
    // imageUrl, is the placeholder for the background
    const val DYNAMIC_FEATURE_INSTALL= DYNAMIC_FEATURE_INSTALL_BASE + "{module}/?" +
        "dfname={moduleTranslate}&" +
        "dfapplink={encodedApplink}&" +
        DFFALLBACKURL_KEY +"={fallbackUrl}"

    /** for param term privacy */
    const val PAGE_TERM_AND_CONDITION = "term-condition"
    const val PAGE_PRIVACY_POLICY = "privacy-policy"

    // AdvancedSettingActivity
    // tokopedia-android-internal://global/advanced-setting
    const val ADVANCED_SETTING = "$INTERNAL_GLOBAL/advanced-setting"

    // GeneralSettingActivity
    // tokopedia-android-internal://global/general-setting
    const val GENERAL_SETTING = "$INTERNAL_GLOBAL/general-setting"

    /**
     * Go to chat list
     */
    const val TOPCHAT = "$INTERNAL_GLOBAL/topchat"

    /**
     * Go to chatroom with the provided {message_id}
     * If you want to use {shopId} to chatroom use external applink
     */
    const val TOPCHAT_ROOM = "$INTERNAL_GLOBAL/topchat/{message_id}"


    //ReferralPhoneNumberVerificationActivity
    // tokopedia-android-internal://global/setting-referral-phone-verification
    const val SETTING_REFERRAL_PHONE_VERIFICATION = "$INTERNAL_GLOBAL/setting-referral-phone-verification"

    const val REFERRAL_WELCOME_FRIENDS = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://referral/{code}/{owner}"

    // CrackTokenActivity
    // tokopedia-android-internal://global/gamification
    const val GAMIFICATION = "$INTERNAL_GLOBAL/gamification"

    // TapTapTokenActivity
    // tokopedia-android-internal://global/gamification2
    const val GAMIFICATION_TAP_TAP_MANTAP = "$INTERNAL_GLOBAL/gamification2"
    const val GAMIFICATION_DAILY_GIFT = "$INTERNAL_GLOBAL/gamification_gift_daily"
    const val GAMIFICATION_TAP_TAP_GIFT = "$INTERNAL_GLOBAL/gamification_gift_60s"

    // ScreenRecorderActivity
    // tokopedia-android-internal://global/screen-recorder
    const val SCREEN_RECORDER = "$INTERNAL_GLOBAL/screen-recorder"

    // AccountHomeActivity
    // tokopedia-android-internal://global/account-home-old
    const val OLD_HOME_ACCOUNT = "$INTERNAL_GLOBAL/old-home-account"

    //SeamlessActivity
    const val SEAMLESS_LOGIN = "${INTERNAL_GLOBAL}/login-seamless"

    //FeedbackPageActivity
    const val FEEDBACK_FORM = "$INTERNAL_GLOBAL/internal-feedback"

    // GlobalSharingActivity
    // tokopedia-android-internal://global/global-sharing
    const val GLOBAL_SHARING = "$INTERNAL_GLOBAL/sharing?text={text}&image={image}&type={type}"

    //TkpdYoutubeVideoActivity
    const val YOUTUBE_VIDEO = "$INTERNAL_GLOBAL/youtube-video"

    //DataExplorerActivity
    const val DATA_EXPLORER = "$INTERNAL_GLOBAL/data-explorer"

    const val COMMISSION_BREAKDOWN = "$INTERNAL_GLOBAL/transaction-fee-download"
}

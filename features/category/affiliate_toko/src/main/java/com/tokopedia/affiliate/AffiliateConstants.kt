package com.tokopedia.affiliate

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.unifyprinciples.Typography

const val AFFILIATE_LOGIN_REQUEST_CODE = 1023
const val AFFILIATE_REGISTER_REQUEST_CODE = 1024
const val AFFILIATE_REQUEST_CODE_LOGOUT = 1025
const val LINK_HISTORY_BUTTON_CLICKED = 403
const val AFFILIATE_HELP_URL = "https://affiliate.tokopedia.com/help"
const val AFFILIATE_TANDC_URL = "https://www.tokopedia.com/help/article/syarat-ketentuan-tokopedia-affiliate"
const val AFFILIATE_FRAUD_URL = "https://www.tokopedia.com/help/article/cara-gabung-tokopedia-affiliate"
const val AFFILIATE_LIHAT_KATEGORI = "tokopedia://affiliate/help"
const val PAGE_ZERO = 0
const val PAGE_SEGMENT_HELP = "help"
const val KYC_DONE = 1

const val ANNOUNCEMENT__TYPE_NO_ANNOUNCEMENT = "noAnnouncement"
const val ANNOUNCEMENT__TYPE_CCA = "cca"
const val ANNOUNCEMENT__TYPE_SERVICE_STATUS = "serviceStatus"
const val ANNOUNCEMENT__TYPE_USER_BLACKLIST = "userBlacklisted"
const val ANNOUNCEMENT__TYPE_SUCCESS = 1
const val TRANSACTION_ID = "TransactionID"

val bodyTypoMap = mapOf(12 to Typography.BODY_1, 14 to Typography.BODY_2, 12 to Typography.BODY_3)
val headerTypoMap = mapOf(12 to Typography.HEADING_6, 14 to Typography.HEADING_5, 16 to Typography.HEADING_4,18 to Typography.HEADING_3,20 to Typography.HEADING_2)

const val ON_BOARDING_TUTORIAL_IMAGE_1 = "https://images.tokopedia.net/img/android/res/singleDpi/affiliate_onboaring_first_image.png"
const val ON_BOARDING_TUTORIAL_IMAGE_2 = "https://images.tokopedia.net/img/android/res/singleDpi/affiliate_onboaring_second_image.png"
const val ON_BOARDING_TUTORIAL_IMAGE_3 = "https://images.tokopedia.net/img/android/res/singleDpi/affiliate_onboaring_third_image.png"

const val AFFILIATE_SPLASH_TIME = 4000L

const val AFFILIATE_WALLET_TRANSACTION_ENABLE = "app_affiliate_wallet_transaction_enable"

const val AFFILIATE_TRX_ENABLED = "aff_trx_history"

const val AFFILIATE_WITHDRAWAL = "Aff_withdrawal"

const val DEFAULT_VALUE = false

const val PROJECT_ID = 20

const val WITHDRAWAL_APPLINK_STAGING = "tokopedia://webview?titlebar=false&url=https://affiliate-staging.tokopedia.com/portal/withdrawal"

const val WITHDRAWAL_APPLINK_PROD = "tokopedia://webview?titlebar=false&url=https://affiliate.tokopedia.com/portal/withdrawal"

const val APP_LINK_DESTINATION = "https://1002-staging-feature.tokopedia.com/portal/withdrawal?module=affiliate"

const val QUERY_CONST = "titlebar=false"

val uri = String.format("%s?%s?url=%s", ApplinkConst.WEBVIEW, QUERY_CONST, APP_LINK_DESTINATION)

const val APP_LINK_PARAMS_KYC = "projectId=$PROJECT_ID"

val APP_LINK_KYC = "${ApplinkConst.KYC_NO_PARAM}?$APP_LINK_PARAMS_KYC&${ApplinkConstInternalGlobal.PARAM_CALL_BACK}=$uri"

const val AFFILIATE_MICRO_SITE_LINK = "https://affiliate.tokopedia.com/"

const val AFFILIATE_INSTAGRAM_REGEX = "(?:(?:http|https):\\/\\/)?(www.)?(instagram.com|instagr.am|instagr.com)\\/[\\w-_@.]+"

const val AFFILIATE_YT_REGEX = "(?:(?:http|https):\\/\\/)?(www.)?(youtube\\.com)\\/((user|channel|c|id)\\/)?[\\w-_@.]+"

const val AFFILIATE_TIKTOK_REGEX = "(?:(?:http|https):\\/\\/)?((www|vt).)?(tiktok\\.com)\\/[\\w-_@.]+"

const val AFFILIATE_TWITTER_REGEX = "(?:(?:http|https):\\/\\/)?(www.)?(twitter.com)\\/[\\w-_@.]+"

const val COACHMARK_TAG = "affiliate_coachmark_onboarding"

const val TRANSACTION_TYPE_DEPOSIT = "TRANSACTION_TYPE_DEPOSIT"

const val TRANSACTION_TYPE_WITHDRAWAL = "TRANSACTION_TYPE_WITHDRAWAL"

const val TIME_ZONE = "Asia/Jakarta"

const val PATTERN = "dd MMM yyyy"

const val HOUR_PATTERN = "HH:00"

const val TWO = 2

const val THIRTY_THREE = 33

const val SIX = 6

const val TWENTY_NINE = 29
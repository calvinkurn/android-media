package com.tokopedia.affiliate

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.unifyprinciples.Typography
import java.util.Locale

const val AFFILIATE_LOGIN_REQUEST_CODE = 1023
const val AFFILIATE_REGISTER_REQUEST_CODE = 1024
const val AFFILIATE_REQUEST_CODE_LOGOUT = 1025
const val LINK_HISTORY_BUTTON_CLICKED = 403
const val AFFILIATE_HELP_URL_WEBVIEW =
    "tokopedia://webview?titlebar=true&url=https://affiliate.tokopedia.com/help?navigation=hide"
const val AFFILIATE_PRIVACY_POLICY_URL_WEBVIEW =
    "tokopedia://webview?titlebar=true&url=https://www.tokopedia.com/privacy?lang=id"
const val AFFILIATE_TANDC_URL =
    "https://www.tokopedia.com/help/article/syarat-ketentuan-tokopedia-affiliate"
const val AFFILIATE_LIHAT_KATEGORI = "tokopedia://affiliate/help"
const val AFFILIATE_APP_LINK = "tokopedia://affiliate"
const val PAGE_ZERO = 0
const val PAGE_SEGMENT_HELP = "help"
const val PAGE_SEGMENT_TRANSACTION_HISTORY = "transaction-history"
const val PAGE_SEGMENT_SSA_SHOP_LIST = "shoplist-dipromosikan-affiliate"
const val PAGE_SEGMENT_EDU_PAGE = "edu-page"
const val PAGE_SEGMENT_ONBOARDING = "onboarding"
const val PAGE_SEGMENT_DISCO_PAGE_LIST = "discopage-list"
const val PAGE_SEGMENT_PROMO_PAGE = "promosikan"
const val PAGE_SEGMENT_PERFORMA = "performa"
const val KYC_DONE = 1

const val TRANSACTION_ID = "TransactionID"
const val SMALL = 12
const val MEDIUM = 14
const val EXTRA_MEDIUM = 16
const val LARGE = 18
const val EXTRA_LARGER = 20
val bodyTypoMap = mapOf(
    EXTRA_MEDIUM to Typography.DISPLAY_1,
    MEDIUM to Typography.DISPLAY_2,
    SMALL to Typography.DISPLAY_3
)
val headerTypoMap = mapOf(
    SMALL to Typography.DISPLAY_3,
    MEDIUM to Typography.DISPLAY_2,
    EXTRA_MEDIUM to Typography.DISPLAY_1,
    LARGE to Typography.HEADING_3,
    EXTRA_LARGER to Typography.HEADING_2
)

const val ON_BOARDING_TUTORIAL_IMAGE_1 =
    "https://images.tokopedia.net/img/android/res/singleDpi/affiliate_onboaring_first_image.png"
const val ON_BOARDING_TUTORIAL_IMAGE_2 =
    "https://images.tokopedia.net/img/android/res/singleDpi/affiliate_onboaring_second_image.png"
const val ON_BOARDING_TUTORIAL_IMAGE_3 =
    "https://images.tokopedia.net/img/android/res/singleDpi/affiliate_onboaring_third_image.png"

const val AFFILIATE_SPLASH_TIME = 4000L
const val AFFILIATE_SCROLL_DELAY = 500L

const val PROJECT_ID = 20

const val WITHDRAWAL_APPLINK_STAGING =
    "tokopedia://webview?titlebar=false&url=https://affiliate-staging.tokopedia.com/portal/withdrawal"

const val WITHDRAWAL_APPLINK_PROD =
    "tokopedia://webview?titlebar=false&url=https://affiliate.tokopedia.com/portal/withdrawal"

const val APP_LINK_DESTINATION =
    "https://1002-staging-feature.tokopedia.com/portal/withdrawal?module=affiliate"

const val QUERY_CONST = "titlebar=false"

val uri = String.format(
    Locale.ENGLISH,
    "%s?%s?url=%s",
    ApplinkConst.WEBVIEW,
    QUERY_CONST,
    APP_LINK_DESTINATION
)

const val SOURCE_AFFILIATE = "Tokopedia Affiliate"

val APP_LINK_KYC =
    ApplinkConstInternalUserPlatform.getGotoKYCApplink(PROJECT_ID.toString(), SOURCE_AFFILIATE, uri)

const val AFFILIATE_MICRO_SITE_LINK = "https://affiliate.tokopedia.com/"

const val AFFILIATE_INSTAGRAM_REGEX =
    "(?:(?:http|https):\\/\\/)?(www.)?(instagram.com|instagr.am|instagr.com)\\/[\\w-_@.]+"

const val AFFILIATE_YT_REGEX =
    "(?:(?:http|https):\\/\\/)?(www.)?(youtube\\.com)\\/((user|channel|c|id)\\/)?[\\w-_@.]+"

const val AFFILIATE_TIKTOK_REGEX =
    "(?:(?:http|https):\\/\\/)?((www|vt).)?(tiktok\\.com)\\/[\\w-_@.]+"

const val AFFILIATE_TWITTER_REGEX = "(?:(?:http|https):\\/\\/)?(www.)?(twitter.com)\\/[\\w-_@.]+"

const val COACHMARK_TAG = "affiliate_coachmark_onboarding"

const val TRANSACTION_TYPE_DEPOSIT = "TRANSACTION_TYPE_DEPOSIT"

const val TRANSACTION_TYPE_WITHDRAWAL = "TRANSACTION_TYPE_WITHDRAWAL"

const val TIME_ZONE = "Asia/Jakarta"

const val PATTERN = "dd MMM yyyy"

const val HOUR_PATTERN = "HH:00"

const val SECOND_TAB = 1

const val TWO = 2

const val THIRD_TAB = 2

const val FOURTH_TAB = 3

const val FIRST_TAB = 0

const val THIRTY_THREE = 33

const val SIX = 6

const val TWENTY_NINE = 29

const val INSTAGRAM_DEFAULT = "https://www.instagram.com/"

const val TIKTOK_DEFAULT = "https://www.tiktok.com/"

const val FACEBOOK_DEFAULT = "https://www.faceboook.com/"

const val TWITTER_DEFAULT = "https://www.twitter.com/"

const val YOUTUBE_DEFAULT = "https://www.youtube.com/"

const val WWW = "www."

const val AVAILABLE = 1

const val ALMOST_OOS = 2

const val EMPTY_STOCK = 3

const val PRODUCT_INACTIVE = 4

const val SHOP_INACTIVE = 5

const val SHOP_CLOSED = 6

const val TRAFFIC_TYPE = "TRAFFIC"

const val PRODUCT_TYPE = "PRODUCT"

const val TYPE_DIVIDER = "divider"

const val YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'"

const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"

const val NEW_DATE_FORMAT = "dd MMM yyyy, HH:mm"

const val UTC = "UTC"

const val CLICK_TYPE = "totalClick"

const val COMMISSION_TYPE = "totalCommission"

const val SYSTEM_DOWN = "systemDown"

const val ON_REVIEWED = "onReviewed"

const val ON_REGISTERED = "onRegistered"

const val TIME_SIX = 6

const val TIME_TEN = 10

const val TIME_ELEVEN = 11

const val TIME_FIFTEEN = 15

const val TIME_SIXTEEN = 16

const val TIME_EIGHTEEN = 18

const val PAGE_LIMIT = 20

const val NO_UI_METRICS = 0

const val TOTAL_ITEMS_METRIC_TYPE = "totalItems"

const val PAGE_ANNOUNCEMENT_ALL = 0
const val PAGE_ANNOUNCEMENT_HOME = 1
const val PAGE_ANNOUNCEMENT_PROMO_PERFORMA = 2
const val PAGE_ANNOUNCEMENT_TRANSACTION_HISTORY = 3

const val AFFILIATE_SSA_SHOP = "aff_ssa_portal"
const val AFFILIATE_DISCO_PROMO = "aff_disco_link_gen"
const val AFFILIATE_TOKONOW_BANNER = "aff_now_att"
const val AFFILIATE_NC = "Affiliate_NC"
const val AFFILIATE_PROMOTE_HOME = "aff_promote_home"
const val AFFILIATE_PROMO_WEBVIEW = "aff_webview"

const val AFFILIATE_GAMIFICATION_VISIBILITY = "affil_banner_gami"
const val AFFILIATE_GAMIFICATION_REDIRECTION = "affil_gami_disco"
const val AFFILIATE_GAMIFICATION_REDIRECTION_APPLINK =
    "tokopedia://discovery/tantangan-hadiah-ramadan"

const val AFFILIATE_TOKONOW_DATA = "android_affiliate_tokonow_static_data"

const val PAGE_TYPE_PDP = "pdp"

const val PAGE_TYPE_SHOP = "shop"

const val PAGE_TYPE_CAMPAIGN = "campaign"

const val PAGE_EDUCATION_EVENT = "education_event"
const val PAGE_EDUCATION_ARTICLE = "education_article"
const val PAGE_EDUCATION_ARTICLE_TOPIC = "education_article_topic"
const val PAGE_EDUCATION_TUTORIAL = "education_tutorial"
const val EDUCATION_ARTICLE_DETAIL_STAGING_URL = "https://affiliate-staging.tokopedia.com/edu/"
const val EDUCATION_ARTICLE_DETAIL_PROD_URL = "https://affiliate.tokopedia.com/edu/"
const val FILTER_HIGHLIGHTED = "highlights"

const val FACEBOOK = "Facebook"
const val INSTAGRAM = "Instagram"
const val TELEGRAM = "Telegram"
const val YOUTUBE = "Youtube"

val SOCIAL_CHANNEL_FOLLOW_COUNT = mapOf(
    INSTAGRAM to "91k Followers",
    FACEBOOK to "1k Members",
    TELEGRAM to "11k Members",
    YOUTUBE to "1k Subscribers"
)
val SOCIAL_CHANNEL_HEADER = mapOf(
    INSTAGRAM to "https://images.tokopedia.net/img/IG.png",
    FACEBOOK to "https://images.tokopedia.net/img/FB.png",
    TELEGRAM to "https://images.tokopedia.net/img/TWT.png",
    YOUTUBE to "https://images.tokopedia.net/img/YT.png"
)
val SOCIAL_CHANNEL_LINK = mapOf(
    INSTAGRAM to "https://www.instagram.com/tokopediaffiliate",
    FACEBOOK to "https://www.facebook.com/groups/akademikreatortokopedia",
    TELEGRAM to "https://t.me/+shJRVBgfGXc1MzZl",
    YOUTUBE to "https://www.youtube.com/c/AkademiKreatorTokopedia"
)
const val AFFILIATE_SSE_URL_STAGING = "https://sse-staging.tokopedia.com/affiliate/sse/connect"
const val AFFILIATE_SSE_URL_PROD = "https://sse.tokopedia.com/affiliate/sse/connect"
const val PROMO_WEBVIEW_URL_STAGING =
    "https://affiliate-staging.tokopedia.com/browse"
const val PROMO_WEBVIEW_URL_PROD =
    "https://affiliate.tokopedia.com/browse?titlebar=false"

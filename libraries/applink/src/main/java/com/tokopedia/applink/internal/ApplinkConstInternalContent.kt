package com.tokopedia.applink.internal

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.url.TokopediaUrl

object ApplinkConstInternalContent {

    const val HOST_CONTENT = "content"
    const val HOST_PLAY = "play"
    private const val HOST_AFFILIATE = "affiliate"
    private const val HOST_FEED = "feed"
    private const val HOST_PLAY_BROADCASTER = "play-broadcaster"
    private const val HOST_PLAY_SHORTS = "play-shorts"

    const val INTERNAL_CONTENT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_CONTENT"
    const val INTERNAL_PLAY = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_PLAY"
    const val INTERNAL_FEED = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_FEED"
    const val INTERNAL_PLAY_BROADCASTER = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_PLAY_BROADCASTER"
    const val INTERNAL_PLAY_SHORTS = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_PLAY_SHORTS"
    const val INTERNAL_AFFILIATE = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_AFFILIATE"

    const val INTERNAL_AFFILIATE_CREATE_POST_V2 = "$INTERNAL_AFFILIATE/create_post_v2/"
    const val INTERNAL_PRODUCT_PICKER_FROM_SHOP = "$INTERNAL_CONTENT/productpickerfromshop/"

    const val INTERNAL_CONTENT_PRODUCT_TAG_AUTOCOMPLETE = "$INTERNAL_CONTENT/product_tag_autocomplete"
    const val INTERNAL_FEED_CREATION_PRODUCT_SEARCH = "$INTERNAL_CONTENT/creation_product_search"
    const val INTERNAL_FEED_CREATION_SHOP_SEARCH = "$INTERNAL_CONTENT/creation_shop_search"

    const val HASHTAG_PAGE = "$INTERNAL_CONTENT/explore-hashtag/{hashtag}/"

    const val CONTENT_REPORT = "$INTERNAL_CONTENT/content-report/{content_id}"
    const val VIDEO_DETAIL = "$INTERNAL_CONTENT/video-detail/{id}"
    const val COMMENT = "$INTERNAL_CONTENT/comment/{post_id}"

    const val SHOP_POST_PICKER = "$INTERNAL_CONTENT/content-shop/picker"

    const val PROFILE_DETAIL = "${DeeplinkConstant.SCHEME_INTERNAL}://people/{user_id}"
    const val PROFILE_SETTINGS = "${DeeplinkConstant.SCHEME_INTERNAL}://people/settings/{user_id}"

    const val HOST_FEED_HOME_NAVIGATION = "home/navigation"
    const val INTERNAL_FEED_HOME_NAVIGATION = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_FEED_HOME_NAVIGATION"
    const val PLAY_DETAIL = "$INTERNAL_PLAY/{channel_id}"

    const val INTERNAL_CONTENT_POST_DETAIL = "$INTERNAL_CONTENT/post-detail/"

    const val PLAY_PATH_LITE = "play/channel/"
    const val PLAY_LIVE = "play/live"
    const val FEED_VIDEO = "feed/video"

    const val TAB_POSITION_EXPLORE = 2
    const val TAB_POSITION_VIDEO = 3
    const val EXTRA_FEED_TAB_POSITION = "FEED_TAB_POSITION"
    const val ARGS_FEED_VIDEO_TAB_SELECT_CHIP = "tab"

    /**
     * Query
     */
    const val SOURCE_TYPE = "source_type"
    const val SOURCE_TYPE_HOME = "HOME"

    /**
     * Unified Feed
     */
    const val UF_TAB_POSITION_FOR_YOU = 0
    const val UF_TAB_POSITION_FOLLOWING = 1
    const val UF_EXTRA_FEED_SOURCE_ID = "ARGS_FEED_SOURCE_ID"
    const val UF_EXTRA_FEED_SOURCE_NAME = "ARGS_FEED_SOURCE_NAME"
    const val UF_EXTRA_FEED_TAB_NAME = "ARGS_FEED_TAB_NAME"
    const val UF_EXTRA_FEED_IS_JUST_LOGGED_IN = "FEED_IS_JUST_LOGGED_IN"
    const val UF_EXTRA_FEED_ENTRY_POINT = "ARGS_FEED_ENTRY_POINT"

    const val NAV_BUTTON_ENTRY_POINT = "Nav button"

    internal const val INTERNAL_FEATURE_PREVENTION = "$INTERNAL_CONTENT/content-prevention"

    private val tokopediaUrl = TokopediaUrl.getInstance().WEB
    private val performanceDashboardUrl = tokopediaUrl + PLAY_LIVE
    private const val PERFORMANCE_DASHBOARD_URL_WEB_VIEW_FORMAT = "" +
        "${ApplinkConst.WEBVIEW}?" +
        "titlebar=false" +
        "&pull_to_refresh=true" +
        "&url=%s" +
        ""
    val PLAY_BROADCASTER_PERFORMANCE_DASHBOARD_APP_LINK = String.format(PERFORMANCE_DASHBOARD_URL_WEB_VIEW_FORMAT, performanceDashboardUrl)
}

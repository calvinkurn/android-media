package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalContent {
    const val HOST_CONTENT = "content"
    const val HOST_AFFILIATE = "affiliate"

    const val INTERNAL_AFFILIATE = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_AFFILIATE"
    const val INTERNAL_CONTENT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_CONTENT"

    const val INTERNAL_AFFILIATE_CREATE_POST_V2 = "$INTERNAL_AFFILIATE/create_post_v2/"
    const val INTERNAL_PRODUCT_PICKER_FROM_SHOP = "$INTERNAL_CONTENT/productpickerfromshop/"
    const val INTERNAL_CONTENT_PRODUCT_TAG_AUTOCOMPLETE = "$INTERNAL_CONTENT/product_tag_autocomplete"
    const val INTERNAL_FEED_CREATION_PRODUCT_SEARCH = "$INTERNAL_CONTENT/creation_product_search"
    const val INTERNAL_FEED_CREATION_SHOP_SEARCH = "$INTERNAL_CONTENT/creation_shop_search"

    const val HASHTAG_PAGE = "$INTERNAL_CONTENT/explore-hashtag/{hashtag}/"

    const val CONTENT_REPORT = "$INTERNAL_CONTENT/content-report/{content_id}"
    const val VIDEO_DETAIL = "$INTERNAL_CONTENT/video-detail/{id}"
    const val COMMENT_NEW = "$INTERNAL_CONTENT/comment-new/{post_id}"

    const val SHOP_POST_PICKER = "$INTERNAL_CONTENT/content-shop/picker"

    const val PROFILE_DETAIL = "${DeeplinkConstant.SCHEME_INTERNAL}://people/{user_id}"

    const val HOST_PLAY = "play"
    const val HOST_FEED = "feed"
    const val HOST_FEED_HOME_NAVIGATION = "home/navigation"
    const val INTERNAL_PLAY = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_PLAY"
    const val INTERNAL_FEED = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_FEED"
    const val INTERNAL_FEED_HOME_NAVIGATION = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_FEED_HOME_NAVIGATION"
    const val PLAY_DETAIL = "$INTERNAL_PLAY/{channel_id}"

    const val HOST_PLAY_BROADCASTER = "play-broadcaster"
    const val INTERNAL_PLAY_BROADCASTER = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_PLAY_BROADCASTER"

    const val INTERNAL_CONTENT_POST_DETAIL = "$INTERNAL_CONTENT/post-detail/"

    const val PLAY_PATH_LITE = "play/channel/"
    const val FEED_VIDEO = "feed/video"

    const val TAB_POSITION_EXPLORE = 2
    const val TAB_POSITION_VIDEO = 3
    const val EXTRA_FEED_TAB_POSITION = "FEED_TAB_POSITION"
    const val ARGS_FEED_VIDEO_TAB_SELECT_CHIP = "tab"

    internal const val INTERNAL_FEATURE_PREVENTION = "$INTERNAL_CONTENT/content-prevention"
}

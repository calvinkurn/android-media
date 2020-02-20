package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalContent {
    const val HOST_CONTENT = "content"
    const val INTERNAL_CONTENT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_CONTENT"
    const val AFFILIATE_EDIT = "$INTERNAL_CONTENT/affiliate/{id}/edit"
    const val SHOP_POST_EDIT = "$INTERNAL_CONTENT/content-shop/{id}/edit"
    const val HASHTAG_PAGE = "$INTERNAL_CONTENT/explore-hashtag/{hashtag}/"

    const val CONTENT_REPORT = "$INTERNAL_CONTENT/content-report/{content_id}"
    const val VIDEO_DETAIL = "$INTERNAL_CONTENT/video-detail/{id}"
    const val MEDIA_PREVIEW = "$INTERNAL_CONTENT/media-preview/{post_id}"
    const val COMMENT = "$INTERNAL_CONTENT/comment/{post_id}"
    const val SHOP_POST_PICKER = "$INTERNAL_CONTENT/content-shop/picker"

    const val HOST_AFFILIATE = "affiliate"
    const val INTERNAL_AFFILIATE = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_AFFILIATE"
    const val AFFILIATE_EXPLORE = "$INTERNAL_AFFILIATE/explore"
    const val AFFILIATE_DASHBOARD = "$INTERNAL_AFFILIATE/dashboard"
    const val AFFILIATE_EDUCATION = "$INTERNAL_AFFILIATE/education"


    const val HOST_PLAY = "play"
    const val INTERNAL_PLAY = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_PLAY"
    const val PLAY_DETAIL = "$INTERNAL_PLAY/{channel_id}"

}
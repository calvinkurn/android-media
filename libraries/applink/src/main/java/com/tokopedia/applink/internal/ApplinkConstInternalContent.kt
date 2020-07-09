package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalContent {
    const val HOST_CONTENT = "content"
    const val HOST_AFFILIATE = "affiliate"

    const val INTERNAL_AFFILIATE = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_AFFILIATE"
    private const val INTERNAL_CONTENT = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_CONTENT"

    const val INTERNAL_CONTENT_CREATE_POST = "$INTERNAL_CONTENT/create_post/"
    const val INTERNAL_CONTENT_DRAFT_POST = "$INTERNAL_CONTENT/draft/"
    const val INTERNAL_AFFILIATE_CREATE_POST = "$INTERNAL_AFFILIATE/create_post/"
    const val INTERNAL_AFFILIATE_DRAFT_POST = "$INTERNAL_AFFILIATE/draft/"
    const val AFFILIATE_EDIT = "$INTERNAL_CONTENT/affiliate/{id}/edit"
    const val SHOP_POST_EDIT = "$INTERNAL_CONTENT/content-shop/{id}/edit"

    const val HASHTAG_PAGE = "$INTERNAL_CONTENT/explore-hashtag/{hashtag}/"

    const val INTERNAL_CONTENT_POST_DETAIL = "$INTERNAL_CONTENT/post-detail/"
    const val CONTENT_REPORT = "$INTERNAL_CONTENT/content-report/{content_id}"
    const val VIDEO_DETAIL = "$INTERNAL_CONTENT/video-detail/{id}"
    const val MEDIA_PREVIEW = "$INTERNAL_CONTENT/media-preview/{post_id}"
    const val COMMENT = "$INTERNAL_CONTENT/comment/{post_id}"
    const val SHOP_POST_PICKER = "$INTERNAL_CONTENT/content-shop/picker"

    const val AFFILIATE_EXPLORE = "$INTERNAL_AFFILIATE/explore"
    const val AFFILIATE_DASHBOARD = "$INTERNAL_AFFILIATE/dashboard"
    const val AFFILIATE_EDUCATION = "$INTERNAL_AFFILIATE/education"
    const val PROFILE_DETAIL = "${DeeplinkConstant.SCHEME_INTERNAL}://people/{user_id}/"


    const val HOST_PLAY = "play"
    const val INTERNAL_PLAY = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_PLAY"
    const val PLAY_DETAIL = "$INTERNAL_PLAY/{channel_id}"

    private const val ARGS_FROM_APPLINK = "isFromApplink"
    private const val KOL_YOUTUBE = "kolyoutube/{youtube_url}"
    private const val ARGS_FROM_APPLINK_VALUE = "true"
    private const val ARG_MEDIA_INDEX = "media_index"
    const val DUMMY_MEDIA_INDEX = "{index}"
    const val COMMENT_EXTRA_PARAM = "?$ARGS_FROM_APPLINK=$ARGS_FROM_APPLINK_VALUE"
    const val INTERNAL_KOL_YOUTUBE = "${DeeplinkConstant.SCHEME_INTERNAL}://${KOL_YOUTUBE}"
    const val INTERNAL_MEDIA_PREVIEW = "$MEDIA_PREVIEW?$ARG_MEDIA_INDEX=$DUMMY_MEDIA_INDEX"

}
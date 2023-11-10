package com.tokopedia.creation.common.util

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.createpost.common.TYPE_CONTENT_SHOP

/**
 * Created By : Jonathan Darwin on November 10, 2023
 */
object StoriesAppLinkBuilder {

    fun buildForShareLink(
        storyId: String,
        authorId: String,
        authorType: String,
    ): String {
        val uri = UriUtil.buildUri(
            ApplinkConst.Stories.STORIES_VIEWER,
            if (authorType == TYPE_CONTENT_SHOP) ApplinkConst.Stories.STORIES_VIEWER_TYPE_SHOP else ApplinkConst.Stories.STORIES_VIEWER_TYPE_USER,
            authorId
        )

        return UriUtil.buildUriAppendParam(
            uri,
            mapOf(
                ApplinkConst.Stories.STORIES_VIEWER_ARG_SOURCE to ApplinkConst.Stories.STORIES_VIEWER_SOURCE_SHARELINK,
                ApplinkConst.Stories.STORIES_VIEWER_ARG_SOURCE_ID to storyId
            )
        )
    }
}

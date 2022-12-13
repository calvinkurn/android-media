package com.tokopedia.play_common.shortsuploader.analytic

import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 13, 2022
 */
class PlayShortsUploadAnalyticImpl @Inject constructor() : PlayShortsUploadAnalytic {

    override fun clickRedirectToChannelRoom(
        authorId: String,
        authorType: String,
        channelId: String
    ) {

    }

    override fun clickRetryUpload(authorId: String, authorType: String, channelId: String) {

    }

//    private fun getAnalyticAuthorType(authorType: String): String {
////        return when(authorType) {
////            ContentCommonUserType.TYPE_USER -> AUTHOR_TYPE_USER
////            ContentCommonUserType.TYPE_SHOP -> AUTHOR_TYPE_SELLER
////            else -> ""
////        }
//    }

    companion object {
        private const val EVENT_NAME = "event"
        private const val EVENT_CATEGORY = "eventCategory"
        private const val EVENT_ACTION = "eventAction"
        private const val EVENT_LABEL = "eventLabel"
        private const val EVENT_BUSINESSUNIT = "businessUnit"
        private const val EVENT_CURRENTSITE = "currentSite"
        private const val EVENT_TRACKER_ID = "trackerId"
        private const val EVENT_SESSION_IRIS = "sessionIris"

        private const val EVENT_CATEGORY_SHORTS = "play broadcast short"
        private const val EVENT_CLICK_CONTENT = "clickContent"
        private const val EVENT_VIEW_CONTENT = "viewContentIris"
        private const val EVENT_ACTION_CLICK_FORMAT = "click - %s"
        private const val EVENT_ACTION_VIEW_FORMAT = "view - %s"

        private const val AUTHOR_TYPE_USER = "user"
        private const val AUTHOR_TYPE_SELLER = "seller"
    }
}

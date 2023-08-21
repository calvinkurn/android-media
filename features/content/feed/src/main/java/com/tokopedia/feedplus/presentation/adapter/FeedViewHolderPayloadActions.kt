package com.tokopedia.feedplus.presentation.adapter

/**
 * Created By : Muhammad Furqan on 24/03/23
 */
object FeedViewHolderPayloadActions {
    const val FEED_POST_LIKED_UNLIKED = 1011
    const val FEED_POST_CLEAR_MODE = 1022
    const val FEED_POST_NOT_SELECTED = 1033
    const val FEED_POST_SELECTED = 1044
    const val FEED_POST_FOLLOW_CHANGED = 1055
    const val FEED_POST_COMMENT_COUNT = 1066
    const val FEED_POST_REMINDER_CHANGED = 1077
    const val FEED_POST_SELECTED_CHANGED = 1088
    const val FEED_POST_SCROLLING_CHANGED = 1099
    const val FEED_POST_SCROLLING = 1111
    const val FEED_POST_DONE_SCROLL = 1112
    const val FEED_FOLLOW_RECOM_RESUME_VIDEO = 1099
    const val FEED_FOLLOW_RECOM_PAUSE_VIDEO = 2000
}

data class FeedViewHolderPayloads(
    val payloads: List<Int>
)

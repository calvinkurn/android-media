package com.tokopedia.content.common.report_content.model

import androidx.annotation.StringRes

data class ContentMenuItem(
    @StringRes val name: Int,
    val iconUnify: Int,
    val type: ContentMenuIdentifier,
    val appLink: String = "",
    val contentData: FeedContentData? = null
)

/**
 * Feed only~
 */
data class FeedContentData(
    val caption: String,
    val postId: String,
    val authorId: String,
    val rowNumber: Int = 0 // absolutePosition
)

enum class ContentMenuIdentifier {
    Edit,
    Report,
    WatchMode,
    SeePerformance,
    LearnVideoInsight,
    Delete;
}

package com.tokopedia.content.common.report_content.model

import androidx.annotation.StringRes

data class FeedMenuItem(
    @StringRes val name: Int,
    val iconUnify: Int,
    val type: FeedMenuIdentifier,
    val appLink: String = "",
    val contentData: FeedContentData? = null
)

data class FeedContentData(
    val caption: String,
    val postId: String,
    val authorId: String,
    val rowNumber: Int = 0 // absolutePosition
)

enum class FeedMenuIdentifier {
    Edit,
    Report,
    WatchMode,
    SeePerformance,
    LearnVideoInsight,
    Delete;
}

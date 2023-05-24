package com.tokopedia.content.common.report_content.model

import android.graphics.drawable.Drawable

data class FeedMenuItem(
    val name: String,
    val drawable: Drawable?,
    val type: FeedMenuIdentifier,
    val contentData: FeedContentData? = null
)

data class FeedContentData(
    val caption: String,
    val postId: String,
    val authorId: String,
    val rowNumber: Int
)

enum class FeedMenuIdentifier {
    Edit,
    Report,
    WatchMode,
    Delete;
}

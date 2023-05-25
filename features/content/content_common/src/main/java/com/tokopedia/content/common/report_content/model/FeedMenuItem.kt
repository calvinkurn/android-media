package com.tokopedia.content.common.report_content.model

import android.graphics.drawable.Drawable

data class FeedMenuItem(
    val name: String,
    var drawable: Drawable?,
    val type: FeedMenuIdentifier,
    val contentData: FeedContentData? = null
)

data class FeedContentData(
    val caption: String,
    val postId: String,
    val authorId: String,
    val rowNumber: Int
)

enum class FeedMenuIdentifier(val value: String) {
    EDIT("Ubah"),
    LAPORKAN("Report"),
    MODE_NONTON("Watch Mode"),
    DELETE("Hapus")
}

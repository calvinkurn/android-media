package com.tokopedia.content.common.report_content.model

import android.graphics.drawable.Drawable

data class FeedMenuItem (
    val name: String,
    var drawable: Drawable?,
    val type: FeedMenuIdentifier
)
enum class FeedMenuIdentifier(val value: String) {
    LAPORKAN("Report"),
    MODE_NONTON("Clear Mode"),
    DELETE("Hapus"),
}

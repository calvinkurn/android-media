package com.tokopedia.content.common.report_content.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContentMenuItem(
    @StringRes val name: Int,
    val iconUnify: Int,
    val type: ContentMenuIdentifier,
    val appLink: String = "",
    val contentData: FeedContentData? = null
): Parcelable

/**
 * Feed only~
 */
@Parcelize
data class FeedContentData(
    val caption: String,
    val postId: String,
    val authorId: String,
    val rowNumber: Int = 0 // absolutePosition
): Parcelable

enum class ContentMenuIdentifier {
    Edit,
    Report,
    WatchMode,
    SeePerformance,
    LearnVideoInsight,
    Delete;
}

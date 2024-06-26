package com.tokopedia.content.common.report_content.model

import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import com.tokopedia.content.common.R

/**
 * @author by astidhiyaa on 27/03/23
 */
data class ContentReportItemUiModel(
    @StringRes val title: Int,
    val icon: Drawable? = null,
    val type: ContentReportType,
)

enum class ContentReportType(val value: String) {
    Spam("spam"),
    Unsuitable("unsuitable"),
    Nudity("nudity"),
    HateSpeech("hateSpeech"),
    Scam("scam"),
    IllegalGoods("illegalGoods"),
    IntellectualProperty("intellectualProperty"),
}

//List of Report - Comment
internal val listOfCommentReport = listOf(
    ContentReportItemUiModel(
        title = R.string.feed_common_reason_desc_spam,
        type = ContentReportType.Spam,
    ),
    ContentReportItemUiModel(
        title = R.string.feed_component_report_subtext2,
        type = ContentReportType.Unsuitable,
    ),
    ContentReportItemUiModel(
        title = R.string.feed_component_report_option_text1,
        type = ContentReportType.Nudity,
    ),
    ContentReportItemUiModel(
        title = R.string.feed_component_report_option_text2,
        type = ContentReportType.HateSpeech,
    ),
    ContentReportItemUiModel(
        title = R.string.feed_component_report_option_text3,
        type = ContentReportType.Scam,
    ),
    ContentReportItemUiModel(
        title = R.string.feed_component_report_option_text4,
        type = ContentReportType.IllegalGoods,
    ),
    ContentReportItemUiModel(
        title = R.string.feed_component_report_option_text5,
        type = ContentReportType.IntellectualProperty,
    ),
)

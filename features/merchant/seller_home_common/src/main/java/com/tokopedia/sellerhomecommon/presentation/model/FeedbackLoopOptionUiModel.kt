package com.tokopedia.sellerhomecommon.presentation.model

import android.content.Context
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerhomecommon.R

/**
 * Created by @ilhamsuaib on 22/08/22.
 */

sealed class FeedbackLoopOptionUiModel(
    open val title: String = String.EMPTY,
    open var isSelected: Boolean = false
) {

    companion object {
        fun getOptions(context: Context): List<FeedbackLoopOptionUiModel> {
            return listOf(
                Static(title = context.getString(R.string.shc_feedback_have_seen_before)),
                Static(title = context.getString(R.string.shc_feedback_not_interesting)),
                Static(title = context.getString(R.string.shc_feedback_not_relevant)),
                Other(title = context.getString(R.string.shc_feedback_other))
            )
        }
    }

    data class Static(
        override val title: String = String.EMPTY,
        override var isSelected: Boolean = false
    ) : FeedbackLoopOptionUiModel(title, isSelected)

    data class Other(
        override val title: String = String.EMPTY,
        override var isSelected: Boolean = false,
        var value: String = String.EMPTY
    ) : FeedbackLoopOptionUiModel(title, isSelected)
}
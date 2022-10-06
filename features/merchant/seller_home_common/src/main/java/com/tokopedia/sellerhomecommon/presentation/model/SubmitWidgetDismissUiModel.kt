package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by @ilhamsuaib on 02/09/22.
 */

data class SubmitWidgetDismissUiModel(
    val action: Action = Action.DISMISS,
    val dismissKey: String = String.EMPTY,
    val dismissObjectIDs: List<String> = emptyList(),
    val dismissSign: String = String.EMPTY,
    val dismissToken: String = String.EMPTY,
    val feedbackReason1: Boolean = false,
    val feedbackReason2: Boolean = false,
    val feedbackReason3: Boolean = false,
    val feedbackReasonOther: String = String.EMPTY,
    val feedbackWidgetIDParent: String = String.EMPTY,
    val shopId: String = String.EMPTY,
    val isFeedbackPositive: Boolean = false
) {

    companion object {
        private const val DISMISS_NAME = "dismiss"
        private const val CANCEL_NAME = "cancelDismiss"
        private const val KEEP_NAME = "keep"
    }

    enum class Action(val actionName: String) {
        DISMISS(DISMISS_NAME),
        CANCEL(CANCEL_NAME),
        KEEP(KEEP_NAME)
    }
}

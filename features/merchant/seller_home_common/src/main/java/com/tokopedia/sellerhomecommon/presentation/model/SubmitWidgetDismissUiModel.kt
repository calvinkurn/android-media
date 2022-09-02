package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created by @ilhamsuaib on 02/09/22.
 */

data class SubmitWidgetDismissUiModel(
    val action: Action,
    val dismissKey: String,
    val dismissObjectIDs: List<String>,
    val dismissSign: String,
    val feedbackReason1: Boolean,
    val feedbackReason2: Boolean,
    val feedbackReason3: Boolean,
    val feedbackReasonOther: String,
    val feedbackWidgetIDParent: String,
    val shopId: String
) {

    companion object {
        private const val DISMISS_NAME = "dismiss"
        private const val CANCEL_NAME = "cancelDismiss"
        private const val KEEP_NAME = "keep"
    }

    enum class Action(name: String) {
        DISMISS(DISMISS_NAME),
        CANCEL(CANCEL_NAME),
        KEEP(KEEP_NAME)
    }
}
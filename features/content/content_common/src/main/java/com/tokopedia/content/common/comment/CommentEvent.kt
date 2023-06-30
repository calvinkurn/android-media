package com.tokopedia.content.common.comment

import com.tokopedia.content.common.comment.uimodel.CommentUiModel

/**
 * @author by astidhiyaa on 20/02/23
 */
sealed interface CommentEvent {
    data class ShowSuccessToaster(
        val message: String? = null
    ) : CommentEvent

    data class ShowErrorToaster(
        val message: Throwable,
        val onClick: () -> Unit
    ) : CommentEvent

    object ShowKeyboard: CommentEvent
    object HideKeyboard: CommentEvent

    data class ReplySuccess(val position: Int): CommentEvent
    data class OpenAppLink(val appLink: String) : CommentEvent
    object OpenReportEvent : CommentEvent

    object ReportSuccess : CommentEvent

    data class AutoType(val parentItem: CommentUiModel.Item) : CommentEvent
}

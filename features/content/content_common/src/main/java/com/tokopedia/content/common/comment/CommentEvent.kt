package com.tokopedia.content.common.comment

import com.tokopedia.content.common.comment.uimodel.CommentUiModel

/**
 * @author by astidhiyaa on 20/02/23
 */
sealed class CommentEvent {
    data class ShowSuccessToaster(
        val message: String? = null
    ) : CommentEvent()

    data class ShowErrorToaster(
        val message: Throwable,
        val onClick: (CommentUiModel.Item) -> Unit
    ) : CommentEvent()

    data class OpenAppLink(val appLink: String) : CommentEvent()
    object ShowKeyboard: CommentEvent()
    object HideKeyboard: CommentEvent()
    object OpenReportEvent : CommentEvent()
}

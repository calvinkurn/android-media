package com.tokopedia.content.common.comment

/**
 * @author by astidhiyaa on 20/02/23
 */
sealed class CommentEvent {
    data class ShowSuccessToaster(
        val message: String? = null
    ) : CommentEvent()

    data class ShowErrorToaster(
        val message: Throwable,
        val onClick: () -> Unit
    ) : CommentEvent()

    data class OpenAppLink(val appLink: String) : CommentEvent()
    object OpenReportEvent : CommentEvent()

    object ReportSuccess : CommentEvent()
}

package com.tokopedia.content.common.comment

import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel

/**
 * @author by astidhiyaa on 14/02/23
 */
sealed interface CommentAction {
    data class LoadNextPage(val commentType: CommentType) : CommentAction
    data class ExpandComment(val comment: CommentUiModel.Expandable) : CommentAction
    object RefreshComment : CommentAction
    object DismissComment: CommentAction

    data class ReplyComment(val comment: String, val commentType: CommentType) : CommentAction

    data class DeleteComment(val isFromToaster: Boolean) : CommentAction
    object PermanentRemoveComment : CommentAction

    data class ReportComment(
        val param: FeedReportRequestParamModel
    ) : CommentAction

    data class EditTextClicked(val item: CommentUiModel.Item): CommentAction
    object RequestReportAction : CommentAction

    data class SelectComment(val comment: CommentUiModel.Item) : CommentAction

    data class OpenAppLinkAction(val appLink: String) : CommentAction
}

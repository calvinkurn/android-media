package com.tokopedia.content.common.comment

import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel

/**
 * @author by astidhiyaa on 14/02/23
 */
sealed class CommentAction {
    data class LoadNextPage(val commentType: CommentType) : CommentAction()
    data class ExpandComment(val comment: CommentUiModel.Expandable) : CommentAction()
    object RefreshComment : CommentAction()
    object DismissComment: CommentAction()

    data class ReplyComment(val comment: CommentUiModel) : CommentAction()
    data class DeleteComment(val comment: CommentUiModel) : CommentAction()
    data class ReportComment(val comment: CommentUiModel) : CommentAction()
}

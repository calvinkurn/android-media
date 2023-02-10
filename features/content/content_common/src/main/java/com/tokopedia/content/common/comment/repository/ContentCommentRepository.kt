package com.tokopedia.content.common.comment.repository

import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.CommentWidgetUiModel
import com.tokopedia.content.common.comment.usecase.PostCommentUseCase

/**
 * @author by astidhiyaa on 08/02/23
 */
interface ContentCommentRepository {
    suspend fun deleteComment(commentId: String): Boolean

    suspend fun replyComment(
        source: PageSource,
        commentType: PostCommentUseCase.CommentType,
        comment: String
    ): CommentUiModel

    suspend fun reportComment(
        source: PageSource,
        reportType: String,
        reason: String,
        detail: String
    ): Boolean

    suspend fun getComments(
        source: PageSource,
        cursor: String,
    ): CommentWidgetUiModel
}

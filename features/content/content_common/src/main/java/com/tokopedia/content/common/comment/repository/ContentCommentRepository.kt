package com.tokopedia.content.common.comment.repository

import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.CommentWidgetUiModel
import com.tokopedia.content.common.comment.uimodel.UserType
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase

/**
 * @author by astidhiyaa on 08/02/23
 */
interface ContentCommentRepository {
    suspend fun deleteComment(commentId: String): Boolean

    suspend fun replyComment(
        source: PageSource,
        commentType: CommentType,
        comment: String,
        commenterType: UserType,
    ): CommentUiModel.Item

    suspend fun reportComment(
        param: FeedComplaintSubmitReportUseCase.Param,
    ): Boolean

    suspend fun getComments(
        source: PageSource,
        commentType: CommentType,
        cursor: String,
    ): CommentWidgetUiModel
}

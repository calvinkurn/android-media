package com.tokopedia.content.common.comment.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.comment.CommentUiModelMapper
import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.uimodel.*
import com.tokopedia.content.common.comment.usecase.*
import com.tokopedia.content.common.comment.usecase.CommentParam
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/02/23
 */
class ContentCommentRepositoryImpl @Inject constructor(
    private val mapper: CommentUiModelMapper,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val postCommentUseCase: PostCommentUseCase,
    private val submitReportCommentUseCase: FeedComplaintSubmitReportUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val dispatcher: CoroutineDispatchers,
) : ContentCommentRepository {

    override suspend fun deleteComment(commentId: String): Boolean =
        withContext(dispatcher.io + NonCancellable) {
            deleteCommentUseCase(
                DeleteCommentUseCase.Param(commentId)
            ).data.status.success == DeleteCommentUseCase.SUCCESS_VALUE
        }

    override suspend fun replyComment(
        source: PageSource,
        commentType: CommentType,
        comment: String,
        commenterType: UserType
    ): CommentUiModel.Item {
        return withContext(dispatcher.io) {
            val type =
                if (commenterType == UserType.Shop) PostCommentUseCase.CommenterType.SHOP else PostCommentUseCase.CommenterType.BUYER
            val response = postCommentUseCase(
                PostCommentUseCase.Param(
                    comment = comment,
                    contentID = source.id,
                    contentType = source.type,
                    commentParentId = commentType.parentId,
                    commenterType = type.value,
                )
            )
            mapper.mapNewComment(
                response.parent.data,
                commenterType
            )
        }
    }

    override suspend fun reportComment(
        param: FeedComplaintSubmitReportUseCase.Param
    ): Boolean = withContext(dispatcher.io) {
        submitReportCommentUseCase(param).data.success
    }

    override suspend fun getComments(
        source: PageSource,
        commentType: CommentType,
        cursor: String
    ): CommentWidgetUiModel = withContext(dispatcher.io) {
        val response = getCommentsUseCase(
            CommentParam(
                contentId = source.id,
                contentType = source.type,
                cursor = cursor,
                limit = if (commentType.isParent) GetCommentsUseCase.PARAM_LIMIT_PARENT_VALUE else GetCommentsUseCase.PARAM_LIMIT_CHILD_VALUE,
                commentParentId = commentType.parentId,
            )
        )
        mapper.mapComments(response)
    }
}

package com.tokopedia.content.common.comment.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.comment.CommentUiModelMapper
import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.CommentWidgetUiModel
import com.tokopedia.content.common.comment.usecase.DeleteCommentUseCase
import com.tokopedia.content.common.comment.usecase.GetCommentsUseCase
import com.tokopedia.content.common.comment.usecase.PostCommentUseCase
import com.tokopedia.content.common.comment.usecase.SubmitReportCommentUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/02/23
 */
class ContentCommentRepositoryImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
    private val mapper: CommentUiModelMapper,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val postCommentUseCase: PostCommentUseCase,
    private val submitReportCommentUseCase: SubmitReportCommentUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
) : ContentCommentRepository {

    private var lastRequestTime: Long = 0L

    private val isCommentAllowed: Boolean
        get() {
            val diff = System.currentTimeMillis() - lastRequestTime
            return diff >= DELAY_MS
        }

    override suspend fun deleteComment(commentId: String): Boolean = withContext(dispatchers.io) {
        val response = deleteCommentUseCase.apply {
            setRequestParams(DeleteCommentUseCase.setParam(commentId))
        }.executeOnBackground().data.status.success == DeleteCommentUseCase.SUCCESS_VALUE
        return@withContext response
    }

    override suspend fun replyComment(
        source: PageSource,
        commentType: CommentType,
        comment: String
    ): CommentUiModel = withContext(dispatchers.io) {
        return@withContext if (!isCommentAllowed) throw MessageErrorException(ERROR_SPAM_MESSAGE)
        else {
            val commenterType =
                if (userSession.isShopOwner) PostCommentUseCase.CommenterType.SHOP else PostCommentUseCase.CommenterType.BUYER
            val response = postCommentUseCase.apply {
                setRequestParams(
                    PostCommentUseCase.setParam(
                        source = source,
                        commentType = commentType,
                        comment = comment,
                        commenterType = commenterType
                    )
                )
            }.executeOnBackground()
            lastRequestTime = System.currentTimeMillis()
            mapper.mapComment(
                response.parent.data,
                response.parent.data.id
            ) //change to commentParentId
        }
    }

    override suspend fun reportComment(
        source: PageSource,
        reportType: String,
        reason: String,
        detail: String
    ): Boolean = withContext(dispatchers.io) {
        val response = submitReportCommentUseCase.apply {
            setRequestParams(
                SubmitReportCommentUseCase.setParam(
                    source = source,
                    reportType = reportType,
                    reason = reason,
                    detail = detail
                )
            )
        }.executeOnBackground().status.success
        return@withContext response
    }

    override suspend fun getComments(
        source: PageSource,
        commentType: CommentType,
        cursor: String
    ): CommentWidgetUiModel =
        withContext(dispatchers.io) {
            val response = getCommentsUseCase.apply {
                setRequestParams(GetCommentsUseCase.setParam(source, cursor, commentType.parentId))
            }.executeOnBackground()
            return@withContext mapper.mapComments(response)
        }

    companion object {
        private const val DELAY_MS = 5000L

        private const val ERROR_SPAM_MESSAGE = "Oops, tidak bisa memberi komentar."
    }
}

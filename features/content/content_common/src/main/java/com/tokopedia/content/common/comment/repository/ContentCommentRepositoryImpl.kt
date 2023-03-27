package com.tokopedia.content.common.comment.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.comment.CommentException
import com.tokopedia.content.common.comment.CommentUiModelMapper
import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.CommentWidgetUiModel
import com.tokopedia.content.common.comment.uimodel.UserType
import com.tokopedia.content.common.comment.usecase.DeleteCommentUseCase
import com.tokopedia.content.common.comment.usecase.GetCommentsUseCase
import com.tokopedia.content.common.comment.usecase.PostCommentUseCase
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.content.common.usecase.FeedComplaintSubmitReportUseCase
import com.tokopedia.network.exception.MessageErrorException
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/02/23
 */
class ContentCommentRepositoryImpl @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val mapper: CommentUiModelMapper,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val postCommentUseCase: PostCommentUseCase,
    private val submitReportCommentUseCase: FeedComplaintSubmitReportUseCase,
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
        comment: String,
        commenterType: UserType
    ): CommentUiModel = withContext(dispatchers.io) {
        return@withContext if (!isCommentAllowed) throw MessageErrorException(CommentException.SendCommentFailed.message)
        else {
            val type =
                if (commenterType == UserType.Shop) PostCommentUseCase.CommenterType.SHOP else PostCommentUseCase.CommenterType.BUYER
            val response = postCommentUseCase.apply {
                setRequestParams(
                    PostCommentUseCase.setParam(
                        source = source,
                        commentType = commentType,
                        comment = comment,
                        commenterType = type
                    )
                )
            }.executeOnBackground()
            lastRequestTime = System.currentTimeMillis()
            mapper.mapNewComment(
                response.parent.data, commenterType
            )
        }
    }

    override suspend fun reportComment(
        param: FeedReportRequestParamModel
    ): Boolean = withContext(dispatchers.io) {
        val response = submitReportCommentUseCase.apply {
            setRequestParams(
                FeedComplaintSubmitReportUseCase.createParam(param)
            )
        }.executeOnBackground().data.success
        return@withContext response
    }

    override suspend fun getComments(
        source: PageSource,
        commentType: CommentType,
        cursor: String
    ): CommentWidgetUiModel =
        withContext(dispatchers.io) {
            val response = getCommentsUseCase.apply {
                setRequestParams(GetCommentsUseCase.setParam(source, cursor, commentType))
            }.executeOnBackground()
            return@withContext mapper.mapComments(response)
        }

    companion object {
        private const val DELAY_MS = 5000L
    }
}

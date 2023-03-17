package com.tokopedia.content.common.comment

import com.tokopedia.content.common.comment.model.Comments
import com.tokopedia.content.common.comment.model.PostComment
import com.tokopedia.content.common.comment.uimodel.CommentType
import com.tokopedia.content.common.comment.uimodel.CommentUiModel
import com.tokopedia.content.common.comment.uimodel.CommentWidgetUiModel
import com.tokopedia.content.common.comment.uimodel.UserType
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import java.time.*
import javax.inject.Inject

/**
 * @author by astidhiyaa on 09/02/23
 */
class CommentUiModelMapper @Inject constructor() {

    private val String.convertToCommentType: CommentType
        get() = if (this == "0") CommentType.Parent else CommentType.Child(this)

    @OptIn(ExperimentalStdlibApi::class)
    fun mapComments(comments: Comments) = CommentWidgetUiModel(
        cursor = comments.parent.lastCursor,
        nextRepliesCount = comments.parent.nextRepliesCountFmt,
        list = buildList {
            comments.parent.comments.forEach {
                add(mapComment(it, comments.parent.parentId))
                if (it.hasReplies) {
                    add(
                        CommentUiModel.Expandable(
                            repliesCount = it.repliesCount,
                            commentType = it.id.convertToCommentType
                        )
                    )
                }
            }
        },
        commentType = comments.parent.parentId.convertToCommentType,
        state = ResultState.Success,
        commenterType = if (comments.parent.isReplyAsShop) UserType.Shop else UserType.People
    )

    fun mapComment(comment: Comments.CommentData, parentId: String): CommentUiModel {
        val username =
            if (comment.isShop) comment.fullName else comment.username.ifBlank { comment.firstName }
        return CommentUiModel.Item(
            id = comment.id,
            username = username,
            photo = comment.photo,
            appLink = comment.linkDetail.appLink,
            content = comment.comment.replace("\n", "<br />").parseAsHtml().toString(),
            createdTime = convertTime(comment.createdTime),
            commentType = parentId.convertToCommentType,
            childCount = comment.repliesCountFmt,
            isOwner = comment.isCommentOwner,
            isReportAllowed = comment.allowReport,
            userId = comment.userId,
            userType = if (comment.isShop) UserType.Shop else UserType.People
        )
    }

    fun mapNewComment(comment: PostComment.Parent.NewComment, userType: UserType): CommentUiModel {
        val username = comment.userInfo.username.ifBlank { comment.userInfo.firstName }
        return CommentUiModel.Item(
            id = comment.id,
            username = username,
            photo = comment.userInfo.photo,
            appLink = comment.userInfo.linkDetail.appLink,
            content = comment.comment.replace("\n", "<br />").parseAsHtml().toString(),
            createdTime = convertTime(comment.createdTime),
            commentType = comment.parentId.convertToCommentType.apply {
                isNewlyAdded = true
            },
            childCount = "0",
            isOwner = true,
            isReportAllowed = false,
            userId = comment.userInfo.userId,
            userType = userType
        )
    }

    private fun convertTime(date: String): String {
        return try {
            val now = ZonedDateTime.now()
            val convert = ZonedDateTime.parse(date)
            val diff = Duration.between(convert, now)
            val minute = diff.toMinutes()
            val hour = diff.toHours()
            val day = diff.toDays()

            return if (day >= 1) {
                "$day $DAY"
            } else if (hour in 1..24) {
                "$hour $HOUR"
            } else if (minute in 1..60) {
                "$minute $MINUTE"
            } else {
                DEFAULT_WORDING
            }
        } catch (e: Exception) {
            DEFAULT_WORDING
        }
    }

    companion object {
        private const val DAY = "hari"
        private const val HOUR = "jam"
        private const val MINUTE = "minute"
        private const val DEFAULT_WORDING = "Beberapa detik yang lalu"
    }
}

package com.tokopedia.feed.common.comment

import com.tokopedia.content.common.types.ResultState
import com.tokopedia.content.common.util.ContentDateConverter
import com.tokopedia.feed.common.comment.model.Comments
import com.tokopedia.feed.common.comment.model.PostComment
import com.tokopedia.feed.common.comment.uimodel.CommentType
import com.tokopedia.feed.common.comment.uimodel.CommentUiModel
import com.tokopedia.feed.common.comment.uimodel.CommentWidgetUiModel
import com.tokopedia.feed.common.comment.uimodel.UserType
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import javax.inject.Inject

/**
 * @author by astidhiyaa on 09/02/23
 */
class CommentUiModelMapper @Inject constructor() {

    private val String.convertToCommentType: CommentType
        get() = if (this == "0") CommentType.Parent else CommentType.Child(this)

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

    private fun mapComment(comment: Comments.CommentData, parentId: String): CommentUiModel {
        val username =
            if (comment.isShop) comment.fullName else comment.username.ifBlank { comment.firstName }
        return CommentUiModel.Item(
            id = comment.id,
            username = username.parseAsHtml().toString(),
            photo = comment.photo,
            appLink = comment.linkDetail.appLink,
            content = comment.comment.replace("\n", "<br />").parseAsHtml().toString(), // to make sure new line doesn't skip
            createdTime = ContentDateConverter.getDiffTime(comment.createdTime),
            commentType = parentId.convertToCommentType,
            childCount = comment.repliesCountFmt,
            isOwner = comment.isCommentOwner,
            isReportAllowed = comment.allowReport,
            userId = comment.userId,
            userType = if (comment.isShop) UserType.Shop else UserType.People
        )
    }

    fun mapNewComment(comment: PostComment.Parent.NewComment, userType: UserType): CommentUiModel.Item {
        val username = comment.userInfo.username.ifBlank { comment.userInfo.name }
        return CommentUiModel.Item(
            id = comment.id,
            username = username.parseAsHtml().toString(),
            photo = comment.userInfo.photo,
            appLink = comment.userInfo.linkDetail.appLink,
            content = comment.comment.replace("\n", "<br />").parseAsHtml().toString(), // to make sure new line doesn't skip
            createdTime = ContentDateConverter.getDiffTime(comment.createdTime),
            commentType = comment.parentId.convertToCommentType,
            childCount = "0",
            isOwner = true,
            isReportAllowed = false,
            userId = comment.userInfo.userId,
            userType = userType
        )
    }
}

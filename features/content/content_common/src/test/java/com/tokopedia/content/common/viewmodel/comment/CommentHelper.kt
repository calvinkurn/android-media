package com.tokopedia.content.common.viewmodel.comment

import com.tokopedia.content.common.comment.uimodel.*
import com.tokopedia.content.common.types.ResultState

/**
 * @author by astidhiyaa on 21/03/23
 */
class CommentHelper {
    fun buildCommentWidget(
        list: List<com.tokopedia.feed.common.comment.uimodel.CommentUiModel> = buildCommentList(),
        cursor: String = "",
        nextRepliesCount: String = "",
        commentType: com.tokopedia.feed.common.comment.uimodel.CommentType = com.tokopedia.feed.common.comment.uimodel.CommentType.Parent,
        state: ResultState = ResultState.Success,
        commenterType: com.tokopedia.feed.common.comment.uimodel.UserType = com.tokopedia.feed.common.comment.uimodel.UserType.People,
    ) = com.tokopedia.feed.common.comment.uimodel.CommentWidgetUiModel(
        list, cursor, nextRepliesCount, commentType, state, commenterType
    )

    fun buildCommentList(list: List<com.tokopedia.feed.common.comment.uimodel.CommentUiModel> = emptyList()) = list

    fun buildItemComment(
        id: String = "123",
        username: String = "yahoo",
        content: String = "Ini comment ya",
        createdTime: String = "2023-05-08T10:24:04+07:00",
        photo: String = "blob.com",
        appLink: String = "",
        commentType: com.tokopedia.feed.common.comment.uimodel.CommentType = com.tokopedia.feed.common.comment.uimodel.CommentType.Parent,
        childCount: String = "0",
        isOwner: Boolean = false,
        isReportAllowed: Boolean = true,
        userId: String = "1",
        userType: com.tokopedia.feed.common.comment.uimodel.UserType = com.tokopedia.feed.common.comment.uimodel.UserType.People,
    ) = com.tokopedia.feed.common.comment.uimodel.CommentUiModel.Item(
        id,
        username,
        content,
        createdTime,
        photo,
        appLink,
        commentType,
        childCount,
        isOwner,
        isReportAllowed,
        userId,
        userType
    )

    fun buildItemExpandable(
        commentType: com.tokopedia.feed.common.comment.uimodel.CommentType = com.tokopedia.feed.common.comment.uimodel.CommentType.Parent,
        repliesCount: String = "",
        isExpanded: Boolean = false,
    ) = com.tokopedia.feed.common.comment.uimodel.CommentUiModel.Expandable(
        commentType = commentType,
        repliesCount = repliesCount,
        isExpanded = isExpanded,
    )

    fun buildCommentParam() = com.tokopedia.feed.common.comment.uimodel.CommentParam()
}

package com.tokopedia.feed.common.comment.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feed.common.comment.model.Comments
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/02/23
 */
class GetCommentsUseCase @Inject constructor(private val repo: GraphqlRepository, dispatchers: CoroutineDispatchers) :
    CoroutineUseCase<CommentParam, Comments>(dispatchers.io) {

    override fun graphqlQuery(): String = QUERY

    override suspend fun execute(params: CommentParam): Comments {
        return repo.request(graphqlQuery(), params.convertToMap())
    }

    companion object {
        const val PARAM_CONTENT_TYPE = "contentType"
        const val PARAM_ID = "contentID"
        const val PARAM_CURSOR = "cursor"
        const val PARAM_LIMIT = "limit"
        const val PARAM_COMMENT_ID = "commentParentID"
        const val PARAM_LIMIT_PARENT_VALUE = 20
        const val PARAM_LIMIT_CHILD_VALUE = 5

        const val QUERY = """
              query getComments(
               ${"$$PARAM_ID"}: String!, 
               ${"$$PARAM_CONTENT_TYPE"}: String!,
               ${"$$PARAM_CURSOR"}: String!,
               ${"$$PARAM_LIMIT"}: Int!,
               ${"$$PARAM_COMMENT_ID"}: String!
              ){
                  feedsCommentGetComments(
                   $PARAM_ID: ${"$$PARAM_ID"},
                   $PARAM_CONTENT_TYPE: ${"$$PARAM_CONTENT_TYPE"},
                   $PARAM_CURSOR: ${"$$PARAM_CURSOR"},
                   $PARAM_LIMIT: ${"$$PARAM_LIMIT"},
                   $PARAM_COMMENT_ID: ${"$$PARAM_COMMENT_ID"}
                  ) {
                    comments {
                      id
                      userID
                      linkDetail {
                        webLink
                        appLink
                        desktopLink
                      }
                      username
                      fullName: userFullName
                      firstName: userFirstName
                      photo: userPhoto
                      badge: userBadge
                      isKol
                      allowReport
                      isCommentOwner
                      createTime
                      comment
                      isShop
                      hasReplies
                      repliesCount
                      repliesCountFmt
                    }
                    nextRepliesCount
                    nextRepliesCountFmt
                    isReplyAsShop
                    totalData
                    lastCursor
                    commentParentID
                    error
                  }
                }
        """
    }
}

data class CommentParam(
    @SerializedName("contentID")
    val contentId: String,
    @SerializedName("contentType")
    val contentType: String,
    @SerializedName("cursor")
    val cursor: String,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("commentParentID")
    val commentParentId: String,
) {
    fun convertToMap() : Map<String, Any> =
        mapOf(
            GetCommentsUseCase.PARAM_ID to contentId,
            GetCommentsUseCase.PARAM_CONTENT_TYPE to contentType,
            GetCommentsUseCase.PARAM_CURSOR to cursor,
            GetCommentsUseCase.PARAM_LIMIT to limit,
            GetCommentsUseCase.PARAM_COMMENT_ID to commentParentId,
        )
}

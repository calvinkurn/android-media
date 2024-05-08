package com.tokopedia.feed.common.comment.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feed.common.comment.model.PostComment
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/02/23
 */
class PostCommentUseCase @Inject constructor(private val repo: GraphqlRepository, dispatchers: CoroutineDispatchers) :
    CoroutineUseCase<PostCommentUseCase.Param, PostComment>(dispatchers.io) {

    override fun graphqlQuery(): String = QUERY

    override suspend fun execute(params: Param): PostComment {
        return repo.request(graphqlQuery(), params.convertToMap())
    }

    companion object {
        private const val PARAM_COMMENT = "comment"
        private const val PARAM_COMMENTER_TYPE = "commenterType"
        private const val PARAM_COMMENT_PARENT_ID = "commentParentID"
        private const val PARAM_CONTENT_TYPE = "contentType"
        private const val PARAM_ID = "contentID"

        const val QUERY = """
            mutation postComment(
            ${"$$PARAM_ID"}: String!, 
            ${"$$PARAM_CONTENT_TYPE"}: String!,
            ${"$$PARAM_COMMENT_PARENT_ID"}: String!,
            ${"$$PARAM_COMMENTER_TYPE"}: Int!,            
            ${"$$PARAM_COMMENT"}: String!
            ){
              feedsCommentPostComment(
                  $PARAM_ID: ${"$$PARAM_ID"},
                  $PARAM_CONTENT_TYPE: ${"$$PARAM_CONTENT_TYPE"},
                  $PARAM_COMMENT_PARENT_ID: ${"$$PARAM_COMMENT_PARENT_ID"},
                  $PARAM_COMMENTER_TYPE: ${"$$PARAM_COMMENTER_TYPE"},
                  $PARAM_COMMENT: ${"$$PARAM_COMMENT"}
              ) {
                data {
                  commentID
                  commentParentID
                  user {
                    isKol: iskol
                    userID: id
                    linkDetail {
                      webLink
                      appLink
                      desktopLink
                    }
                    name
                    firstName
                    username: userName
                    photo
                  }
                  createTime
                  comment
                }
                error
              }
            }
        """
    }

    enum class CommenterType(val value: Int) {
        SHOP(2), BUYER(3)
    }

    data class Param(
        @SerializedName("comment")
        val comment: String,
        @SerializedName("commenterType")
        val commenterType: Int,
        @SerializedName("commentParentID")
        val commentParentId: String,
        @SerializedName("contentType")
        val contentType: String,
        @SerializedName("contentID")
        val contentID: String,
    ) {
        fun convertToMap() : Map<String, Any> = mutableMapOf(
            PARAM_ID to contentID,
            PARAM_CONTENT_TYPE to contentType,
            PARAM_COMMENT_PARENT_ID to commentParentId,
            PARAM_COMMENTER_TYPE to commenterType,
            PARAM_COMMENT to comment,
        )
    }
}

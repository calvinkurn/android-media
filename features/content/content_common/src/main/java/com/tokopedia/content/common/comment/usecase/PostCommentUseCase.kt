package com.tokopedia.content.common.comment.usecase

import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.model.PostComment
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/02/23
 */
@GqlQuery(PostCommentUseCase.QUERY_NAME, PostCommentUseCase.QUERY)
class PostCommentUseCase @Inject constructor(repo: GraphqlRepository) :
    GraphqlUseCase<PostComment>(repo) {

    init {
        setGraphqlQuery(PostCommentUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(PostComment::class.java)
    }

    companion object {
        private const val PARAM_COMMENT = "comment"
        private const val PARAM_COMMENTER_TYPE = "commenterType"
        private const val PARAM_COMMENT_PARENT_ID = "commentParentID"
        private const val PARAM_CONTENT_TYPE = "contentType"
        private const val PARAM_ID = "contentID"

        fun setParam(
            source: PageSource,
            commentType: CommentType,
            commenterType: CommenterType,
            comment: String
        ) = mapOf(
            PARAM_ID to source.id,
            PARAM_CONTENT_TYPE to source.type,
            PARAM_COMMENT_PARENT_ID to commentType.value,
            PARAM_COMMENTER_TYPE to commenterType.value,
            PARAM_COMMENT to comment,
        )

        const val QUERY_NAME = "PostCommentUseCaseQuery"
        const val QUERY = """
            mutation postComment(
            ${"$${PARAM_ID}"}: String!, 
            ${"$${PARAM_CONTENT_TYPE}"}: String!,
            ${"$${PARAM_COMMENT_PARENT_ID}"}: String!,
            ${"$${PARAM_COMMENTER_TYPE}"}: Int!,            
            ${"$${PARAM_COMMENT}"}: String!
            ){
              feedsCommentPostComment(
                  ${PARAM_ID}: ${"$${PARAM_ID}"},
                  ${PARAM_CONTENT_TYPE}: ${"$${PARAM_CONTENT_TYPE}"},
                  ${PARAM_COMMENT_PARENT_ID}: ${"$${PARAM_COMMENT_PARENT_ID}"},
                  ${PARAM_COMMENTER_TYPE}: ${"$${PARAM_COMMENTER_TYPE}"},
                  ${PARAM_COMMENT}: ${"$${PARAM_COMMENT}"}
              ) {
                data {
                  commentID
                  commentParentID
                  user {
                    isKol: iskol
                    id
                    linkDetail: LinkDetail {
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

    enum class CommentType(val value: String) {
        PARENT("0"), CHILD("123")
    } // not necessary to make enum

    enum class CommenterType(val value: Int) {
        SHOP(2), BUYER(3)
    }
}

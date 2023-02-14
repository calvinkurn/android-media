package com.tokopedia.content.common.comment.usecase

import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.model.Comments
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/02/23
 */
@GqlQuery(GetCommentsUseCase.QUERY_NAME, GetCommentsUseCase.QUERY)
class GetCommentsUseCase @Inject constructor(repo: GraphqlRepository) :
    GraphqlUseCase<Comments>(repo) {

    init {
        setGraphqlQuery(GetCommentsUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(Comments::class.java)
    }

    companion object {
        private const val PARAM_CONTENT_TYPE = "contentType"
        private const val PARAM_ID = "contentID"
        private const val PARAM_CURSOR = "cursor"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_COMMENT_ID = "commentParentID"
        private const val PARAM_LIMIT_VALUE = 20

        fun setParam(
            source: PageSource,
            cursor: String,
            parentId: String
        ) = mapOf(
            PARAM_ID to source.id,
            PARAM_CONTENT_TYPE to source.type,
            PARAM_CURSOR to cursor,
            PARAM_LIMIT to PARAM_LIMIT_VALUE,
            PARAM_COMMENT_ID to parentId
        )

        const val QUERY_NAME = "GetCommentsUseCaseQuery"
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

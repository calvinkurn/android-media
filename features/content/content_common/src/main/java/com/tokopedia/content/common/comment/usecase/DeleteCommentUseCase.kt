package com.tokopedia.content.common.comment.usecase

import com.tokopedia.content.common.comment.model.DeleteComment
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/02/23
 */
@GqlQuery(DeleteCommentUseCase.QUERY_NAME, DeleteCommentUseCase.QUERY)
class DeleteCommentUseCase @Inject constructor(repo: GraphqlRepository) :
    GraphqlUseCase<DeleteComment>(repo) {

    init {
        setGraphqlQuery(DeleteCommentUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(DeleteComment::class.java)
    }

    companion object {
        private const val PARAM_COMMENT_ID = "commentID"

        const val SUCCESS_VALUE = 1L

        fun setParam(commentId: String) = mapOf(PARAM_COMMENT_ID to commentId)

        const val QUERY_NAME = "DeleteCommentUseCaseQuery"
        const val QUERY = """
            mutation deleteComment (${"$$PARAM_COMMENT_ID"}: String!){
	            feedsCommentDeleteComment($PARAM_COMMENT_ID: ${"$$PARAM_COMMENT_ID"}){
                    data{
                      success
                    }
                    error
              }
            }
        """
    }
}

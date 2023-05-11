package com.tokopedia.content.common.comment.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.comment.model.DeleteComment
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/02/23
 */
class DeleteCommentUseCase @Inject constructor(
    private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<DeleteParam, DeleteComment>(dispatchers.io) {

    override fun graphqlQuery(): String = QUERY


    override suspend fun execute(params: DeleteParam): DeleteComment {
        return repo.request(graphqlQuery(), params.convertToMap())
    }

    companion object {
        const val PARAM_COMMENT_ID = "commentID"

        const val SUCCESS_VALUE = 1L

        private const val QUERY = """
            mutation deleteComment (${"$$PARAM_COMMENT_ID"}: String!){
	            feedsCommentDeleteComment($PARAM_COMMENT_ID: ${"$$PARAM_COMMENT_ID"}){
                    data {
                      success
                    }
                    error
              }
            }
        """
    }
}

data class DeleteParam(
    @SerializedName("commentID")
    val commentId: String
) {
    fun convertToMap() : Map<String, Any> =
        mapOf(
            DeleteCommentUseCase.PARAM_COMMENT_ID to commentId
        )
}

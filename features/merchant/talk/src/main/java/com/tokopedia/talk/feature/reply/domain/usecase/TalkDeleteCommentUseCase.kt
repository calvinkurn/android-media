package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.delete.comment.TalkDeleteCommentResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkDeleteCommentUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<TalkDeleteCommentResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TALK_ID = "talk_id"
        const val PARAM_COMMENT_ID = "comment_id"
        private const val TALK_DELETE_COMMENT_MUTATION_CLASS_NAME = "TalkDeleteComment"
        private const val query =
            """
                mutation talkDeleteComment(${'$'}talk_id: Int,${'$'}comment_id: Int) {
                  talkDeleteComment(talk_id:${'$'}talk_id, comment_id:${'$'}comment_id) {
                    status
                    messageError
                    data {
                      isSuccess
                      commentId
                    }
                    messageErrorOriginal
                  }
                }
            """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(TALK_DELETE_COMMENT_MUTATION_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(TalkDeleteComment.GQL_QUERY)
        setTypeClass(TalkDeleteCommentResponseWrapper::class.java)
    }

    fun setParams(talkId: Int, commentId: Int) {
        val requestParams = RequestParams()
        requestParams.putInt(PARAM_TALK_ID, talkId)
        requestParams.putInt(PARAM_COMMENT_ID, commentId)
        setRequestParams(requestParams.parameters)
    }
}
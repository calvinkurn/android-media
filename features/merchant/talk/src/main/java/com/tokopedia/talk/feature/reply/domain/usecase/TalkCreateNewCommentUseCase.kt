package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.createcomment.TalkCreateNewCommentResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkCreateNewCommentUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<TalkCreateNewCommentResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TEXT = "text"
        const val PARAM_TALK_ID = "talk_id"
        const val PARAM_PRODUCT_IDS = "product_ids"
        private const val TALK_CREATE_NEW_COMMENT_MUTATION_CLASS_NAME = "TalkCreateNewComment"
        private const val query =
            """
                mutation talkCreateNewComment(${'$'}text: String, ${'$'}talk_id: Int, ${'$'}product_ids: String) {
                  talkCreateNewComment(text:${'$'}text, talk_id:${'$'}talk_id, product_ids:${'$'}product_ids) {
                    status
                    messageError
                    data {
                      isSuccess
                      commentID
                    }
                    messageErrorOriginal
                  }
                }
            """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(TALK_CREATE_NEW_COMMENT_MUTATION_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(TalkCreateNewComment.GQL_QUERY)
        setTypeClass(TalkCreateNewCommentResponseWrapper::class.java)
    }

    fun setParams(text: String, talkId: Int, productIds: String) {
        val requestParams = RequestParams()
        requestParams.putString(PARAM_TEXT, text)
        requestParams.putInt(PARAM_TALK_ID, talkId)
        requestParams.putString(PARAM_PRODUCT_IDS, productIds)
        setRequestParams(requestParams.parameters)
    }
}
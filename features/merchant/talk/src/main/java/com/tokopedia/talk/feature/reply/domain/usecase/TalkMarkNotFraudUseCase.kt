package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.unmask.TalkMarkNotFraudResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkMarkNotFraudUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<TalkMarkNotFraudResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TALK_ID = "talk_id"
        private const val TALK_MARK_NOT_FRAUD_MUTATION_CLASS_NAME = "TalkMarkNotFraud"
        private const val query =
            """
                mutation talkMarkNotFraud(${'$'}talk_id: Int) {
                  talkMarkNotFraud(talk_id:${'$'}talk_id) {
                    status
                    messageError
                    data {
                      isSuccess
                    }
                    messageErrorOriginal
                  }
                }
            """
    }

    init {
      setupUseCase()
    }

    @GqlQuery(TALK_MARK_NOT_FRAUD_MUTATION_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(TalkMarkNotFraud.GQL_QUERY)
        setTypeClass(TalkMarkNotFraudResponseWrapper::class.java)
    }

    fun setParams(talkId: Int) {
        setRequestParams(
                RequestParams.create().apply {
                    putInt(TalkMarkCommentNotFraudUseCase.PARAM_TALK_ID, talkId)
                }.parameters
        )
    }
}
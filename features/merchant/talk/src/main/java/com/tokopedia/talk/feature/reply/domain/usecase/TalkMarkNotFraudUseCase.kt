package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.unmask.TalkMarkNotFraudResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkMarkNotFraudUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<TalkMarkNotFraudResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TALK_ID = "talk_id"
        private val query by lazy {
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
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
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
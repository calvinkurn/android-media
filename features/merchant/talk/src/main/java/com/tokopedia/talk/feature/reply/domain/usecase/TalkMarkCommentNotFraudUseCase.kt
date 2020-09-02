package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.unmask.TalkMarkCommentNotFraudResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkMarkCommentNotFraudUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<TalkMarkCommentNotFraudResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TALK_ID = "talk_id"
        const val PARAM_COMMENT_ID = "comment_id"
        private val query by lazy {
            """
                mutation talkMarkCommentNotFraud(${'$'}talk_id: Int,${'$'}comment_id: Int) {
                  talkMarkCommentNotFraud(talk_id:${'$'}talk_id, comment_id:${'$'}comment_id) {
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
        setTypeClass(TalkMarkCommentNotFraudResponseWrapper::class.java)
    }

    fun setParams(talkId: Int, commentId: Int) {
        setRequestParams(
                RequestParams.create().apply {
                    putInt(PARAM_TALK_ID, talkId)
                    putInt(PARAM_COMMENT_ID, commentId)
                }.parameters
        )
    }
}
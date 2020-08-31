package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.delete.talk.TalkDeleteTalkResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkDeleteTalkUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<TalkDeleteTalkResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TALK_ID = "talk_id"

        private val query by lazy {
            val talkId = "\$talk_id"
            """
                mutation talkDeleteTalk($talkId: Int) {
                  talkDeleteTalk(talk_id:$talkId) {
                    status
                    messageError
                    data {
                      isSuccess
                      talkID
                    }
                    messageErrorOriginal
                  }
                }
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(TalkDeleteTalkResponseWrapper::class.java)
    }

    fun setParams(talkId: Int) {
        val requestParams = RequestParams()
        requestParams.putInt(PARAM_TALK_ID, talkId)
        setRequestParams(requestParams.parameters)
    }

}

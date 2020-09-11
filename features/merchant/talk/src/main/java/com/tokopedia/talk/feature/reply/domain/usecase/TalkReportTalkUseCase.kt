package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.report.TalkReportTalkResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkReportTalkUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<TalkReportTalkResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TALK_ID = "talk_id"
        private val query by lazy {
            """
                mutation talkReportTalk(${'$'}talk_id: Int, ${'$'}reason: String, ${'$'}report_type: Int) {
                  talkReportTalk(talk_id: ${'$'}talk_id, reason: ${'$'}reason, report_type: ${'$'}report_type) {
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
        setTypeClass(TalkReportTalkResponseWrapper::class.java)
    }

    fun setParams(talkId: Int) {
        val requestParams = RequestParams()
        requestParams.putInt(PARAM_TALK_ID, talkId)
        setRequestParams(requestParams.parameters)
    }
}
package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.report.TalkReportTalkResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkReportTalkUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<TalkReportTalkResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TALK_ID = "talk_id"
        const val PARAM_REASON = "reason"
        const val SELLER_REPORT_REASON = "seller dismiss unmask"
        const val PARAM_REPORT_TYPE = "report_type"
        const val OTHER_REPORT_TYPE = 3
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
        requestParams.putString(PARAM_REASON, SELLER_REPORT_REASON)
        requestParams.putInt(PARAM_REPORT_TYPE, OTHER_REPORT_TYPE)
        setRequestParams(requestParams.parameters)
    }
}
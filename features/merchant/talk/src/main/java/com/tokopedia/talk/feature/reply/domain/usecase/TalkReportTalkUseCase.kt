package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.report.TalkReportTalkResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkReportTalkUseCase @Inject constructor(
    @ApplicationContext
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TalkReportTalkResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_TALK_ID = "talk_id"
        const val PARAM_REASON = "reason"
        const val SELLER_REPORT_REASON = "seller dismiss unmask"
        const val REPORT_REASON_BLOCK = "suggest to block"
        const val PARAM_REPORT_TYPE = "report_type"
        const val OTHER_REPORT_TYPE = 3
        private const val TALK_REPORT_TALK_MUTATION_CLASS_NAME = "TalkReportTalk"
        private const val query =
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
            """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(TALK_REPORT_TALK_MUTATION_CLASS_NAME, query)
    private fun setupUseCase() {
        setGraphqlQuery(TalkReportTalk.GQL_QUERY)
        setTypeClass(TalkReportTalkResponseWrapper::class.java)
    }

    fun setParams(talkId: Long, reason: String = "", reportType: Int = OTHER_REPORT_TYPE) {
        val requestParams = RequestParams()
        requestParams.putLong(PARAM_TALK_ID, talkId)
        if(reason.isNotBlank()) {
            requestParams.putString(PARAM_REASON, reason)
        }
        requestParams.putInt(PARAM_REPORT_TYPE, reportType)
        setRequestParams(requestParams.parameters)
    }
}

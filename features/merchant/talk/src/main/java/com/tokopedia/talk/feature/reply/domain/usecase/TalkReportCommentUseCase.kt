package com.tokopedia.talk.feature.reply.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.talk.feature.reply.data.model.report.TalkReportCommentResponseWrapper
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class TalkReportCommentUseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<TalkReportCommentResponseWrapper>(graphqlRepository) {

    companion object {
        const val PARAM_COMMENT_ID = "comment_id"
        const val PARAM_REASON = "reason"
        const val SELLER_REPORT_REASON = "seller dismiss unmask"
        const val PARAM_REPORT_TYPE = "report_type"
        const val OTHER_REPORT_TYPE = 3
        private const val TALK_MARK_NOT_FRAUD_MUTATION_CLASS_NAME = "TalkReportComment"
        private const val query =
            """
                mutation talkReportComment(${'$'}comment_id: Int, ${'$'}reason: String, ${'$'}report_type: Int) {
                  talkReportComment(comment_id: ${'$'}comment_id, reason: ${'$'}reason, report_type: ${'$'}report_type) {
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
        setGraphqlQuery(TalkReportComment.GQL_QUERY)
        setTypeClass(TalkReportCommentResponseWrapper::class.java)
    }

    fun setParams(commentId: Int, reason: String = "", reportType: Int = OTHER_REPORT_TYPE) {
        val requestParams = RequestParams()
        requestParams.putInt(PARAM_COMMENT_ID, commentId)
        if(reason.isNotBlank()) {
            requestParams.putString(TalkReportTalkUseCase.PARAM_REASON, reason)
        }
        requestParams.putInt(PARAM_REPORT_TYPE, reportType)
        setRequestParams(requestParams.parameters)
    }
}
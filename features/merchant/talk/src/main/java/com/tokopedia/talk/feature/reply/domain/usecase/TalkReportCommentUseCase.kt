package com.tokopedia.talk.feature.reply.domain.usecase

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
        private val query by lazy {
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
            """.trimIndent()
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(TalkReportCommentResponseWrapper::class.java)
    }

    fun setParams(commentId: Int) {
        val requestParams = RequestParams()
        requestParams.putInt(PARAM_COMMENT_ID, commentId)
        requestParams.putString(PARAM_REASON, SELLER_REPORT_REASON)
        requestParams.putInt(PARAM_REPORT_TYPE, OTHER_REPORT_TYPE)
        setRequestParams(requestParams.parameters)
    }
}
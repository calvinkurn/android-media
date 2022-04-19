package com.tokopedia.review.feature.inbox.buyerreview.domain.interactor.report

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.report.ReportReviewResponse
import com.tokopedia.usecase.RequestParams
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 9/13/17.
 */
class ReportReviewUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<ReportReviewResponse>
) {

    init {
        useCase.setTypeClass(ReportReviewResponse::class.java)
        useCase.setGraphqlQuery(QUERY)
    }

    private fun createParam(
        feedbackId: String,
        reasonCode: Int,
        reasonText: String
    ): HashMap<String, Any> {
        val params: RequestParams = RequestParams.create()
        params.putString(PARAM_FEEDBACK_ID, feedbackId)
        params.putInt(PARAM_REASON_CODE, reasonCode)
        params.putString(PARAM_REASON_TEXT, reasonText)
        return params.parameters
    }

    suspend fun execute(
        feedbackId: String,
        reasonCode: Int,
        reasonText: String
    ): ReportReviewResponse.ProductrevReportReview {
        useCase.setRequestParams(createParam(feedbackId, reasonCode, reasonText))
        return useCase.executeOnBackground().productrevReportReview
    }

    companion object {
        const val REPORT_SPAM: Int = 1
        const val REPORT_SARA: Int = 2
        const val REPORT_OTHER: Int = 3

        private const val QUERY = """
            mutation report_review(${'$'}feedbackID: String!, ${'$'}reasonCode: Int!, ${'$'}reasonText: String) {
              productrevReportReview(feedbackID: ${'$'}feedbackID, reasonCode: ${'$'}reasonCode, reasonText: ${'$'}reasonText) {
                success
              }
            }
        """
        private const val PARAM_FEEDBACK_ID: String = "feedbackID"
        private const val PARAM_REASON_CODE: String = "reasonCode"
        private const val PARAM_REASON_TEXT: String = "reasonText"
    }
}
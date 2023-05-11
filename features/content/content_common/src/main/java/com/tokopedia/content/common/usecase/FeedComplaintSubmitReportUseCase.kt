package com.tokopedia.content.common.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedComplaintSubmitReportResponse
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class FeedComplaintSubmitReportUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<FeedReportRequestParamModel, FeedComplaintSubmitReportResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = QUERY

    override suspend fun execute(params: FeedReportRequestParamModel): FeedComplaintSubmitReportResponse {
        return graphqlRepository.request(graphqlQuery(), params.convertToMap())
    }

    companion object {
        const val VALUE_REPORT_TYPE_POST = "post"
        const val VALUE_REPORT_TYPE_COMMENT = "comment"

        const val PARAM_REPORT_TYPE = "reportType"
        const val PARAM_CONTENT_ID = "contentID"
        const val PARAM_REASON = "reason"
        const val PARAM_REASON_DETAILS = "reasonDetails"

        private const val QUERY = """
            mutation FeedsComplaintSubmitReport(
            ${'$'}$PARAM_REPORT_TYPE: String!, 
            ${'$'}$PARAM_CONTENT_ID: String!, 
            ${'$'}$PARAM_REASON: String!, 
            ${'$'}$PARAM_REASON_DETAILS: String!
            ){
              feedsComplaintSubmitReport(
                  reportType: ${'$'}$PARAM_REPORT_TYPE, 
                  contentID: ${'$'}$PARAM_CONTENT_ID, 
                  reason: ${'$'}$PARAM_REASON, 
                  reasonDetails: ${'$'}$PARAM_REASON_DETAILS
              ) {
                success
              }
            }
        """
    }
}

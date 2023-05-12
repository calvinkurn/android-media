package com.tokopedia.content.common.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.FeedComplaintSubmitReportResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class FeedComplaintSubmitReportUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<FeedComplaintSubmitReportUseCase.Param, FeedComplaintSubmitReportResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = QUERY

    override suspend fun execute(params: Param): FeedComplaintSubmitReportResponse {
        return graphqlRepository.request(graphqlQuery(), params.convertToMap())
    }

    data class Param(
        @SerializedName("reportType")
        val reportType: String = VALUE_REPORT_TYPE_POST,
        @SerializedName("contentID")
        val contentId: String,
        @SerializedName("reason")
        val reason: String,
        @SerializedName("reasonDetails")
        val reasonDetails: String
    ) {
        fun convertToMap() : Map<String, Any> =
            mapOf(
                PARAM_REPORT_TYPE to reportType,
                PARAM_CONTENT_ID to contentId,
                PARAM_REASON to reason,
                PARAM_REASON_DETAILS to reasonDetails
            )
    }

    companion object {
        const val VALUE_REPORT_TYPE_POST = "post"
        const val VALUE_REPORT_TYPE_COMMENT = "comment"

        private const val PARAM_REPORT_TYPE = "reportType"
        private const val PARAM_CONTENT_ID = "contentID"
        private const val PARAM_REASON = "reason"
        private const val PARAM_REASON_DETAILS = "reasonDetails"

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

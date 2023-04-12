package com.tokopedia.content.common.usecase

import com.tokopedia.content.common.model.FeedComplaintSubmitReportResponse
import com.tokopedia.content.common.report_content.model.FeedReportRequestParamModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

@GqlQuery(FeedComplaintSubmitReportUseCase.QUERY_NAME, FeedComplaintSubmitReportUseCase.QUERY)
class FeedComplaintSubmitReportUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<FeedComplaintSubmitReportResponse>(graphqlRepository) {

    init {
        setTypeClass(FeedComplaintSubmitReportResponse::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(FeedComplaintSubmitReportQuery())
    }

    companion object {
        private const val PARAM_REPORT_TYPE = "reportType"
        private const val PARAM_CONTENT_ID = "contentID"
        private const val PARAM_REASON = "reason"
        private const val PARAM_REASON_DETAILS = "reasonDetails"

        const val VALUE_REPORT_TYPE_POST = "post"
        const val VALUE_REPORT_TYPE_COMMENT = "comment"

        const val QUERY_NAME = "FeedComplaintSubmitReportQuery"

        const val QUERY = """
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

        fun createParam(
            feedReportRequestParamModel: FeedReportRequestParamModel
        ): Map<String, Any> {
            return mapOf(
                PARAM_REPORT_TYPE to feedReportRequestParamModel.reportType,
                PARAM_CONTENT_ID to feedReportRequestParamModel.contentId,
                PARAM_REASON to feedReportRequestParamModel.reason,
                PARAM_REASON_DETAILS to feedReportRequestParamModel.reasonDetails
            )
        }
    }
}

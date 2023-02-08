package com.tokopedia.content.common.comment.usecase

import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.model.SubmitReportComment
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * @author by astidhiyaa on 08/02/23
 */
@GqlQuery(SubmitReportCommentUseCase.QUERY_NAME, SubmitReportCommentUseCase.QUERY)
class SubmitReportCommentUseCase @Inject constructor(repo: GraphqlRepository) :
    GraphqlUseCase<SubmitReportComment>(repo) {
    init {
        setGraphqlQuery(SubmitReportCommentUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(SubmitReportComment::class.java)
    }

    companion object {
        private const val PARAM_REPORT_TYPE = "reportType"
        private const val PARAM_ID = "contentID"
        private const val PARAM_REASON = "reason"
        private const val PARAM_REASON_DETAIL = "reasonDetails"

        fun setParam(
            source: PageSource,
            reportType: String,
            reason: String,
            detail: String
        ) = mapOf(
            PARAM_ID to source.id,
            PARAM_REPORT_TYPE to reportType,
            PARAM_REASON to reason,
            PARAM_REASON_DETAIL to detail,
        )

        const val QUERY_NAME = "SubmitReportCommentUseCaseQuery"
        const val QUERY = """
            mutation commentReport (
            ${"$${PARAM_ID}"}: String!,
            ${"$${PARAM_REPORT_TYPE}"}: String!,
            ${"$${PARAM_REASON}"}: String!,
            ${"$${PARAM_REASON_DETAIL}"}: String!
            ){
              feedsComplaintSubmitReport(
               ${PARAM_ID}: ${"$${PARAM_ID}"},
               ${PARAM_REPORT_TYPE}: ${"$${PARAM_REPORT_TYPE}"},
               ${PARAM_REASON}: ${"$${PARAM_REASON}"},
               ${PARAM_REASON_DETAIL}: ${"$${PARAM_REASON_DETAIL}"},
              ) {
                success
              }
            }
        """
    }
}

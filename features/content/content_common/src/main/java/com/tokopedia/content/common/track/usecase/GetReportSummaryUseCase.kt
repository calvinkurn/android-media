package com.tokopedia.content.common.track.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.track.usecase.GetReportSummaryUseCase.Companion.QUERY
import com.tokopedia.content.common.track.usecase.GetReportSummaryUseCase.Companion.QUERY_NAME
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.content.common.track.response.GetReportSummaryResponse
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 08 March 2024
 */
@GqlQuery(QUERY_NAME, QUERY)
class GetReportSummaryUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<GetReportSummaryRequest, GetReportSummaryResponse>(dispatchers.io) {

    private val gqlQuery: GqlQueryInterface = GetReportSummaryUseCaseQuery()

    override fun graphqlQuery(): String = gqlQuery.getQuery()

    override suspend fun execute(params: GetReportSummaryRequest): GetReportSummaryResponse {
        return repository.request(gqlQuery, params)
    }

    companion object {
        private const val PARAM_CONTENT_IDS = "contentIDs"
        private const val PARAM_CONTENT_TYPE = "contentType"

        const val QUERY_NAME = "GetReportSummaryUseCaseQuery"
        const val QUERY = """
            query broadcasterReportSummariesBulkV2(
                ${"$$PARAM_CONTENT_IDS"}: [String]!,
                ${"$$PARAM_CONTENT_TYPE"}: String!
            ) {
                broadcasterReportSummariesBulkV2(
                    $PARAM_CONTENT_IDS: ${"$$PARAM_CONTENT_IDS"},
                    $PARAM_CONTENT_TYPE: ${"$$PARAM_CONTENT_TYPE"}
                ) {
                    reportData {
                        content {
                            metrics {
                                liveConcurrentUsersFmt
                                visitContentFmt
                                estimatedIncomeFmt
                                totalLikeFmt
                            }
                            userMetrics {
                                liveConcurrentUsersFmt
                                visitContentFmt
                                estimatedIncomeFmt
                                totalLikeFmt
                                totalLike
                            }
                        }
                    }
                    timestamp
                }
            }
        """
    }
}

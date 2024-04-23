package com.tokopedia.play.broadcaster.domain.usecase.report.product

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.play.broadcaster.domain.model.report.product.GetReportProductSummaryRequest
import com.tokopedia.play.broadcaster.domain.model.report.product.GetReportProductSummaryResponse
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 06 March 2024
 */
@GqlQuery(GetReportProductSummaryUseCase.QUERY_NAME, GetReportProductSummaryUseCase.QUERY)
class GetReportProductSummaryUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<GetReportProductSummaryRequest, GetReportProductSummaryResponse>(dispatchers.io) {

    private val gqlQuery: GqlQueryInterface = GetReportProductSummaryUseCaseQuery()

    override fun graphqlQuery(): String = gqlQuery.getQuery()

    override suspend fun execute(params: GetReportProductSummaryRequest): GetReportProductSummaryResponse {
        return repository.request(gqlQuery, params)
    }

    companion object {
        private const val PARAM_REQ = "req"

        const val QUERY_NAME = "GetReportProductSummaryUseCaseQuery"
        const val QUERY = """
            query broadcasterReportReportProductSummary(
                ${"$$PARAM_REQ"}: broadcasterReportReportProductSummaryRequest!
            ) {
                broadcasterReportReportProductSummary(
                    $PARAM_REQ: ${"$$PARAM_REQ"}
                ) {
                    reportProductAggregate {
                        estimatedIncomeFmt
                        visitPDPFmt
                        addToCartFmt
                        productSoldQtyFmt
                    }
                    reportProductMetricsWithDetail {
                        reportProductMetric {
                            estimatedIncomeFmt
                            visitPDPFmt
                            addToCartFmt
                            productSoldQtyFmt
                        }
                        productName
                        productImageURL
                        productID
                    }
                }
            }
        """
    }
}

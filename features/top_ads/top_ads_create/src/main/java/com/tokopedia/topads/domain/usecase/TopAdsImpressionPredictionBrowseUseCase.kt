package com.tokopedia.topads.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.data.ImpressionPredictionResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

private const val IMPRESSION_PREDICTION_QUERY_BROWSE = """query umpGetImpressionPredictionBrowse(${'$'}productIDs: [String]!, ${'$'}initialBid: Float, ${'$'}finalBid: Float!, ${'$'}dailyBudget: Float, ${'$'}source: String!) {
  umpGetImpressionPredictionBrowse(productIDs: ${'$'}productIDs, initialBid: ${'$'}initialBid, finalBid: ${'$'}finalBid, dailyBudget: ${'$'}dailyBudget, source: ${'$'}source) {
    data {
      impression {
        oldImpression
        finalImpression
        increment
      }
    }
    error {
      title
    }
  }
}"""

@GqlQuery("ImpressionPredictionQueryBrowse", IMPRESSION_PREDICTION_QUERY_BROWSE)
class TopAdsImpressionPredictionBrowseUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<ImpressionPredictionResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(IMPRESSION_PREDICTION_QUERY_BROWSE)
        setTypeClass(ImpressionPredictionResponse::class.java)
    }

    suspend operator fun invoke(source: String,
                                productIds: List<String>,
                                finalBid: Float,
                                initialBid: Float,
                                dailyBudget: Float): Result<ImpressionPredictionResponse> {
        setRequestParams(createRequestParam(source, productIds, finalBid, initialBid, dailyBudget).parameters)

        val data = executeOnBackground()

        return when {
            data.umpGetImpressionPrediction.error.title.isEmpty() -> {
                Success(data)
            }

            else -> Fail(Throwable(data.umpGetImpressionPrediction.error.title))
        }
    }

    private fun createRequestParam(source: String,
                                   productIds: List<String>,
                                   finalBid: Float,
                                   initialBid: Float,
                                   dailyBudget: Float): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putObject("finalBid", finalBid)
        requestParams.putObject("initialBid", initialBid)
        requestParams.putObject("dailyBudget", dailyBudget)
        requestParams.putObject("productIDs", productIds)
        requestParams.putString(ParamObject.SOURCE, source)
        return requestParams
    }

//    object GqlQuery : GqlQueryInterface {
//        var OPERATION_NAME = "umpGetImpressionPredictionSearch"
//        private val QUERY = """
//            query $OPERATION_NAME(${'$'}productIDs: [String]!, ${'$'}initialBid: Float, ${'$'}finalBid: Float!, ${'$'}dailyBudget: Float, ${'$'}source: String!) {
//              $OPERATION_NAME(productIDs: ${'$'}productIDs, initialBid: ${'$'}initialBid, finalBid: ${'$'}finalBid, dailyBudget: ${'$'}dailyBudget, source: ${'$'}source) {
//                data {
//                  impression {
//                    oldImpression
//                    finalImpression
//                    increment
//                  }
//                }
//                error {
//                  title
//                }
//              }
//            }
//        """.trimIndent()
//
//        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
//
//        override fun getQuery(): String = QUERY
//
//        override fun getTopOperationName(): String = OPERATION_NAME
//    }

}

package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.PARAM_PRODUCT_IDS
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_DAILYBUDGET
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_FINAL_BID
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_INITIAL_BID
import com.tokopedia.topads.common.data.response.ImpressionPredictionResponse
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
        requestParams.putObject(PARAM_FINAL_BID, finalBid)
        requestParams.putObject(PARAM_INITIAL_BID, initialBid)
        requestParams.putObject(PARAM_DAILYBUDGET, dailyBudget)
        requestParams.putObject(PARAM_PRODUCT_IDS, productIds)
        requestParams.putString(ParamObject.SOURCE, source)
        return requestParams
    }
}

package com.tokopedia.topads.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.data.TopAdsGetBidSuggestionResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

private const val GET_PRODUCT_BID_QUERY = """query topAdsGetBidSuggestionByProductIDs(${'$'}productIDs: [String]!, ${'$'}adGroupType: String!, ${'$'}source: String!) {
  topAdsGetBidSuggestionByProductIDs(productIDs: ${'$'}productIDs, adGroupType: ${'$'}adGroupType, source: ${'$'}source) {
    data {
      bidSuggestion
    }
    error {
      code
      detail
      title
    }
  }
}"""

@GqlQuery("GetProductBid", GET_PRODUCT_BID_QUERY)
class TopAdsGetBidSuggestionByProductIDsUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<TopAdsGetBidSuggestionResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GET_PRODUCT_BID_QUERY)
        setTypeClass(TopAdsGetBidSuggestionResponse::class.java)
    }

    suspend operator fun invoke(source: String, productIds: List<String>): Result<TopAdsGetBidSuggestionResponse> {
        setRequestParams(createRequestParam(source, productIds).parameters)
        val data = executeOnBackground()

        return when {
            data.topAdsGetBidSuggestionByProductIDs.error.title.isEmpty() -> {
                Success(data)
            }

            else -> Fail(Throwable(data.topAdsGetBidSuggestionByProductIDs.error.title))
        }
    }

    private fun createRequestParam(source: String, productIds: List<String>): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString("adGroupType", "product")
        requestParams.putObject("productIDs", productIds)
        requestParams.putString(ParamObject.SOURCE, source)
        return requestParams
    }
}

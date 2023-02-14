package com.tokopedia.manageaddress.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.manageaddress.domain.request.shareaddress.GetTargetedTickerParam
import com.tokopedia.manageaddress.domain.response.GetTargetedTickerResponse
import javax.inject.Inject

class GetTargetedTickerUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetTargetedTickerParam, GetTargetedTickerResponse>(dispatcher.io) {

    @GqlQuery(
        QUERY_GET_TARGETED_TICKER_NAME,
        QUERY_GET_TARGETED_TICKER
    )
    override suspend fun execute(params: GetTargetedTickerParam): GetTargetedTickerResponse {
        return repository.request(GetTargetedTicker(), params)
    }

    override fun graphqlQuery(): String {
        return QUERY_GET_TARGETED_TICKER
    }

    companion object {
        private const val QUERY_GET_TARGETED_TICKER_NAME = "GetTargetedTicker"
        private const val QUERY_GET_TARGETED_TICKER =
            """query GetTargetedTicker(${'$'}input: GetTargetedTickerRequest!) {
  GetTargetedTicker(input: ${'$'}input) {
    List {
      Action {
        Type
        AppURL
        Label
        WebURL
      }
      Type
      Content
      Priority
      Metadata {
        Type
        Values
      }
      Title
      ID
    }
  }
}
"""
    }
}

package com.tokopedia.shopdiscount.info.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.info.data.request.GetSlashPriceTickerRequest
import com.tokopedia.shopdiscount.info.data.response.GetSlashPriceTickerResponse
import javax.inject.Inject

class GetSlashPriceTickerUseCase @Inject constructor(
    repository: GraphqlRepository
)  : GraphqlUseCase<GetSlashPriceTickerResponse>(repository) {

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
        setGraphqlQuery(GetSlashPriceTickerQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetSlashPriceTickerResponse::class.java)
    }

    fun setParams(request: GetSlashPriceTickerRequest) {
        setRequestParams(
            mapOf<String, Any>(
                KEY_PARAMS to request
            )
        )
    }

    companion object {
        private const val KEY_PARAMS = "params"
        private const val QUERY_NAME = "GetSlashPriceTickerQuery"
        private const val QUERY = """
            query GetSlashPriceTicker(${'$'}params: GetSlashPriceTickerRequest!) {
              GetSlashPriceTicker(params: ${'$'}params) {
                response_header {
                  status
                  success
                  process_time
                  reason
                  error_code
                }
                tickers
              }
            }
        """
    }

}
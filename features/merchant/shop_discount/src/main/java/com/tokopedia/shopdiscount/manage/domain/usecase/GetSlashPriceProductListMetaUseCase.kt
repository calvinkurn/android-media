package com.tokopedia.shopdiscount.manage.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.manage.data.response.GetSlashPriceProductListMetaResponse
import com.tokopedia.shopdiscount.utils.constant.CAMPAIGN
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import javax.inject.Inject

class GetSlashPriceProductListMetaUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<GetSlashPriceProductListMetaResponse>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val REQUEST_QUERY_NAME = "GetSlashPriceProductListMeta"
        private const val REQUEST_QUERY = """
            query GetSlashPriceProductListMeta(${'$'}params: GetSlashPriceProductListMetaRequest)  {
              GetSlashPriceProductListMeta(params: ${'$'}params){
                response_header {
                  status
                  error_message
                  success
                  process_time
                  reason
                  error_code
                }
                data {
                  tab{
                    id
                    name
                    value
                  }
                }
              }
            }
        """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(
        REQUEST_QUERY_NAME,
        REQUEST_QUERY
    )

    private fun setupUseCase() {
        setGraphqlQuery(GetSlashPriceProductListMeta())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetSlashPriceProductListMetaResponse::class.java)
    }

    fun setRequestParams(
        source: String = CAMPAIGN,
        ip: String = EMPTY_STRING,
        useCase: String = EMPTY_STRING
    ) {
        val request = RequestHeader(source, ip, useCase)
        val params = mapOf(REQUEST_PARAM_KEY to request)
        setRequestParams(params)
    }


}
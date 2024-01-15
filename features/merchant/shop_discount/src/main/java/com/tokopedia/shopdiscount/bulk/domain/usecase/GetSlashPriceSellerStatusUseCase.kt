package com.tokopedia.shopdiscount.bulk.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceSellerStatusResponse
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.utils.constant.CAMPAIGN
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import javax.inject.Inject

class GetSlashPriceSellerStatusUseCase @Inject constructor() : GraphqlUseCase<GetSlashPriceSellerStatusResponse>() {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val REQUEST_QUERY_NAME = "GetSlashPriceSellerStatus"
        private const val REQUEST_QUERY = """
            query GetSlashPriceSellerStatusQuery(${'$'}params: GetSlashPriceSellerStatusRequest!) {
              getSlashPriceSellerStatus(params: ${'$'}params) {
                response_header {
                  status
                  success
                  process_time
                  reason
                  error_code
                  error_message
                }
              }
            }
        """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(REQUEST_QUERY_NAME, REQUEST_QUERY)
    private fun setupUseCase() {
        setGraphqlQuery(GetSlashPriceSellerStatus())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetSlashPriceSellerStatusResponse::class.java)
    }

    fun setParams(
        source: String = CAMPAIGN,
        ip: String = EMPTY_STRING,
        useCase: String = EMPTY_STRING
    ) {
        val requestHeader = RequestHeader(source, ip, useCase)
        val params = mapOf(REQUEST_PARAM_KEY to requestHeader)
        setRequestParams(params)
    }

}

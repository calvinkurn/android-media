package com.tokopedia.shopdiscount.bulk.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.utils.constant.CAMPAIGN
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import javax.inject.Inject

class GetSlashPriceBenefitUseCase @Inject constructor() : GraphqlUseCase<GetSlashPriceBenefitResponse>() {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val REQUEST_QUERY_NAME = "GetSlashPriceBenefit"
        private const val REQUEST_QUERY = """
            query GetSlashPriceBenefit(${'$'}params: GetSlashPriceBenefitRequest) {
                GetSlashPriceBenefit(params: ${'$'}params) {
                   response_header {
                       status
                       success
                       process_time
                       reason
                       error_code
                   }
                   is_use_vps
                   slash_price_benefits {
                       package_id
                       package_name
                       shop_tier
                       shop_grade
                       remaining_quota
                       max_quota
                       expired_at
                       expired_at_unix
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
        setGraphqlQuery(GetSlashPriceBenefit())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetSlashPriceBenefitResponse::class.java)
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
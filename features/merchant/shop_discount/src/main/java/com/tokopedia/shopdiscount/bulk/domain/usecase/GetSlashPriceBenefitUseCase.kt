package com.tokopedia.shopdiscount.bulk.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shopdiscount.bulk.data.request.GetSlashPriceBenefitRequest
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.utils.constant.CAMPAIGN
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetSlashPriceBenefitUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : UseCase<GetSlashPriceBenefitResponse>(), GqlQueryInterface {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
    }

    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return """
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
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return EMPTY_STRING
    }

    private var params = emptyMap<String, Any>()

    suspend fun execute(source: String = CAMPAIGN, ip: String = "", useCase: String = ""): GetSlashPriceBenefitResponse {
        val request = GetSlashPriceBenefitRequest(source, ip, useCase)
        params = mapOf(REQUEST_PARAM_KEY to request)
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): GetSlashPriceBenefitResponse {
        val request = GraphqlRequest(this, GetSlashPriceBenefitResponse::class.java, params, true)
        return repository.response(listOf(request)).getSuccessData()
    }


}
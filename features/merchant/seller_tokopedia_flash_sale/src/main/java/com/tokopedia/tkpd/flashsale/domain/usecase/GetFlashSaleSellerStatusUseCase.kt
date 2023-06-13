package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.data.mapper.GetFlashSaleSellerStatusMapper
import com.tokopedia.tkpd.flashsale.data.request.CampaignParticipationRequestHeader
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleSellerStatusRequest
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleSellerStatusResponse
import com.tokopedia.tkpd.flashsale.domain.entity.SellerEligibility
import javax.inject.Inject


class GetFlashSaleSellerStatusUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetFlashSaleSellerStatusMapper
) : GraphqlUseCase<SellerEligibility>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
    }

    private val query = object : GqlQueryInterface {

        private val OPERATION_NAME = "getFlashSaleSellerStatus"
        private val QUERY = """
        query $OPERATION_NAME(${'$'}params: GetFlashSaleSellerStatusRequest!) {
             $OPERATION_NAME(params: ${'$'}params) {
                response_header {
                  status
                  success
                  process_time
                  error_code
                }
                admin_access {
                  manage_flash_sale {
                    is_device_allowed
                    is_user_allowed
                  }
                }
             }
       }

    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(): SellerEligibility {
        val request = buildRequest()
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetFlashSaleSellerStatusResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(): GraphqlRequest {
        val requestHeader = CampaignParticipationRequestHeader(usecase = "campaign_list")
        val payload = GetFlashSaleSellerStatusRequest(requestHeader)
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            query,
            GetFlashSaleSellerStatusResponse::class.java,
            params
        )
    }

}
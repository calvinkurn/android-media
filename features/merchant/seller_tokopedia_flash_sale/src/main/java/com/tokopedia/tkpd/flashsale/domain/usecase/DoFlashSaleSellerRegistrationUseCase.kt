package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.data.mapper.DoFlashSaleSellerRegistrationMapper
import com.tokopedia.tkpd.flashsale.data.request.CampaignParticipationRequestHeader
import com.tokopedia.tkpd.flashsale.data.request.DoFlashSaleSellerRegistrationRequest
import com.tokopedia.tkpd.flashsale.data.response.DoFlashSaleProductRegistrationResponse
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleRegistrationResult
import javax.inject.Inject

class DoFlashSaleSellerRegistrationUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: DoFlashSaleSellerRegistrationMapper
) : GraphqlUseCase<FlashSaleRegistrationResult>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
    }

    private val mutation = object : GqlQueryInterface {
        private val OPERATION_NAME = "doFlashSaleSellerRegistration"
        private val MUTATION = """
        mutation $OPERATION_NAME(${'$'}params: DoFlashSaleSellerRegistrationRequest!) {
             $OPERATION_NAME(params: ${'$'}params) {
                response_header {
                    status
                    success
                    error_message
                    error_code
                }
             }
       }
    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = MUTATION
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(campaignId: Long): FlashSaleRegistrationResult {
        val request = buildRequest(campaignId)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<DoFlashSaleProductRegistrationResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(campaignId: Long): GraphqlRequest {
        val requestHeader = CampaignParticipationRequestHeader(usecase = "manage_product")
        val payload = DoFlashSaleSellerRegistrationRequest(
            requestHeader,
            campaignId
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            mutation,
            DoFlashSaleProductRegistrationResponse::class.java,
            params
        )
    }
}
package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.data.mapper.DoFlashSaleProductDeleteMapper
import com.tokopedia.tkpd.flashsale.data.request.CampaignParticipationRequestHeader
import com.tokopedia.tkpd.flashsale.data.request.DoFlashSaleProductDeleteRequest
import com.tokopedia.tkpd.flashsale.data.response.DoFlashSaleProductDeleteResponse
import com.tokopedia.tkpd.flashsale.domain.entity.ProductDeleteResult
import javax.inject.Inject


class DoFlashSaleProductDeleteUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: DoFlashSaleProductDeleteMapper
) : GraphqlUseCase<ProductDeleteResult>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
    }

    private val mutation = object : GqlQueryInterface {
        private val OPERATION_NAME = "doFlashSaleProductDelete"
        private val MUTATION = """
        mutation $OPERATION_NAME(${'$'}params: DoFlashSaleProductDeleteRequest!) {
             $OPERATION_NAME(params: ${'$'}params) {
                response_header {
                    status
                    success
                    error_message
                    error_code
                }
                product_status {
                    product_id
                    is_success
                    message
                }
             }
       }
    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = MUTATION
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): ProductDeleteResult {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<DoFlashSaleProductDeleteResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val requestHeader = CampaignParticipationRequestHeader(usecase = "manage_product")
        val payload = DoFlashSaleProductDeleteRequest(
            requestHeader,
            param.campaignId,
            param.productIds,
            param.reservationId
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            mutation,
            DoFlashSaleProductDeleteResponse::class.java,
            params
        )
    }

    data class Param(
        val campaignId: Long,
        val productIds: List<Long>,
        val reservationId: String = ""
    )
}

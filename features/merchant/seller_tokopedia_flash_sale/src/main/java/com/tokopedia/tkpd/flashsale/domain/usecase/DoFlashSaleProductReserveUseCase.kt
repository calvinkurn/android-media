package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.data.request.DoFlashSaleProductReserveRequest
import com.tokopedia.tkpd.flashsale.data.response.DoFlashSaleProductReserveResponse
import com.tokopedia.tkpd.flashsale.domain.entity.ProductReserveResult
import javax.inject.Inject

class DoFlashSaleProductReserveUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<ProductReserveResult>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val ACTION_ADD = 1
    }

    private val mutation = object : GqlQueryInterface {
        private val OPERATION_NAME = "doFlashSaleProductReserve"
        private val MUTATION = """
            mutation doFlashSaleProductReserve(${'$'}params: DoFlashSaleProductReserveRequest!) {
              doFlashSaleProductReserve(params: ${'$'}params) {
                response_header {
                  status
                  error_message
                  success
                  process_time
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

    suspend fun execute(param: Param): ProductReserveResult {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<DoFlashSaleProductReserveResponse>()
        return mapToResult(data.doFlashSaleProductReserve.responseHeader, param.reservationId)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val payload = DoFlashSaleProductReserveRequest(
            action = ACTION_ADD,
            campaignId = param.campaignId,
            reservationId = param.reservationId,
            productData = param.productData.map {
                DoFlashSaleProductReserveRequest.ProductData(it.first, it.second)
            }
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            mutation,
            DoFlashSaleProductReserveResponse::class.java,
            params
        )
    }

    private fun mapToResult(
        responseHeader: DoFlashSaleProductReserveResponse.ResponseHeader,
        reservationId: String
    ) =
        with(responseHeader) {
            ProductReserveResult(success, errorMessage.joinToString("\n"), reservationId)
        }

    data class Param(
        val campaignId: Long,
        val reservationId: String,
        val productData: List<Pair<Long, Long>>
    )
}
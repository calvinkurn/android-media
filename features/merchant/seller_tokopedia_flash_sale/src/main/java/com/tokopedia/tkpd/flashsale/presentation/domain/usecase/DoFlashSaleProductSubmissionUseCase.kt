package com.tokopedia.tkpd.flashsale.presentation.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.presentation.data.mapper.GetFlashSaleListForSellerMetaMapper
import com.tokopedia.tkpd.flashsale.presentation.data.request.CampaignParticipationRequestHeader
import com.tokopedia.tkpd.flashsale.presentation.data.request.DoFlashSaleProductSubmissionRequest
import com.tokopedia.tkpd.flashsale.presentation.data.response.GetFlashSaleListForSellerMetaResponse
import com.tokopedia.tkpd.flashsale.presentation.domain.entity.TabMetadata
import javax.inject.Inject


class DoFlashSaleProductSubmissionUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetFlashSaleListForSellerMetaMapper
) : GraphqlUseCase<List<TabMetadata>>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
    }

    private val mutation = object : GqlQueryInterface {
        private val OPERATION_NAME = "doFlashSaleProductSubmission"
        private val MUTATION = """
        mutation $OPERATION_NAME(${'$'}params: DoFlashSaleProductSubmissionRequest!) {
             $OPERATION_NAME(params: ${'$'}params) {
                response_header {
                    status
                    success
                    error_message
                    process_time
                    error_code
                  
                }
                product_status {
                    product_id
                    is_success
                    message
                    warehouses {
                        warehouse_id
                        is_success
                        message
                    }
                }
             }
       }
    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = MUTATION
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): List<TabMetadata> {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetFlashSaleListForSellerMetaResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val requestHeader = CampaignParticipationRequestHeader(usecase = "campaign_list")
        val payload = DoFlashSaleProductSubmissionRequest(
            requestHeader,
            param.campaignId,
            param.productData,
            param.reservationId
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            mutation,
            GetFlashSaleListForSellerMetaResponse::class.java,
            params
        )
    }

    data class Param(
        val campaignId: Long,
        val productData: List<DoFlashSaleProductSubmissionRequest.ProductData>,
        val reservationId: String
    )

}
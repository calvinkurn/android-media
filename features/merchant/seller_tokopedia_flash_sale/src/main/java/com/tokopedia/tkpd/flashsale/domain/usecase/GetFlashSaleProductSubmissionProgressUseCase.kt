package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.data.mapper.GetFlashSaleProductSubmissionProgressMapper
import com.tokopedia.tkpd.flashsale.data.request.CampaignParticipationRequestHeader
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleProductSubmissionProgressRequest
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleProductSubmissionProgressResponse
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSaleProductSubmissionProgress
import javax.inject.Inject

class GetFlashSaleProductSubmissionProgressUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetFlashSaleProductSubmissionProgressMapper
) : GraphqlUseCase<FlashSaleProductSubmissionProgress>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val MAX_PRODUCT_PER_PAGE = 10
    }

    private val mutation = object : GqlQueryInterface {
        private val OPERATION_NAME = "getFlashSaleProductSubmissionProgress"
        private val GET = """
        query $OPERATION_NAME(${'$'}params: GetFlashSaleProductSubmissionProgressRequest!) {
             $OPERATION_NAME(params: ${'$'}params) {
                response_header {
                    status
                    success
                    error_message
                    process_time
                    error_code
                }
                type
                campaign_list {
                    campaign_id
                    campaign_name
                    product_processed
                    product_submitted
                    campaign_picture
                }
                campaign_product_error_list{
                    product_id
                    product_name
                    sku
                    product_picture
                    message
                    error_type
                }
                open_sse
                list_product_has_next
             }
       }
    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = GET
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): FlashSaleProductSubmissionProgress {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetFlashSaleProductSubmissionProgressResponse>()
        return mapper.map(data.getFlashSaleProductSubmissionProgress)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val requestHeader = CampaignParticipationRequestHeader(usecase = "manage_product")
        val payload = GetFlashSaleProductSubmissionProgressRequest(
            requestHeader,
            param.campaignId.toLongOrZero(),
            MAX_PRODUCT_PER_PAGE,
            param.offset,
            param.checkProgress
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            mutation,
            GetFlashSaleProductSubmissionProgressResponse::class.java,
            params
        )
    }

    data class Param(val campaignId: String = "", val offset: Int = 0, val checkProgress: Boolean)

}

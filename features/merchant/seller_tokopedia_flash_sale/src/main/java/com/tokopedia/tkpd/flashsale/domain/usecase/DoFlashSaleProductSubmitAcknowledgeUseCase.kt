package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.data.request.CampaignParticipationRequestHeader
import com.tokopedia.tkpd.flashsale.data.request.DoFlashSaleProductSubmitAcknowledgeRequest
import com.tokopedia.tkpd.flashsale.data.response.DoFlashSaleProductSubmitAcknowledgeResponse
import javax.inject.Inject


class DoFlashSaleProductSubmitAcknowledgeUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<Boolean>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
    }

    private val mutation = object : GqlQueryInterface {
        private val OPERATION_NAME = "doFlashSaleProductSubmitAcknowledge"
        private val MUTATION = """
        mutation $OPERATION_NAME(${'$'}params: DoFlashSaleProductSubmitAcknowledgeRequest!) {
             $OPERATION_NAME(params: ${'$'}params) {
                response_header {
                    status
                    success
                    error_message
                    process_time
                    error_code
                  
                }
                is_success
             }
       }
    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = MUTATION
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): Boolean {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<DoFlashSaleProductSubmitAcknowledgeResponse>()
        return data.doFlashSaleProductSubmitAcknowledge.isSuccess
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val requestHeader = CampaignParticipationRequestHeader(usecase = "manage_product")
        val payload = DoFlashSaleProductSubmitAcknowledgeRequest(
            requestHeader,
            param.campaignId
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            mutation,
            DoFlashSaleProductSubmitAcknowledgeResponse::class.java,
            params
        )
    }

    data class Param(val campaignId: Long)

}

package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.data.mapper.GetFlashSaleListForSellerMetaMapper
import com.tokopedia.tkpd.flashsale.data.request.CampaignParticipationRequestHeader
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleListForSellerMetaRequest
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleListForSellerMetaResponse
import com.tokopedia.tkpd.flashsale.domain.entity.TabMetadata
import javax.inject.Inject


class GetFlashSaleListForSellerMetaUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetFlashSaleListForSellerMetaMapper
) : GraphqlUseCase<TabMetadata>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "getFlashSaleListForSellerMeta"
        private val QUERY = """
        query $OPERATION_NAME(${'$'}params: GetFlashSaleListForSellerMeta!) {
             $OPERATION_NAME(params: ${'$'}params) {
                   tab_list {
                     tab_id
                     tab_name
                     total_campaign
                     display_name
                   }
             }
       }

    """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(): TabMetadata {
        val request = buildRequest()
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetFlashSaleListForSellerMetaResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(): GraphqlRequest {
        val requestHeader = CampaignParticipationRequestHeader(usecase = "campaign_list")
        val payload = GetFlashSaleListForSellerMetaRequest(requestHeader)
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            query,
            GetFlashSaleListForSellerMetaResponse::class.java,
            params
        )
    }

}

package com.tokopedia.tkpd.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tkpd.flashsale.data.mapper.GetFlashSaleProductPerCriteriaMapper
import com.tokopedia.tkpd.flashsale.data.request.CampaignParticipationRequestHeader
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleProductListToReserveRequest
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleProductPerCriteriaResponse
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaSelection
import javax.inject.Inject

class GetFlashSaleProductPerCriteriaUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetFlashSaleProductPerCriteriaMapper
) : GraphqlUseCase<List<CriteriaSelection>>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "getFlashSaleProductPerCriteria"
        private val QUERY = """
            query $OPERATION_NAME(${'$'}params: GetFlashSaleProductPerCriteriaRequest!) {
              $OPERATION_NAME(params: ${'$'}params) {
                product_criteria {
                  criteria_id
                  criteria_name
                  max_submission
                  count_submitted
                  count_remaining
                  category_list {
                    category_id
                    category_name
                  }
                }
              }
            }
        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(campaignId: Long): List<CriteriaSelection> {
        val request = buildRequest(campaignId)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetFlashSaleProductPerCriteriaResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(campaignId: Long): GraphqlRequest {
        val requestHeader = CampaignParticipationRequestHeader(usecase = "campaign_list")
        val payload = GetFlashSaleProductListToReserveRequest(
            requestHeader = requestHeader,
            campaignId = campaignId
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            query,
            GetFlashSaleProductPerCriteriaResponse::class.java,
            params
        )
    }

}
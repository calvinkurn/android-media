package com.tokopedia.shop.flash_sale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flash_sale.data.request.CampaignCancellationRequest
import com.tokopedia.shop.flash_sale.domain.entity.CampaignCancellation.CampaignCancellationResponse
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@GqlQuery(
    GetSellerCampaignCancellationListUseCase.QUERY_NAME,
    GetSellerCampaignCancellationListUseCase.QUERY
)
class GetSellerCampaignCancellationListUseCase @Inject constructor(
    private val repository: GraphqlRepository
): GraphqlUseCase<CampaignCancellationResponse>(repository) {

    companion object {
        private const val CAMPAIGN_TYPE_SHOP_FLASH_SALE = 0
        private const val REQUEST_PARAM_KEY = "params"
        private const val EXPIRED_CACHE_DAYS = 1L
        const val QUERY_NAME = "GetSellerCampaignCancellationListQuery"
        const val QUERY = """
            query GetSellerCampaignCancellationList(${'$'}params: GetSellerCampaignCancellationListRequest!)  {
              getSellerCampaignCancellationList(params: ${'$'}params) {
                cancellation_reason
              }
            }
        """
    }

    init {
        setGraphqlQuery(GetSellerCampaignCancellationListQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).setExpiryTime(
            TimeUnit.DAYS.toMillis(EXPIRED_CACHE_DAYS)).build())
    }

    suspend fun execute(): List<String> {
        val request = buildRequest()
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<CampaignCancellationResponse>()
        return data.cancellationList.cancellationList
    }

    private fun buildRequest(): GraphqlRequest {
        val payload = CampaignCancellationRequest(CAMPAIGN_TYPE_SHOP_FLASH_SALE)
        val params = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            GetSellerCampaignCancellationListQuery(),
            CampaignCancellationResponse::class.java,
            params
        )
    }
}
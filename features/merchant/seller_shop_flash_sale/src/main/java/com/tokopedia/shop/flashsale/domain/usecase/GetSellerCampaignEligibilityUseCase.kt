package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignEligibilityRequest
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignEligibilityResponse
import javax.inject.Inject

class GetSellerCampaignEligibilityUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<Boolean>(repository) {

    companion object {
        private const val CAMPAIGN_TYPE_SHOP_FLASH_SALE = 0
        private const val REQUEST_PARAM_KEY = "params"
        private const val QUERY_NAME = "GetSellerCampaignEligibility"
        private const val QUERY = """
            query GetSellerCampaignEligibility(${'$'}params: GetSellerCampaignEligibilityRequest!)  {
                getSellerCampaignEligibility(params: ${'$'}params){
                    is_eligible
                }
            }
        """
    }


    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    suspend fun execute(): Boolean {
        val request = buildRequest()
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetSellerCampaignEligibilityResponse>()
        return data.campaignEligibility.isEligible
    }

    private fun buildRequest(): GraphqlRequest {
        val payload = GetSellerCampaignEligibilityRequest(CAMPAIGN_TYPE_SHOP_FLASH_SALE)
        val params = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            GetSellerCampaignEligibility(),
            GetSellerCampaignEligibilityResponse::class.java,
            params
        )
    }
}
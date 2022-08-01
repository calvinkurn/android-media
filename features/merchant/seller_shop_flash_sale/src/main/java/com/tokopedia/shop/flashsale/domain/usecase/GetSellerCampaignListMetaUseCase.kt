package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flashsale.data.mapper.SellerCampaignListMetaMapper
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignListMetaRequest
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignListMetaResponse
import com.tokopedia.shop.flashsale.domain.entity.TabMeta
import javax.inject.Inject


class GetSellerCampaignListMetaUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: SellerCampaignListMetaMapper
) : GraphqlUseCase<List<TabMeta>>(repository) {

    companion object {
        private const val CAMPAIGN_TYPE_SHOP_FLASH_SALE = 0
        private const val REQUEST_PARAM_KEY = "params"
        private const val QUERY_NAME = "GetSellerCampaignListMeta"
        private const val QUERY = """
            query GetSellerCampaignListMeta(${'$'}params: GetSellerCampaignListMetaRequest!)  {
              getSellerCampaignListMeta(params: ${'$'}params){
                response_header {
                  status
                  success
                }
                tab {
                  id
                  total_campaign
                  name
                  status
                }
                thematic_participation
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

    suspend fun execute(sellerCampaignType: Int = CAMPAIGN_TYPE_SHOP_FLASH_SALE): List<TabMeta> {
        val request = buildRequest(sellerCampaignType)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetSellerCampaignListMetaResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(sellerCampaignType: Int): GraphqlRequest {
        val payload = GetSellerCampaignListMetaRequest(sellerCampaignType)
        val params = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            GetSellerCampaignListMeta(),
            GetSellerCampaignListMetaResponse::class.java,
            params
        )
    }
}
package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flashsale.data.mapper.HighlightedProductMapper
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignHighlightProductsRequest
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignHighlightProductsResponse
import com.tokopedia.shop.flashsale.domain.entity.HighlightedProduct
import javax.inject.Inject


class GetSellerCampaignHighlightProductsUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val highlightedProductMapper: HighlightedProductMapper
): GraphqlUseCase<List<HighlightedProduct>>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val QUERY_NAME = "GetSellerCampaignHighlightProducts"
        private const val QUERY = """
            query GetSellerCampaignHighlightProducts(${'$'}params: GetSellerCampaignHighlightProductsRequest!)  {
              getSellerCampaignHighlightProducts(params: ${'$'}params) {
                   highlight_product_datas {
                      ID
                      name
                      URL
                      ImageURL
                      Price
                      OriginalPrice
                      DiscountedPrice
                      DiscountedPercentage
                      CampaignStatus
                      StartDate
                      EndDate
                   }
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

    suspend fun execute(campaignId: Long): List<HighlightedProduct> {
        val payload = GetSellerCampaignHighlightProductsRequest(campaignId)
        val request = buildRequest(payload)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetSellerCampaignHighlightProductsResponse>()
        return highlightedProductMapper.map(data)
    }

    private fun buildRequest(payload: GetSellerCampaignHighlightProductsRequest): GraphqlRequest {
        val params = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            GetSellerCampaignHighlightProducts(),
            GetSellerCampaignHighlightProductsResponse::class.java,
            params
        )
    }
}
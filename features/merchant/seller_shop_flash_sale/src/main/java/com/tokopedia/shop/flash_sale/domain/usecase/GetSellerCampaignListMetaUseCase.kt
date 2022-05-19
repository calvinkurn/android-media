package com.tokopedia.shop.flash_sale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flash_sale.data.mapper.SellerCampaignListMetaMapper
import com.tokopedia.shop.flash_sale.data.request.GetSellerProductListMetaRequest
import com.tokopedia.shop.flash_sale.data.response.GetSellerProductListMetaResponse
import com.tokopedia.shop.flash_sale.domain.entity.ProductListMeta
import javax.inject.Inject


class GetSellerCampaignListMetaUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: SellerCampaignListMetaMapper
) : GraphqlUseCase<List<ProductListMeta>>(repository) {

    companion object {
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

    suspend fun execute(sellerCampaignType: Int): List<ProductListMeta> {
        val requestParams = buildRequest(sellerCampaignType)
        val request = GraphqlRequest(
            GetSellerCampaignListMeta(),
            GetSellerProductListMetaResponse::class.java,
            requestParams
        )

        val response = repository.response(listOf(request))
        val errors = response.getError(GetSellerProductListMetaResponse::class.java)
        return if (errors.isNullOrEmpty()) {
            val data = response.getSuccessData<GetSellerProductListMetaResponse>()
            mapper.map(data)
        } else {
             throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    private fun buildRequest(sellerCampaignType: Int): Map<String, Any> {
        val payload = GetSellerProductListMetaRequest(sellerCampaignType)
        return mapOf(REQUEST_PARAM_KEY to payload)
    }
}
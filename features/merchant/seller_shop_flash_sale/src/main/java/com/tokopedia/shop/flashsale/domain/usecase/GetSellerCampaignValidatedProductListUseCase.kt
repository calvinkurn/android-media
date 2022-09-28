package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.shop.flashsale.common.constant.ChooseProductConstant.PRODUCT_LIST_SIZE
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignValidatedProductListRequest
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignValidatedProductListResponse
import javax.inject.Inject


class GetSellerCampaignValidatedProductListUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<List<GetSellerCampaignValidatedProductListResponse.Product>>(repository) {

    companion object {
        private const val CAMPAIGN_TYPE_SHOP_FLASH_SALE = 0
        private const val REQUEST_PARAM_KEY = "params"
        private const val QUERY_NAME = "GetSellerCampaignValidatedProductListQuery"
        private const val QUERY = """
            query getSellerCampaignValidatedProductList(${'$'}params: GetSellerCampaignValidatedProductListRequest!) {
              getSellerCampaignValidatedProductList(params: ${'$'}params) {
                response_header {
                  status
                  errorMessage
                  success
                  processTime
                }
                products {
                  product_id
                  product_name
                  price
                  formatted_price
                  product_url
                  sku
                  status
                  pictures {
                    url_thumbnail
                  }
                  disabled
                  disabled_reason
                  transaction_statistics {
                    sold
                  }
                  stock
                  variant_childs_ids
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

    suspend fun execute(
        campaignId: String,
        keyword: String,
        page: Int
    ): List<GetSellerCampaignValidatedProductListResponse.Product> {
        val request = buildRequest(campaignId, keyword, page)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetSellerCampaignValidatedProductListResponse>()
        return data.response.products
    }

    private fun buildRequest(campaignId: String, keyword:String, page: Int): GraphqlRequest {
        val payload = GetSellerCampaignValidatedProductListRequest(
            campaignType = CAMPAIGN_TYPE_SHOP_FLASH_SALE,
            campaignId = campaignId.toLongOrNull().orZero(),
            filter = GetSellerCampaignValidatedProductListRequest.Filter(
                page = page,
                pageSize = PRODUCT_LIST_SIZE,
                keyword = keyword
            )
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            GetSellerCampaignValidatedProductListQuery(),
            GetSellerCampaignValidatedProductListResponse::class.java,
            params
        )
    }
}
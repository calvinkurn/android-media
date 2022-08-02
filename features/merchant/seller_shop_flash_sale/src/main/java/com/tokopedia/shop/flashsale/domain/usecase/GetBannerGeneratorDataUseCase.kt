package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flashsale.common.constant.Constant.ZERO
import com.tokopedia.shop.flashsale.data.mapper.BannerGeneratorDataMapper
import com.tokopedia.shop.flashsale.data.request.BannerGeneratorDataRequest
import com.tokopedia.shop.flashsale.data.response.BannerGeneratorDataResponse
import com.tokopedia.shop.flashsale.domain.entity.CampaignBanner
import javax.inject.Inject


class GetBannerGeneratorDataUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: BannerGeneratorDataMapper
) : GraphqlUseCase<CampaignBanner>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val QUERY_NAME = "GetMerchantCampaignBannerGeneratorData"
        private const val QUERY = """
            query GetMerchantCampaignBannerGeneratorData(${'$'}params: GetMerchantCampaignBannerGeneratorDataRequest!)  {
              getMerchantCampaignBannerGeneratorData(params: ${'$'}params){
                campaign {
                  campaign_id
                  name
                  start_date
                  end_date
                  review_end_date
                  status_text
                  discount_percentage_text
                  highlight_products {
                    Products {
                      ID
                      name
                      URL
                      imageUrl
                      campaign {
                        DiscountPercentage
                        DiscountedPrice
                        OriginalPrice
                      }
                    }
                    total_product
                    total_product_wording
                  }
                  max_discount_percentage
                  total_product
                  total_product_overload
                  status_id
                }
                shop_data {
                  ShopID
                  Name
                  Domain
                  URL
                  URLMobile
                  URLApps
                  IsGold
                  IsOfficial
                  Logo
                  City
                  Badge {
                    Title
                    ImageURL
                  }
                }
                formatted_start_date
                formatted_end_date
                formatted_review_end_date
                formatted_sharing_start_date
                formatted_sharing_end_date
                formatted_sharing_review_end_date
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

    suspend fun execute(campaignId: Long): CampaignBanner {
        val request = buildRequest(campaignId)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<BannerGeneratorDataResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(campaignId: Long): GraphqlRequest {
        val payload = BannerGeneratorDataRequest(campaignId, ZERO)
        val requestParams = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            GetMerchantCampaignBannerGeneratorData(),
            BannerGeneratorDataResponse::class.java,
            requestParams
        )
    }

}
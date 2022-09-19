package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flashsale.data.mapper.SellerCampaignAttributeMapper
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignAttributeRequest
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignAttributeResponse
import com.tokopedia.shop.flashsale.domain.entity.CampaignAttribute
import javax.inject.Inject


class GetSellerCampaignAttributeUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: SellerCampaignAttributeMapper
) : GraphqlUseCase<CampaignAttribute>(repository) {

    companion object {
        private const val CAMPAIGN_TYPE_SHOP_FLASH_SALE = 0
        private const val REQUEST_PARAM_KEY = "params"
        private const val QUERY_NAME = "GetSellerCampaignAttribute"
        private const val QUERY = """
            query GetSellerCampaignAttribute(${'$'}params: GetSellerCampaignAttributeRequest!)  {
              getSellerCampaignAttribute(params: ${'$'}params){
                response_header {
                  status
                  success
                  errorMessage
                }
                total_count
                max_count_allowed
                remaining_campaign_quota
                shop_attribute {
                  campaign_quota
                  max_campaign_duration
                  max_upcoming_duration
                  widget_background_color
                  max_single_product_submission
                  user_relation_restriction
                  max_overlapping_campaign
                  max_etalase
                }
                campaign_detail {
                  campaign_id
                  campaign_name
                  status_id
                  start_date
                  end_date
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
        sellerCampaignType: Int = CAMPAIGN_TYPE_SHOP_FLASH_SALE,
        month: Int,
        year: Int,
        vpsPackageId: Long
    ): CampaignAttribute {
        val request = buildRequest(sellerCampaignType, month, year, vpsPackageId)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetSellerCampaignAttributeResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(
        sellerCampaignType: Int,
        month: Int,
        year: Int,
        vpsPackageId: Long
    ): GraphqlRequest {
        val payload = GetSellerCampaignAttributeRequest(
            sellerCampaignType,
            month,
            year,
            GetSellerCampaignAttributeRequest.Field(
                shopAttribute = true,
                campaignDetail = true,
                vpsAttribute = true
            ),
            vpsPackageId
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            GetSellerCampaignAttribute(),
            GetSellerCampaignAttributeResponse::class.java,
            params
        )
    }
}
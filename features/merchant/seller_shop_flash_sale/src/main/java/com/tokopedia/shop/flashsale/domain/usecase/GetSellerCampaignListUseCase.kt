package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flashsale.common.constant.Constant.EMPTY_STRING
import com.tokopedia.shop.flashsale.common.constant.Constant.ZERO
import com.tokopedia.shop.flashsale.data.mapper.SellerCampaignListMapper
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignListRequest
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignListResponse
import com.tokopedia.shop.flashsale.domain.entity.CampaignMeta
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


class GetSellerCampaignListUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: SellerCampaignListMapper,
    private val userSessionInterface: UserSessionInterface
) : GraphqlUseCase<CampaignMeta>(repository) {

    companion object {
        private const val ORDER_BY_UPDATED_TIME = 5
        private const val ORDER_DESC = 1
        private const val CAMPAIGN_TYPE_SHOP_FLASH_SALE = 0
        private const val REQUEST_PARAM_KEY = "params"
        private const val QUERY_NAME = "GetSellerCampaignList"
        private const val QUERY = """
            query GetSellerCampaignList(${'$'}params: GetSellerCampaignListRequest!)  {
              getSellerCampaignList(params: ${'$'}params){
                response_header {
                  status
                  success
                }
                campaign {
                  campaign_id
                  campaign_name
                  campaign_type_id
                  campaign_type_name
                  status_id
                  status_text
                  status_detail
                  start_date
                  end_date
                  submission_start_date
                  submission_end_date
                  review_start_date
                  review_end_date
                  max_product_submission
                  product_summary {
                    total_item
                    sold_item
                    reserved_product
                    submitted_product
                    deleted_product
                    visible_product_count
                  }
                  etalase_prefix
                  redirect_url
                  redirect_url_app
                  finished_widget_time
                  finished_widget_time_in_mins
                  is_unique_buyer
                  is_campaign_relation
                  is_campaign_rule_submit
                  campaign_relation_data {
                    id
                    name
                  }
                  gradient_color {
                     first_color
                     second_color
                  }
                  is_shareable
                  bitmask_is_set
                  notify_me_count
                  use_upcoming_widget
                  payment_type
                  cover_img
                  thematic_participation
                  is_cancellable
                  thematic_info {
                    thematic_id
                    thematic_sub_id
                    status
                    status_str
                    thematic_name
                  }
                  package_info {
                    package_id
                    package_name
                  }
                }
                total_campaign
                total_campaign_active
                total_campaign_finished
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
        listType: Int = ZERO,
        rows: Int,
        offset: Int,
        statusId: List<Int> = emptyList(),
        campaignId: Long = ZERO.toLong(),
        campaignName: String = EMPTY_STRING
    ): CampaignMeta {
        val request = buildRequest(
            sellerCampaignType,
            listType,
            rows,
            offset,
            statusId,
            campaignId,
            campaignName
        )
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetSellerCampaignListResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(
        sellerCampaignType: Int,
        listType: Int = ZERO,
        rows: Int,
        offset: Int,
        statusId: List<Int> = emptyList(),
        campaignId: Long = ZERO.toLong(),
        campaignName: String = EMPTY_STRING
    ): GraphqlRequest {
        val payload = GetSellerCampaignListRequest(
            userSessionInterface.shopId.toLong(),
            sellerCampaignType,
            listType,
            GetSellerCampaignListRequest.Pagination(rows, offset),
            GetSellerCampaignListRequest.Filter(statusId, campaignId, campaignName),
            GetSellerCampaignListRequest.Sort(ORDER_BY_UPDATED_TIME, ORDER_DESC)
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            GetSellerCampaignList(),
            GetSellerCampaignListResponse::class.java,
            params
        )
    }
}
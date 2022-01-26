package com.tokopedia.play.broadcaster.domain.usecase.campaign

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.play.broadcaster.domain.model.campaign.GetCampaignListResponse
import com.tokopedia.play_common.domain.usecase.RetryableGraphqlUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by meyta.taliti on 25/01/22.
 */
@GqlQuery(GetCampaignListUseCase.QUERY_NAME, GetCampaignListUseCase.QUERY)
class GetCampaignListUseCase @Inject constructor(
    gqlRepository: GraphqlRepository,
    private val dispatchers: CoroutineDispatchers,
) : RetryableGraphqlUseCase<GetCampaignListResponse>(gqlRepository) {

    init {
        setGraphqlQuery(GetCampaignListUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(GetCampaignListResponse::class.java)
    }

    override suspend fun executeOnBackground() = withContext(dispatchers.io) {
        super.executeOnBackground()
    }


 companion object {

     private const val PARAM_SHOP_ID = "shopId"
     private const val PARAM_CAMPAIGN_TYPE = "campaignType"
     private const val PARAM_CAMPAIGN_LIST_TYPE = "campaignListType"
     private const val PARAM_CAMPAIGN_STATUS = "campaignStatus"
     private const val PARAM_ROWS = "rows"
     private const val PARAM_OFFSET = "offset"
     private const val PARAM_ORDER_BY = "orderBy"
     private const val PARAM_ORDER_RULE = "orderRule"

     const val QUERY_NAME = "GetCampaignListUseCaseQuery"
     const val QUERY = """
         query GetCampaignList(
         ${"$$PARAM_SHOP_ID"}: Int64, 
         ${"$$PARAM_CAMPAIGN_TYPE"}: Int, 
         ${"$$PARAM_CAMPAIGN_LIST_TYPE"}: Int, 
         ${"$$PARAM_CAMPAIGN_STATUS"}: [Int], 
         ${"$$PARAM_ROWS"}: Int, 
         ${"$$PARAM_OFFSET"}: Int, 
         ${"$$PARAM_ORDER_BY"}: Int, 
         ${"$$PARAM_ORDER_RULE"}: Int) {
          getSellerCampaignList(params: {
            shop_id: ${"$$PARAM_SHOP_ID"},
            seller_campaign_type: ${"$$PARAM_CAMPAIGN_TYPE"},
            list_type: ${"$$PARAM_CAMPAIGN_LIST_TYPE"},
            pagination: {
              rows: ${"$$PARAM_ROWS"},
              offset: ${"$$PARAM_OFFSET"}
            },
            filter: {
              status_id: ${"$$PARAM_CAMPAIGN_STATUS"}
            },
            sort: {
              order_by: ${"$$PARAM_ORDER_BY"},
              order_rule: ${"$$PARAM_ORDER_RULE"}
            }
          }) {
            response_header {
              status
              success
              processTime
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
              etalase_prefix
              redirect_url
              redirect_url_app
              finished_widget_time
              finished_widget_time_in_mins
              is_unique_buyer
              is_campaign_relation
              is_campaign_rule_submit
              is_shareable
              bitmask_is_set
              notify_me_count
              use_upcoming_widget
              payment_type
              cover_img
            }
            total_campaign
            total_campaign_active
            total_campaign_finished
          }
        }
     """

     // TODO: make sure request nya udh bener, dan hapus param / default value yg gak perlu
     fun createParams(
         shopId: String = "1961630", // https://www.tokopedia.com/voc123, content.prod.automation3+frontendtest@tokopedia.com
         campaignType: Int = 1, // 0: FLASH_SALE_BY_SELLER, 1: RILISAN_SPESIAL
         campaignListType: Int = 1, // params for shop etalase, must be used with listType: 1
         campaignStatus: IntArray = intArrayOf(6, 7, 14), // 6 (Ready), 14 (Ready Locked), 7 (Ongoing)
         rows: Int = 10,
         offset: Int = 0,
         orderBy: Int = 0, // 0: DEFAULT_SORT (by campaign_id), 1: SORT_BY_CAMPAIGN_NAME, 2: SORT_BY_START_DATE, 3: SORT_BY_STATUS_ID, 4: SORT_BY_NPL_STATUS, 5: SORT_BY_UPDATE_TIME
         orderRule: Int = 0 // 0: ASC, 1: DESC
     ): Map<String, Any> {
         return mapOf(
             PARAM_SHOP_ID to shopId,
             PARAM_CAMPAIGN_TYPE to campaignType,
             PARAM_CAMPAIGN_LIST_TYPE to campaignListType,
             PARAM_CAMPAIGN_STATUS to campaignStatus,
             PARAM_ROWS to rows,
             PARAM_OFFSET to offset,
             PARAM_ORDER_BY to orderBy,
             PARAM_ORDER_RULE to orderRule
         )
     }
 }
}
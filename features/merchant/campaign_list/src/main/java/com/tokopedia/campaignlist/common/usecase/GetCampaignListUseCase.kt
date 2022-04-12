package com.tokopedia.campaignlist.common.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.campaignlist.common.data.model.request.GetCampaignListV2Request
import com.tokopedia.campaignlist.common.data.model.response.GetCampaignListV2Response
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetCampaignListUseCase @Inject constructor(
        @ApplicationContext repository: GraphqlRepository
) : GraphqlUseCase<GetCampaignListV2Response>(repository) {

    companion object {

        private const val KEY_PARAMS = "params"
        private const val INTEGER_ZERO = 0
        private const val BOOLEAN_TRUE = true
        const val NPL_CAMPAIGN_TYPE = 73
        const val NPL_LIST_TYPE = 20

        // sorting : tesedia, akan datang, berlangsung
        val statusId = listOf(5, 6, 7, 14)

        private var query = """
            query getCampaignListV2(${'$'}params:GetCampaignListForSellerRequest) {
                getCampaignListV2(params:${'$'}params) {
                   data {
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
                     submission_end_time_diff
                     review_start_date
                     review_end_date
                     min_requirement
                     slug
                     description
                     cover_img
                     dashboard_url
                     product_criteria {
                       Categories {
                         CriteriaCategoryID
                         DepID
                         DepName
                         CategoryLevel1
                         CategoryNameLevel1
                       }
                       MaxStock
                       ExcludePreorder
                       ExcludeBarangBekas
                       ExcludeGrosirPrice
                       MaxSubmission
                       MinOrderQty
                       MinPrice
                       MaxPrice
                       MinStock
                       MinimumDiscount
                       MaximumDiscount
                       MinimumCashback
                       MinimumRating
                       MaximumRating
                       MinCustomStock
                       MaxCustomStock
                       ProductCriteriaID
                     }
                     seller_campaign_info {
                       AcceptedProduct
                       RejectedProduct
                       TotalItem
                       SoldItem
                       OnBookingItem,
                       TotalRevenueSoldItem,
                       TotalRevenueOnBookingItem
                       RegisteredProduct
                       SubmittedProduct
                     }
                     start_date_unix
                     end_date_unix
                     max_product_submission
                     campaign_relation
                     finished_widget_time
                     finished_widget_time_in_mins
                     total_notify
                     campaign_banner {
                       banner_id
                       file_path
                       file_name
                       position
                       device
                       campaign_id
                       redirect_url
                       redirect_url_app
                       banner_name
                       banner_type
                     }
                     remaining_quota
                     is_set
                     cta_link
                   }
                   total_campaign
                   total_campaign_active
                   enable_rules
                   total_campaign_finished
                   use_restriction_engine                
                }
            }
    """.trimIndent()

        @JvmStatic
        fun createParams(campaignName: String, campaignType: Int, listType: Int, statusId: List<Int>): RequestParams {
            return RequestParams.create().apply {
                val params = GetCampaignListV2Request(
                        rows = INTEGER_ZERO,
                        offset = INTEGER_ZERO,
                        is_joined = BOOLEAN_TRUE,
                        campaignType = campaignType,
                        statusId = statusId,
                        listType = listType,
                        campaignName = campaignName
                )
                putObject(KEY_PARAMS, params)
            }
        }
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(GetCampaignListV2Response::class.java)
    }
}
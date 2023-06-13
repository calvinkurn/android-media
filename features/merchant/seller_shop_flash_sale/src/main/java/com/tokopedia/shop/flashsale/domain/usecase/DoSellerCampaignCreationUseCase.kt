package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.localFormatTo
import com.tokopedia.shop.flashsale.common.extension.minuteOnly
import com.tokopedia.shop.flashsale.data.mapper.DoSellerCampaignCreationMapper
import com.tokopedia.shop.flashsale.data.request.DoSellerCampaignCreationRequest
import com.tokopedia.shop.flashsale.data.response.DoSellerCampaignCreationResponse
import com.tokopedia.shop.flashsale.domain.entity.CampaignAction
import com.tokopedia.shop.flashsale.domain.entity.CampaignCreationResult
import com.tokopedia.shop.flashsale.domain.entity.enums.Action
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignType
import com.tokopedia.shop.flashsale.domain.entity.enums.PaymentType
import java.util.Date
import javax.inject.Inject


class DoSellerCampaignCreationUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: DoSellerCampaignCreationMapper
) : GraphqlUseCase<CampaignCreationResult>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val QUERY_NAME = "DoSellerCampaignCreation"
        private const val QUERY = """
            mutation DoSellerCampaignCreation(${'$'}params: DoSellerCampaignCreationRequest!)  {
              doSellerCampaignCreation(params: ${'$'}params){
                is_success
                response_header {
                   errorMessage
                }
                campaign_id
                total_product_failed
                product_failed {
                  product_id
                  product_name
                  sku
                  image_url
                  stock
                  discounted_price
                }
                seller_campaign_error_creation {
                  error_title
                  error_description
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

    suspend fun execute(params: Param): CampaignCreationResult {
        val request = buildRequest(params)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<DoSellerCampaignCreationResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(params: Param): GraphqlRequest {
        val actionId = when (params.action) {
            CampaignAction.Create -> Action.CREATE.id
            is CampaignAction.Submit -> Action.SUBMIT.id
            is CampaignAction.Update -> Action.UPDATE.id
        }

        val campaignId: Long = when (params.action) {
            CampaignAction.Create -> Constant.ZERO.toLong()
            is CampaignAction.Submit -> params.action.campaignId
            is CampaignAction.Update -> params.action.campaignId
        }

        val payload = DoSellerCampaignCreationRequest(
            actionId,
            CampaignType.FLASH_SALE.id,
            campaignId,
            params.campaignName,
            params.scheduledStart.minuteOnly().localFormatTo(DateConstant.DATE_TIME),
            params.scheduledEnd.minuteOnly().localFormatTo(DateConstant.DATE_TIME),
            params.teaserDate.minuteOnly().localFormatTo(DateConstant.DATE_TIME),
            params.campaignRelation,
            params.isCampaignRuleSubmit,
            DoSellerCampaignCreationRequest.GradientColorInput(
                params.firstColor,
                params.secondColor
            ),
            params.showTeaser,
            params.paymentType.id,
            params.packageId.toString()
        )

        val requestParams = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            DoSellerCampaignCreation(),
            DoSellerCampaignCreationResponse::class.java,
            requestParams
        )
    }

    data class Param(
        val action: CampaignAction,
        val campaignName: String,
        val scheduledStart: Date,
        val scheduledEnd: Date,
        val teaserDate: Date,
        val campaignRelation: List<Long> = emptyList(),
        val isCampaignRuleSubmit: Boolean = false,
        val firstColor: String,
        val secondColor: String,
        val showTeaser: Boolean = false,
        val paymentType: PaymentType,
        val packageId: Long
    )

}
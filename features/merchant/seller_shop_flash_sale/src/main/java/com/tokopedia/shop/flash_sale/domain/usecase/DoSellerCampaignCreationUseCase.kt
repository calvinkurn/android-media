package com.tokopedia.shop.flash_sale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flash_sale.common.constant.Constant
import com.tokopedia.shop.flash_sale.common.constant.DateConstant
import com.tokopedia.shop.flash_sale.common.extension.formatTo
import com.tokopedia.shop.flash_sale.common.extension.toCalendar
import com.tokopedia.shop.flash_sale.data.mapper.DoSellerCampaignCreationMapper
import com.tokopedia.shop.flash_sale.data.request.DoSellerCampaignCreationRequest
import com.tokopedia.shop.flash_sale.data.response.DoSellerCampaignCreationResponse
import com.tokopedia.shop.flash_sale.domain.entity.CampaignAction
import com.tokopedia.shop.flash_sale.domain.entity.CampaignCreationResult
import com.tokopedia.shop.flash_sale.domain.entity.enums.Action
import com.tokopedia.shop.flash_sale.domain.entity.enums.CampaignType
import com.tokopedia.shop.flash_sale.domain.entity.enums.PaymentType
import java.util.*
import javax.inject.Inject


class DoSellerCampaignCreationUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: DoSellerCampaignCreationMapper
) : GraphqlUseCase<CampaignCreationResult>(repository) {

    companion object {
        private const val DECREASED_BY_ONE_HOUR = -1
        private const val REQUEST_PARAM_KEY = "params"
        private const val QUERY_NAME = "DoSellerCampaignCreation"
        private const val QUERY = """
            mutation DoSellerCampaignCreation(${'$'}params: DoSellerCampaignCreationRequest!)  {
              doSellerCampaignCreation(params: ${'$'}params){
                is_success
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

        val startDate = params.scheduledStart.toCalendar()
        startDate.add(Calendar.HOUR_OF_DAY, DECREASED_BY_ONE_HOUR)
        val adjustedUpcomingTime = startDate.time

        val payload = DoSellerCampaignCreationRequest(
            actionId,
            CampaignType.FLASH_SALE.id,
            campaignId,
            params.campaignName,
            params.scheduledStart.formatTo(DateConstant.DATE_TIME),
            params.scheduledEnd.formatTo(DateConstant.DATE_TIME),
            adjustedUpcomingTime.formatTo(DateConstant.DATE_TIME),
            params.campaignRelation,
            params.isCampaignRuleSubmit,
            DoSellerCampaignCreationRequest.GradientColorInput(
                params.firstColor,
                params.secondColor
            ),
            params.showTeaser,
            params.paymentType.id,
            params.thematicParticipation,
            DoSellerCampaignCreationRequest.ThematicInfo(params.thematicId, params.subThematicId)
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
        val campaignRelation: List<Long> = emptyList(),
        val isCampaignRuleSubmit: Boolean,
        val firstColor: String,
        val secondColor: String,
        val showTeaser: Boolean = false,
        val paymentType: PaymentType,
        val thematicParticipation: Boolean = false,
        val thematicId: Long = 0,
        val subThematicId: Long = 0
    )

}
package com.tokopedia.shop.flash_sale.domain.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flash_sale.common.constant.Constant
import com.tokopedia.shop.flash_sale.data.mapper.SellerCampaignDetailMapper
import com.tokopedia.shop.flash_sale.data.request.GetSellerCampaignListRequest
import com.tokopedia.shop.flash_sale.data.response.GetSellerCampaignListResponse
import com.tokopedia.shop.flash_sale.domain.entity.CampaignUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetSellerCampaignDetailUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: SellerCampaignDetailMapper,
    private val userSessionInterface: UserSessionInterface
) : GraphqlUseCase<CampaignUiModel>(repository) {
    companion object {
        private const val CAMPAIGN_TYPE_SHOP_FLASH_SALE = 0
        private const val REQUEST_PARAM_KEY = "params"
    }

    init {
        setupUseCase()
    }

    private fun setupUseCase() {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    suspend fun execute(
        campaignId: Int,
    ): CampaignUiModel {
        val request = buildRequest(campaignId)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<GetSellerCampaignListResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(
        campaignId: Int,
        sellerCampaignType: Int = CAMPAIGN_TYPE_SHOP_FLASH_SALE,
        listType: Int = Constant.ZERO,
        rows: Int = Constant.ZERO,
        offset: Int = Constant.ZERO,
        statusId: List<Int> = emptyList(),
        campaignName: String = Constant.EMPTY_STRING,
        thematicParticipation: Boolean = false
    ): GraphqlRequest {
        val payload = GetSellerCampaignListRequest(
            userSessionInterface.shopId.toLong(),
            sellerCampaignType,
            listType,
            GetSellerCampaignListRequest.Pagination(rows, offset),
            GetSellerCampaignListRequest.Filter(statusId, campaignId, campaignName, thematicParticipation)
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)
        return GraphqlRequest(
            GetSellerCampaignList(),
            GetSellerCampaignListResponse::class.java,
            params
        )
    }
}
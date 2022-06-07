package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetSellerCampaignDetailUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val getSellerCampaignListUseCase: GetSellerCampaignListUseCase,
) : GraphqlUseCase<CampaignUiModel>(repository) {

    suspend fun execute(
        campaignId: Int,
    ): CampaignUiModel {
        return coroutineScope {
            val campaignList = getSellerCampaignListUseCase.execute(
                    rows = Constant.ZERO,
                    offset = Constant.ZERO,
                    campaignId = campaignId,
                )
            campaignList.campaigns.first()
        }
    }
}
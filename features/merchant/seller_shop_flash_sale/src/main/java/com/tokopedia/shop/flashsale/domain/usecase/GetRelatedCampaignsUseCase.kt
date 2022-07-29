package com.tokopedia.shop.flashsale.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetRelatedCampaignsUseCase @Inject constructor(
    repository: GraphqlRepository,
    private val getSellerCampaignListUseCase: GetSellerCampaignListUseCase,
) : GraphqlUseCase<List<CampaignUiModel>>(repository) {
    companion object {
        private const val PREVIOUS_CAMPAIGN_ROWS = 20
    }

    private val previousCampaignStatusIds = listOf(
        CampaignStatus.IN_SUBMISSION.id,
        CampaignStatus.IN_REVIEW.id,
        CampaignStatus.READY.id,
        CampaignStatus.ONGOING.id,
        CampaignStatus.FINISHED.id,
        CampaignStatus.ONGOING_CANCELLATION.id,
        CampaignStatus.READY_LOCKED.id
    )

    suspend fun execute(
        keyword: String,
        currentCampaignId: Long,
    ): List<CampaignUiModel> {
        return coroutineScope {
            val campaignList = getSellerCampaignListUseCase.execute(
                rows = PREVIOUS_CAMPAIGN_ROWS,
                offset = Constant.FIRST_PAGE,
                statusId = previousCampaignStatusIds,
                campaignName = keyword,
            )
            campaignList.campaigns.filter {
                // remove current campaign id from result
                it.campaignId != currentCampaignId
            }
        }
    }
}
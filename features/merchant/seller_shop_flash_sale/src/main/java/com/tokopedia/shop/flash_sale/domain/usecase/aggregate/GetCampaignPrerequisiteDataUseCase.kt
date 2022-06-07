package com.tokopedia.shop.flash_sale.domain.usecase.aggregate

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.flash_sale.common.constant.Constant
import com.tokopedia.shop.flash_sale.common.util.DateManager
import com.tokopedia.shop.flash_sale.domain.entity.aggregate.CampaignPrerequisiteData
import com.tokopedia.shop.flash_sale.domain.entity.aggregate.ShareComponentMetadata
import com.tokopedia.shop.flash_sale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flash_sale.domain.usecase.GetSellerCampaignAttributeUseCase
import com.tokopedia.shop.flash_sale.domain.usecase.GetSellerCampaignListUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


class GetCampaignPrerequisiteDataUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val getSellerCampaignListUseCase: GetSellerCampaignListUseCase,
    private val getSellerCampaignAttributeUseCase: GetSellerCampaignAttributeUseCase,
    private val dateManager: DateManager
) : GraphqlUseCase<ShareComponentMetadata>(repository) {

    companion object {
        private const val MAX_DRAFT_COUNT = 3
    }

    suspend fun execute(): CampaignPrerequisiteData {
        return coroutineScope {
            val campaignDraftDeferred = async {
                getSellerCampaignListUseCase.execute(
                    rows = MAX_DRAFT_COUNT,
                    offset = Constant.FIRST_PAGE,
                    statusId = listOf(CampaignStatus.DRAFT.id)
                )
            }
            val remainingQuotaDeferred = async {
                getSellerCampaignAttributeUseCase.execute(
                    month = dateManager.getCurrentMonth(),
                    year = dateManager.getCurrentYear()
                )
            }
            val campaignDrafts = campaignDraftDeferred.await()
            val remainingQuota = remainingQuotaDeferred.await()

            CampaignPrerequisiteData(
                campaignDrafts.campaigns,
                remainingQuota.remainingCampaignQuota
            )
        }
    }

}
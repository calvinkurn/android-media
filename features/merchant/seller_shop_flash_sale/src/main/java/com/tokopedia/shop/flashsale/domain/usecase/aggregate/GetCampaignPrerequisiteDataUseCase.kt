package com.tokopedia.shop.flashsale.domain.usecase.aggregate

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.domain.entity.aggregate.CampaignPrerequisiteData
import com.tokopedia.shop.flashsale.domain.entity.aggregate.ShareComponentMetadata
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignAttributeUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignListUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


class GetCampaignPrerequisiteDataUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val getSellerCampaignListUseCase: GetSellerCampaignListUseCase
) : GraphqlUseCase<ShareComponentMetadata>(repository) {

    companion object {
        private const val DRAFT_COUNT_TO_FETCH = 50
    }

    suspend fun execute(): CampaignPrerequisiteData {
        return coroutineScope {
            val campaignDraftDeferred = async {
                getSellerCampaignListUseCase.execute(
                    rows = DRAFT_COUNT_TO_FETCH,
                    offset = Constant.FIRST_PAGE,
                    statusId = listOf(CampaignStatus.DRAFT.id)
                )
            }

            val campaignDrafts = campaignDraftDeferred.await()

            CampaignPrerequisiteData(campaignDrafts.campaigns)
        }
    }

}
package com.tokopedia.shop.flashsale.domain.usecase.aggregate

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.constant.DraftConstant.MAX_DRAFT_COUNT
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.domain.entity.aggregate.CampaignPrerequisiteData
import com.tokopedia.shop.flashsale.domain.entity.aggregate.ShareComponentMetadata
import com.tokopedia.shop.flashsale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignAttributeUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignListUseCase
import com.tokopedia.shop.flashsale.domain.usecase.ShopInfoByIdQueryUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


class GetCampaignPrerequisiteDataUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val getSellerCampaignListUseCase: GetSellerCampaignListUseCase,
    private val getSellerCampaignAttributeUseCase: GetSellerCampaignAttributeUseCase,
    private val shopInfoByIdQueryUseCase: ShopInfoByIdQueryUseCase,
    private val dateManager: DateManager
) : GraphqlUseCase<ShareComponentMetadata>(repository) {

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
            val shopInfoDeferred = async { shopInfoByIdQueryUseCase.execute() }

            val campaignDrafts = campaignDraftDeferred.await()
            val remainingQuota = remainingQuotaDeferred.await()
            val shopInfo = shopInfoDeferred.await()

            CampaignPrerequisiteData(
                campaignDrafts.campaigns,
                remainingQuota.remainingCampaignQuota,
                shopInfo
            )
        }
    }

}
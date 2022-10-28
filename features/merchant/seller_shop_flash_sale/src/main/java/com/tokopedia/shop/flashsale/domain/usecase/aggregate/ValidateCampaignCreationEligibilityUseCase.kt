package com.tokopedia.shop.flashsale.domain.usecase.aggregate

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.flashsale.common.util.DateManager
import com.tokopedia.shop.flashsale.domain.entity.aggregate.CampaignCreationEligibility
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignAttributeUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignEligibilityUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


class ValidateCampaignCreationEligibilityUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val getSellerCampaignAttributeUseCase: GetSellerCampaignAttributeUseCase,
    private val getSellerCampaignEligibilityUseCase: GetSellerCampaignEligibilityUseCase,
    private val dateManager: DateManager
) : GraphqlUseCase<CampaignCreationEligibility>(repository) {


    suspend fun execute(vpsPackageId : Long): CampaignCreationEligibility {
        return coroutineScope {
            val remainingQuotaDeferred = async {
                getSellerCampaignAttributeUseCase.execute(
                    month = dateManager.getCurrentMonth(),
                    year = dateManager.getCurrentYear(),
                    vpsPackageId = vpsPackageId
                )
            }
            val sellerEligibilityDeferred = async { getSellerCampaignEligibilityUseCase.execute() }

            val remainingQuota = remainingQuotaDeferred.await()
            val isEligible = sellerEligibilityDeferred.await()

            CampaignCreationEligibility(remainingQuota.remainingCampaignQuota, isEligible)
        }
    }

}
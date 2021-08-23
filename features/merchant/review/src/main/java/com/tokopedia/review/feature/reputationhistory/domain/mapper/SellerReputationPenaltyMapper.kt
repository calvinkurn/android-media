package com.tokopedia.review.feature.reputationhistory.domain.mapper

import com.tokopedia.review.feature.reputationhistory.data.model.response.ReputationPenaltyAndRewardResponse
import com.tokopedia.review.feature.reputationhistory.data.model.response.ReputationPenaltyRewardWrapper
import com.tokopedia.review.feature.reputationhistory.util.ReputationPenaltyDateUtils.convertDateTextToTimeStamp
import com.tokopedia.review.feature.reputationhistory.view.model.*
import com.tokopedia.review.feature.reputationhistory.view.viewmodel.SellerReputationViewModel
import javax.inject.Inject

class SellerReputationPenaltyMapper @Inject constructor() {

    fun mapToSellerReputationMerge(reputationPenaltyRewardWrapper: ReputationPenaltyRewardWrapper, startDate: String, endDate: String): SellerReputationPenaltyMergeUiModel {
        val reputationPenaltyAndReward = reputationPenaltyRewardWrapper.reputationPenaltyRewardResponse?.reputationPenaltyAndReward
        val reputationShop = reputationPenaltyRewardWrapper.reputationShopResponse?.reputationShops
        return SellerReputationPenaltyMergeUiModel(
            hasNext = reputationPenaltyAndReward?.page?.next,
            hasPrev = reputationPenaltyAndReward?.page?.prev,
            baseSellerReputationList = mutableListOf<BaseSellerReputation>().apply {
                add(ReputationShopUiModel(
                    reputationScore = reputationShop?.firstOrNull()?.score.orEmpty(),
                    badgeReputationUrl = reputationShop?.firstOrNull()?.badgeHd.orEmpty(),
                ))
                add(ReputationDateUiModel(
                    startDate = startDate.convertDateTextToTimeStamp(SellerReputationViewModel.PATTERN_PENALTY_DATE_PARAM),
                    endDate = endDate.convertDateTextToTimeStamp(SellerReputationViewModel.PATTERN_PENALTY_DATE_PARAM)
                ))
                if (reputationPenaltyAndReward?.list?.isNotEmpty() == true) {
                    addAll(
                        reputationPenaltyAndReward.list.map {
                            ReputationPenaltyUiModel(
                                date = it.timeFmt,
                                invoice = it.invoiceRefNum,
                                penaltyScore = it.score,
                                description = it.information
                            )
                        }
                    )
                }
            }
        )
    }

    fun mapToPenaltyReputationList(reputationPenaltyRewardResponse: ReputationPenaltyAndRewardResponse.Data): SellerReputationPenaltyUiModel {
        return SellerReputationPenaltyUiModel(
            reputationPenaltyList = mutableListOf<ReputationPenaltyUiModel>().apply {
                if (reputationPenaltyRewardResponse.reputationPenaltyAndReward.list.isNotEmpty()) {
                    addAll(
                        reputationPenaltyRewardResponse.reputationPenaltyAndReward.list.map {
                            ReputationPenaltyUiModel(
                                date = it.timeFmt,
                                invoice = it.invoiceRefNum,
                                penaltyScore = it.score,
                                description = it.information
                            )
                        }
                    )
                }
            },
            hasNext = reputationPenaltyRewardResponse.reputationPenaltyAndReward.page.next,
            hasPrev = reputationPenaltyRewardResponse.reputationPenaltyAndReward.page.prev
        )
    }
}
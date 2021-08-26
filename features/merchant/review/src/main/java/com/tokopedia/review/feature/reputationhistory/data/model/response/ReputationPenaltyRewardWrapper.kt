package com.tokopedia.review.feature.reputationhistory.data.model.response

data class ReputationPenaltyRewardWrapper(
    val reputationShopResponse: ReputationShopResponse? = ReputationShopResponse(),
    val reputationPenaltyRewardResponse: ReputationPenaltyAndRewardResponse.Data? = ReputationPenaltyAndRewardResponse.Data()
)
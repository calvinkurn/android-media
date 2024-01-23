package com.tokopedia.sellerhomecommon.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RewardDetailUiModel(
    val rewardId: String,
    val rewardTitle: String,
    val rewardSubtitle: String,
    val benefitList: List<RewardDetailBenefit>,
    val rewardImage: String
) : Parcelable

@Parcelize
data class RewardDetailBenefit(
    val benefitTitle: String,
    val benefitDescription: String
) : Parcelable

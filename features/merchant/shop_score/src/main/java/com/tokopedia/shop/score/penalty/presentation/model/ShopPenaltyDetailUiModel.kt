package com.tokopedia.shop.score.penalty.presentation.model

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

data class ShopPenaltyDetailUiModel(
    val titleDetail: String = "",
    val startDateDetail: String = "",
    @StringRes val descStatusPenalty: Int? = null,
    val summaryDetail: String = "",
    val deductionPointPenalty: String = "",
    val endDateDetail: String = "",
    val prefixDateDetail: String = "",
    val productName: String? = null,
    val stepperPenaltyDetailList: List<StepperPenaltyDetail> = listOf()
) {

    data class StepperPenaltyDetail(
        @ColorRes val colorDotStepper: Int? = null,
        @ColorRes val colorLineStepper: Int? = null,
        @StringRes val titleStepper: Int? = null,
        @ColorRes val colorStatusTitle: Int? = null,
        val isBold: Boolean = false,
        val isDividerShow: Boolean = false
    )
}

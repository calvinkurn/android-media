package com.tokopedia.shop.score.penalty.presentation.model

import androidx.annotation.ColorRes

data class ShopPenaltyDetailUiModel(val titleDetail: String = "",
                                    val startDateDetail: String = "",
                                    val summaryDetail: String = "",
                                    val deductionPointPenalty: String = "",
                                    val endDateDetail: String = "",
                                    val prefixDateDetail: String = "",
                                    val stepperPenaltyDetailList: List<StepperPenaltyDetail> = listOf()) {

    data class StepperPenaltyDetail(
            @ColorRes val colorDotStepper: Int? = null,
            @ColorRes val colorLineStepper: Int? = null,
            val titleStepper: String = "",
            @ColorRes val colorStatusTitle: Int? = null,
            val isBold: Boolean = false,
            val isDividerShow: Boolean = false
    )
}
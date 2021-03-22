package com.tokopedia.shop.score.penalty.presentation.model

import androidx.annotation.ColorRes

data class ShopPenaltyDetail(val titleDetail: String = "",
                             val dateDetail: String = "",
                             val invoiceTransaction: String = "",
                             val deductionPointPenalty: String = "",
                             val activeDate: String = "",
                             val stepperPenaltyDetailList: List<StepperPenaltyDetail> = listOf()) {

    data class StepperPenaltyDetail(
            val colorDotStepper: String = "",
            @ColorRes val colorLineStepper: Int? = null,
            @ColorRes val titleStepper: Int? = null,
            val currentStatus: String = "")
}
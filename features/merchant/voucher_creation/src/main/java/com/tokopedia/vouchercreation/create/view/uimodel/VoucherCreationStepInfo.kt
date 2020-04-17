package com.tokopedia.vouchercreation.create.view.uimodel

enum class VoucherCreationStepInfo(val stepPosition: Int,
                                   val stepDescription: String,
                                   val progressPercentage: Int = stepPosition.plus(1) * StepDescriptionText.percentagePerStep) {
    STEP_ONE(0, StepDescriptionText.VOUCHER_TARGET),
    STEP_TWO(1, StepDescriptionText.PROMOTION_TYPE_AND_BUDGET),
    STEP_THREE(2, StepDescriptionText.VOUCHER_PERIOD),
    STEP_FOUR(3, StepDescriptionText.VOUCHER_REVIEW)
}

object StepDescriptionText {
    internal const val VOUCHER_TARGET = "Target dan nama voucher"
    internal const val PROMOTION_TYPE_AND_BUDGET = "Jenis dan budget promosi"
    internal const val VOUCHER_PERIOD = "Periode voucher"
    internal const val VOUCHER_REVIEW = "Review voucher"

    internal const val percentagePerStep = 2500
}
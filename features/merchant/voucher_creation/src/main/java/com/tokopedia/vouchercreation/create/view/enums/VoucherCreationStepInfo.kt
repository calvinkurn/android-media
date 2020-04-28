package com.tokopedia.vouchercreation.create.view.enums

import androidx.annotation.StringRes
import com.tokopedia.vouchercreation.R

enum class VoucherCreationStepInfo(val stepPosition: Int,
                                   @StringRes val stepDescriptionRes: Int,
                                   val progressPercentage: Int = stepPosition.plus(1) * StepDescriptionText.percentagePerStep) {
    STEP_ONE(0, R.string.mvc_create_step_desc_voucher_target),
    STEP_TWO(1, R.string.mvc_create_step_desc_promo_type),
    STEP_THREE(2, R.string.mvc_create_step_desc_voucher_period),
    STEP_FOUR(3, R.string.mvc_create_step_desc_voucher_review)
}

object StepDescriptionText {
    internal const val percentagePerStep = 2500
}
package com.tokopedia.vouchercreation.create.view.enums

import androidx.annotation.IntDef
import androidx.annotation.StringRes
import com.tokopedia.vouchercreation.R

enum class VoucherCreationStepInfo(val stepPosition: Int,
                                   @StringRes val stepDescriptionRes: Int,
                                   val progressPercentage: Int = stepPosition.plus(1) * StepDescriptionText.percentagePerStep) {
    STEP_ONE(VoucherCreationStep.TARGET, R.string.mvc_create_step_desc_voucher_information),
    STEP_TWO(VoucherCreationStep.BENEFIT, R.string.mvc_create_step_desc_voucher_adjustment),
    STEP_THREE(VoucherCreationStep.PERIOD, R.string.mvc_create_step_desc_voucher_period),
    STEP_FOUR(VoucherCreationStep.REVIEW, R.string.mvc_create_step_desc_voucher_review)
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(VoucherCreationStep.TARGET, VoucherCreationStep.BENEFIT, VoucherCreationStep.PERIOD, VoucherCreationStep.REVIEW)
annotation class VoucherCreationStep {
    companion object {
        const val TARGET = 0
        const val BENEFIT = 1
        const val PERIOD = 2
        const val REVIEW = 3
    }
}

object StepDescriptionText {
    internal const val percentagePerStep = 2500
}
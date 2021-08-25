package com.tokopedia.power_merchant.subscribe.view.model

import androidx.annotation.ColorRes

data class PMProStatusStepperUiModel(
    val titleStepper: String = "",
    @ColorRes val colorStatusTitle: Int? = null,
    @ColorRes val colorDivider: Int? = null,
    val isStepperShow: Boolean = false,
    val isPassedActive: Boolean = false,
    val isCurrentActive: Boolean = false
)
package com.tokopedia.gm.common.data.source.local.model

import androidx.annotation.DrawableRes

/**
 * Created by @ilhamsuaib on 22/04/22.
 */

data class PMBenefitItemUiModel(
    @DrawableRes val resIcon: Int,
    val benefitDescription: String
)
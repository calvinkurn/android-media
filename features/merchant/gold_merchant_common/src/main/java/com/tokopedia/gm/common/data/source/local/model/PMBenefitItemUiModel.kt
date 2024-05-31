package com.tokopedia.gm.common.data.source.local.model

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO

/**
 * Created by @ilhamsuaib on 22/04/22.
 */

data class PMBenefitItemUiModel(
    val icon: Int = Int.ZERO,
    val iconUrl: String= String.EMPTY,
    val benefitDescription: String
)

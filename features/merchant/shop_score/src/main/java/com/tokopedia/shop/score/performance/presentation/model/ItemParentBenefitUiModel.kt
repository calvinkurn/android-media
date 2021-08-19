package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes

open class ItemParentBenefitUiModel(
    open val iconUrl: String = "",
    @StringRes open val titleResources: Int? = null
)
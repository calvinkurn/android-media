package com.tokopedia.gm.common.presentation.model

import androidx.annotation.StringRes

/**
 * Created by @ilhamsuaib on 28/04/22.
 */

data class CardTooltipLevelUiModel(
    @StringRes val title: Int? = null,
    @StringRes val desc: Int? = null,
    val isMyShop: Boolean = false
)
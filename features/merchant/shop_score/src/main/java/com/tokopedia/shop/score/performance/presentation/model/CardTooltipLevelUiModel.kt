package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes

data class CardTooltipLevelUiModel(@StringRes val title: Int = 0,
                                   @StringRes val desc: Int = 0,
                                   val isMyShop: Boolean = false)
package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes

data class CardTooltipLevelUiModel(@StringRes val title: Int? = null,
                                   @StringRes val desc: Int? = null,
                                   val isMyShop: Boolean = false)
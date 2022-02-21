package com.tokopedia.shop.score.performance.presentation.model

import androidx.annotation.StringRes

data class ItemFaqUiModel(
    @StringRes val title: Int? = null,
    @StringRes val desc_first: Int? = null,
    @StringRes val desc_second: Int? = null,
    var isShow: Boolean = false,
    val isCalculationScore: Boolean = false,
    val cardLevelList: List<CardTooltipLevelUiModel> = listOf(),
    val parameterFaqList: List<ItemParameterFaqUiModel> = listOf()
)
package com.tokopedia.shop.score.performance.presentation.model

data class ItemFaqUiModel(val title: String = "",
                          val desc_first: String= "",
                          val desc_second: String = "",
                          var isShow: Boolean = false,
                          val isCalculationScore: Boolean = false,
                          val cardLevelList: List<CardTooltipLevelUiModel> = listOf(),
                          val parameterFaqList: List<ItemParameterFaqUiModel> = listOf()
)
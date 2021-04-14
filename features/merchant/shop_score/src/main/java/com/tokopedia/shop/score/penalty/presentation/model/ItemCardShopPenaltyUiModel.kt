package com.tokopedia.shop.score.penalty.presentation.model

data class ItemCardShopPenaltyUiModel(val totalPenalty: Int = 0,
                                      val deductionPoints: Int = 0,
                                      val hasPenalty: Boolean = false)
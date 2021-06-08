package com.tokopedia.shop.score.penalty.presentation.adapter

import com.tokopedia.shop.score.penalty.presentation.model.*

interface PenaltyTypeFactory {
    fun type(itemPenaltyUiModel: ItemPenaltyUiModel): Int
    fun type(itemCardShopPenaltyUiModel: ItemCardShopPenaltyUiModel): Int
    fun type(itemPeriodDetailPenaltyUiModel: ItemPeriodDetailPenaltyUiModel): Int
    fun type(itemSortFilterPenaltyUiModel: ItemSortFilterPenaltyUiModel): Int
    fun type(itemPenaltyErrorUiModel: ItemPenaltyErrorUiModel): Int
}
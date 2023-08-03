package com.tokopedia.shop.score.penalty.presentation.adapter

import com.tokopedia.shop.score.penalty.presentation.model.ItemCardShopPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyEmptyStateUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyErrorUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyInfoNotificationUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyPointCardUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltySubsectionUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyTickerUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPeriodDetailPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemSortFilterPenaltyUiModel

interface PenaltyTypeFactory {
    fun type(itemPenaltyUiModel: ItemPenaltyUiModel): Int
    fun type(itemCardShopPenaltyUiModel: ItemCardShopPenaltyUiModel): Int
    fun type(itemPeriodDetailPenaltyUiModel: ItemPeriodDetailPenaltyUiModel): Int
    fun type(itemSortFilterPenaltyUiModel: ItemSortFilterPenaltyUiModel): Int
    fun type(itemPenaltyErrorUiModel: ItemPenaltyErrorUiModel): Int

    fun type(itemPenaltyTickerUiModel: ItemPenaltyTickerUiModel): Int
    fun type(itemPenaltyInfoNotificationUiModel: ItemPenaltyInfoNotificationUiModel): Int
    fun type(itemPenaltySubsectionUiModel: ItemPenaltySubsectionUiModel): Int
    fun type(itemPenaltyPointCardUiModel: ItemPenaltyPointCardUiModel): Int
    fun type(itemPenaltyEmptyStateUiModel: ItemPenaltyEmptyStateUiModel): Int
}

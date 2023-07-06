package com.tokopedia.shop.score.penalty.presentation.adapter.filter

import com.tokopedia.shop.score.penalty.presentation.model.filtertypes.ItemPenaltyFilterTypesChecklistUiModel
import com.tokopedia.shop.score.penalty.presentation.model.filtertypes.ItemPenaltyFilterTypesSubtitleUiModel

interface FilterPenaltyTypesTypeFactory {

    fun type(itemPenaltyFilterTypesSubtitleUiModel: ItemPenaltyFilterTypesSubtitleUiModel): Int
    fun type(itemPenaltyFilterTypesChecklistUiModel: ItemPenaltyFilterTypesChecklistUiModel): Int

}

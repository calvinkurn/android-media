package com.tokopedia.shop.score.penalty.presentation.adapter.filter

import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterDateUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel

interface FilterPenaltyTypeFactory {
    fun type(penaltyFilterUiModel: PenaltyFilterUiModel): Int
    fun type(penaltyFilterDateUiModel: PenaltyFilterDateUiModel): Int
}

package com.tokopedia.shop.score.penalty.presentation.adapter.filter

import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterDateUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.old.model.PenaltyFilterUiModelOld

interface FilterPenaltyTypeFactory {
    fun type(penaltyFilterUiModel: PenaltyFilterUiModel): Int
    fun type(penaltyFilterUiModelOld: PenaltyFilterUiModelOld): Int
    fun type(penaltyFilterDateUiModel: PenaltyFilterDateUiModel): Int
}

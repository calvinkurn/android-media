package com.tokopedia.shop.score.penalty.presentation.adapter.calculation

import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationFormulaUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationInformationUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationScoreUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationSubtitleUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationTableUiModel

interface PenaltyCalculationTypeFactory {

    fun type(itemPenaltyCalculationScoreUiModel: ItemPenaltyCalculationScoreUiModel): Int
    fun type(itemPenaltyCalculationFormulaUiModel: ItemPenaltyCalculationFormulaUiModel): Int
    fun type(itemPenaltyCalculationSubtitleUiModel: ItemPenaltyCalculationSubtitleUiModel): Int
    fun type(itemPenaltyCalculationTableUiModel: ItemPenaltyCalculationTableUiModel): Int
    fun type(itemPenaltyCalculationInformationUiModel: ItemPenaltyCalculationInformationUiModel): Int

}

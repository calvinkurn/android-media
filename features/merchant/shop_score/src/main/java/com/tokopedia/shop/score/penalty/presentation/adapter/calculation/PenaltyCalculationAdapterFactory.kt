package com.tokopedia.shop.score.penalty.presentation.adapter.calculation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.calculation.ItemPenaltyCalculationFormulaViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.calculation.ItemPenaltyCalculationInformationViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.calculation.ItemPenaltyCalculationScoreViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.calculation.ItemPenaltyCalculationSubtitleViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.calculation.ItemPenaltyCalculationTableViewHolder
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationFormulaUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationInformationUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationScoreUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationSubtitleUiModel
import com.tokopedia.shop.score.penalty.presentation.model.calculation.ItemPenaltyCalculationTableUiModel

class PenaltyCalculationAdapterFactory(
    private val itemPenaltyCalculationScoreListener: ItemPenaltyCalculationScoreAdapter.Listener
): BaseAdapterTypeFactory(), PenaltyCalculationTypeFactory {

    override fun type(itemPenaltyCalculationScoreUiModel: ItemPenaltyCalculationScoreUiModel): Int {
        return ItemPenaltyCalculationScoreViewHolder.LAYOUT
    }

    override fun type(itemPenaltyCalculationFormulaUiModel: ItemPenaltyCalculationFormulaUiModel): Int {
        return ItemPenaltyCalculationFormulaViewHolder.LAYOUT
    }

    override fun type(itemPenaltyCalculationSubtitleUiModel: ItemPenaltyCalculationSubtitleUiModel): Int {
        return ItemPenaltyCalculationSubtitleViewHolder.LAYOUT
    }

    override fun type(itemPenaltyCalculationTableUiModel: ItemPenaltyCalculationTableUiModel): Int {
        return ItemPenaltyCalculationTableViewHolder.LAYOUT
    }

    override fun type(itemPenaltyCalculationInformationUiModel: ItemPenaltyCalculationInformationUiModel): Int {
        return ItemPenaltyCalculationInformationViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ItemPenaltyCalculationScoreViewHolder.LAYOUT -> ItemPenaltyCalculationScoreViewHolder(
                parent,
                itemPenaltyCalculationScoreListener
            )
            ItemPenaltyCalculationFormulaViewHolder.LAYOUT -> ItemPenaltyCalculationFormulaViewHolder(parent)
            ItemPenaltyCalculationSubtitleViewHolder.LAYOUT -> ItemPenaltyCalculationSubtitleViewHolder(parent)
            ItemPenaltyCalculationTableViewHolder.LAYOUT -> ItemPenaltyCalculationTableViewHolder(parent)
            ItemPenaltyCalculationInformationViewHolder.LAYOUT -> ItemPenaltyCalculationInformationViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}

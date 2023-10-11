package com.tokopedia.shop.score.penalty.presentation.adapter.filter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyBottomSheetListener
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyDateListener
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltyEmptyViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltyFilterBottomSheetViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltyFilterDateViewHolder
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterDateUiModel
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.old.adapter.viewholder.ItemPenaltyFilterBottomSheetOldViewHolder
import com.tokopedia.shop.score.penalty.presentation.old.model.PenaltyFilterUiModelOld

class FilterPenaltyAdapterFactory(
    private val itemBottomSheetListener: FilterPenaltyBottomSheetListener,
    private val penaltyFilterPenaltyDateListener: FilterPenaltyDateListener? = null
) : BaseAdapterTypeFactory(), FilterPenaltyTypeFactory {

    override fun type(penaltyFilterUiModel: PenaltyFilterUiModel): Int {
        return ItemPenaltyFilterBottomSheetViewHolder.LAYOUT
    }

    override fun type(penaltyFilterUiModelOld: PenaltyFilterUiModelOld): Int {
        return ItemPenaltyFilterBottomSheetOldViewHolder.LAYOUT
    }

    override fun type(penaltyFilterDateUiModel: PenaltyFilterDateUiModel): Int {
        return ItemPenaltyFilterDateViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int {
        return ItemPenaltyEmptyViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ItemPenaltyFilterBottomSheetViewHolder.LAYOUT -> ItemPenaltyFilterBottomSheetViewHolder(
                parent,
                itemBottomSheetListener
            )
            ItemPenaltyFilterBottomSheetOldViewHolder.LAYOUT -> ItemPenaltyFilterBottomSheetOldViewHolder(
                parent,
                itemBottomSheetListener
            )
            ItemPenaltyEmptyViewHolder.LAYOUT -> ItemPenaltyEmptyViewHolder(parent)
            ItemPenaltyFilterDateViewHolder.LAYOUT -> ItemPenaltyFilterDateViewHolder(parent, penaltyFilterPenaltyDateListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}

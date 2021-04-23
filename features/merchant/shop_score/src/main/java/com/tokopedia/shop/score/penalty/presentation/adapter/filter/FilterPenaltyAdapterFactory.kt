package com.tokopedia.shop.score.penalty.presentation.adapter.filter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyBottomSheetListener
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyTypeFactory
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltyEmptyViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltyFilterUiViewHolder
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel

class FilterPenaltyAdapterFactory(private val itemBottomSheetListener: FilterPenaltyBottomSheetListener): BaseAdapterTypeFactory(), FilterPenaltyTypeFactory {

    override fun type(penaltyFilterUiModel: PenaltyFilterUiModel): Int {
        return ItemPenaltyFilterUiViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int {
        return ItemPenaltyEmptyViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ItemPenaltyFilterUiViewHolder.LAYOUT -> ItemPenaltyFilterUiViewHolder(parent, itemBottomSheetListener)
            ItemPenaltyEmptyViewHolder.LAYOUT -> ItemPenaltyEmptyViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
package com.tokopedia.shop.score.penalty.presentation.adapter.filter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.filtertypes.ItemPenaltyFilterTypesChecklistViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.filtertypes.ItemPenaltyFilterTypesSubtitleViewHolder
import com.tokopedia.shop.score.penalty.presentation.model.filtertypes.ItemPenaltyFilterTypesChecklistUiModel
import com.tokopedia.shop.score.penalty.presentation.model.filtertypes.ItemPenaltyFilterTypesSubtitleUiModel

class FilterPenaltyTypesAdapterFactory(
    private val itemPenaltyFiltertypesChecklistListener: ItemPenaltyFilterTypesChecklistViewHolder.Listener
): BaseAdapterTypeFactory(), FilterPenaltyTypesTypeFactory {

    override fun type(itemPenaltyFilterTypesSubtitleUiModel: ItemPenaltyFilterTypesSubtitleUiModel): Int {
        return ItemPenaltyFilterTypesSubtitleViewHolder.LAYOUT
    }

    override fun type(itemPenaltyFilterTypesChecklistUiModel: ItemPenaltyFilterTypesChecklistUiModel): Int {
        return ItemPenaltyFilterTypesChecklistViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ItemPenaltyFilterTypesSubtitleViewHolder.LAYOUT -> ItemPenaltyFilterTypesSubtitleViewHolder(parent)
            ItemPenaltyFilterTypesChecklistViewHolder.LAYOUT -> ItemPenaltyFilterTypesChecklistViewHolder(
                parent,
                itemPenaltyFiltertypesChecklistListener
            )
            else -> super.createViewHolder(parent, type)
        }
    }

}

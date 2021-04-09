package com.tokopedia.shop.score.penalty.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.*
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel

class PenaltyPageAdapterFactory(
        private val itemPenaltyDetailPenaltyListener: ItemDetailPenaltyListener,
        private val itemGlobalErrorListener: PenaltyGlobalErrorListener
): BaseAdapterTypeFactory(), PenaltyTypeFactory {

    override fun type(itemPenaltyUiModel: ItemPenaltyUiModel): Int {
        return ItemPenaltyViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel?): Int {
        return ItemPenaltyEmptyViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ItemPenaltyViewHolder.LAYOUT -> ItemPenaltyViewHolder(parent, itemPenaltyDetailPenaltyListener)
            ItemPenaltyEmptyViewHolder.LAYOUT -> ItemPenaltyEmptyViewHolder(parent)
            ItemPenaltyGlobalErrorViewHolder.LAYOUT -> ItemPenaltyGlobalErrorViewHolder(parent, itemGlobalErrorListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
package com.tokopedia.shop.score.penalty.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.*
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel

class PenaltyPageAdapterFactory(
        private val itemPenaltyDetailPenaltyListener: ItemDetailPenaltyListener
): BaseAdapterTypeFactory(), PenaltyTypeFactory {

    override fun type(itemPenaltyUiModel: ItemPenaltyUiModel): Int {
        return ItemPenaltyViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int {
        return ItemPenaltyShimmerViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel?): Int {
        return ItemPenaltyEmptyViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ItemPenaltyViewHolder.LAYOUT -> ItemPenaltyViewHolder(parent, itemPenaltyDetailPenaltyListener)
            ItemPenaltyShimmerViewHolder.LAYOUT -> ItemPenaltyShimmerViewHolder(parent)
            ItemPenaltyEmptyViewHolder.LAYOUT -> ItemPenaltyEmptyViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
package com.tokopedia.shop.score.penalty.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.*
import com.tokopedia.shop.score.penalty.presentation.model.ItemCardShopPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemDetailPenaltyFilterUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel

class PenaltyPageAdapterFactory(private val dateFilterPenaltyListener: FilterPenaltyListener): BaseAdapterTypeFactory(), PenaltyTypeFactory {

    override fun type(itemCardShopPenaltyUiModel: ItemCardShopPenaltyUiModel): Int {
        return ItemCardShopPenaltyViewHolder.LAYOUT
    }

    override fun type(itemDetailPenaltyFilterUiModel: ItemDetailPenaltyFilterUiModel): Int {
        return ItemDetailPenaltyFilterViewHolder.LAYOUT
    }

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
            ItemCardShopPenaltyViewHolder.LAYOUT -> ItemCardShopPenaltyViewHolder(parent)
            ItemDetailPenaltyFilterViewHolder.LAYOUT -> ItemDetailPenaltyFilterViewHolder(parent, dateFilterPenaltyListener)
            ItemPenaltyViewHolder.LAYOUT -> ItemPenaltyViewHolder(parent)
            ItemPenaltyShimmerViewHolder.LAYOUT -> ItemPenaltyShimmerViewHolder(parent)
            ItemPenaltyEmptyViewHolder.LAYOUT -> ItemPenaltyEmptyViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
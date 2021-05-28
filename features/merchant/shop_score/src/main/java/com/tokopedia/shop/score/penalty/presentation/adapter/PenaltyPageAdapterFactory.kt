package com.tokopedia.shop.score.penalty.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.*
import com.tokopedia.shop.score.penalty.presentation.model.*

class PenaltyPageAdapterFactory(
        private val itemPenaltyDetailPenaltyListener: ItemDetailPenaltyListener,
        private val itemHeaderCardPenaltyListener: ItemHeaderCardPenaltyListener,
        private val itemPenaltyErrorListener: ItemPenaltyErrorListener,
        private val itemSortFilterPenaltyListener: ItemSortFilterPenaltyListener,
        private val itemPeriodDateFilterListener: ItemPeriodDateFilterListener
): BaseAdapterTypeFactory(), PenaltyTypeFactory {

    override fun type(itemPenaltyUiModel: ItemPenaltyUiModel): Int {
        return ItemPenaltyViewHolder.LAYOUT
    }

    override fun type(itemCardShopPenaltyUiModel: ItemCardShopPenaltyUiModel): Int {
        return ItemHeaderCardPenaltyViewHolder.LAYOUT
    }

    override fun type(itemPeriodDetailPenaltyUiModel: ItemPeriodDetailPenaltyUiModel): Int {
        return ItemPeriodDateFilterViewHolder.LAYOUT
    }

    override fun type(itemSortFilterPenaltyUiModel: ItemSortFilterPenaltyUiModel): Int {
        return ItemSortFilterPenaltyViewHolder.LAYOUT
    }

    override fun type(itemPenaltyErrorUiModel: ItemPenaltyErrorUiModel): Int {
        return ItemPenaltyErrorViewHolder.LAYOUT
    }

    override fun type(viewModel: EmptyModel?): Int {
        return ItemPenaltyEmptyViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel?): Int {
        return ItemPenaltyLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ItemPenaltyViewHolder.LAYOUT -> ItemPenaltyViewHolder(parent, itemPenaltyDetailPenaltyListener)
            ItemHeaderCardPenaltyViewHolder.LAYOUT -> ItemHeaderCardPenaltyViewHolder(parent, itemHeaderCardPenaltyListener)
            ItemPenaltyEmptyViewHolder.LAYOUT -> ItemPenaltyEmptyViewHolder(parent)
            ItemPenaltyErrorViewHolder.LAYOUT -> ItemPenaltyErrorViewHolder(parent, itemPenaltyErrorListener)
            ItemPeriodDateFilterViewHolder.LAYOUT -> ItemPeriodDateFilterViewHolder(parent, itemPeriodDateFilterListener)
            ItemSortFilterPenaltyViewHolder.LAYOUT -> ItemSortFilterPenaltyViewHolder(parent, itemSortFilterPenaltyListener)
            ItemPenaltyLoadingViewHolder.LAYOUT -> ItemPenaltyLoadingViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
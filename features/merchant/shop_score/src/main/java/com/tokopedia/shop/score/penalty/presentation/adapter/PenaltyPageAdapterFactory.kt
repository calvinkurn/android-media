package com.tokopedia.shop.score.penalty.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemHeaderCardPenaltyViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltyEmptyCustomViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltyEmptyViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltyErrorViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltyInfoNotificationViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltyLoadingViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltyPointCardViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltySubsectionViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltyTickerViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPenaltyViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemPeriodDateFilterViewHolder
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemSortFilterPenaltyViewHolder
import com.tokopedia.shop.score.penalty.presentation.model.ItemCardShopPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyEmptyStateUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyErrorUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyInfoNotificationUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyPointCardUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltySubsectionUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyTickerUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPeriodDetailPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemSortFilterPenaltyUiModel

class PenaltyPageAdapterFactory(
    private val itemPenaltyDetailPenaltyListener: ItemDetailPenaltyListener,
    private val itemHeaderCardPenaltyListener: ItemHeaderCardPenaltyListener,
    private val itemPenaltyErrorListener: ItemPenaltyErrorListener,
    private val itemSortFilterPenaltyListener: ItemSortFilterPenaltyListener,
    private val itemPeriodDateFilterListener: ItemPeriodDateFilterListener,
    private val itemPenaltyPointCardListener: ItemPenaltyPointCardListener? = null,
    private val itemPenaltySubsectionListener: ItemPenaltySubsectionListener? = null,
    private val itemPenaltyInfoNotificationListener: ItemPenaltyInfoNotificationListener? = null,
    private val itemPenaltyTickerListener: ItemPenaltyTickerListener? = null
) : BaseAdapterTypeFactory(), PenaltyTypeFactory {

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

    override fun type(itemPenaltyTickerUiModel: ItemPenaltyTickerUiModel): Int {
        return ItemPenaltyTickerViewHolder.LAYOUT
    }

    override fun type(itemPenaltyInfoNotificationUiModel: ItemPenaltyInfoNotificationUiModel): Int {
        return ItemPenaltyInfoNotificationViewHolder.LAYOUT
    }

    override fun type(itemPenaltySubsectionUiModel: ItemPenaltySubsectionUiModel): Int {
        return ItemPenaltySubsectionViewHolder.LAYOUT
    }

    override fun type(itemPenaltyPointCardUiModel: ItemPenaltyPointCardUiModel): Int {
        return ItemPenaltyPointCardViewHolder.LAYOUT
    }

    override fun type(itemPenaltyEmptyStateUiModel: ItemPenaltyEmptyStateUiModel): Int {
        return ItemPenaltyEmptyCustomViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ItemPenaltyViewHolder.LAYOUT -> ItemPenaltyViewHolder(
                parent,
                itemPenaltyDetailPenaltyListener
            )
            ItemHeaderCardPenaltyViewHolder.LAYOUT -> ItemHeaderCardPenaltyViewHolder(
                parent,
                itemHeaderCardPenaltyListener
            )
            ItemPenaltyEmptyViewHolder.LAYOUT -> ItemPenaltyEmptyViewHolder(parent)
            ItemPenaltyErrorViewHolder.LAYOUT -> ItemPenaltyErrorViewHolder(
                parent,
                itemPenaltyErrorListener
            )
            ItemPeriodDateFilterViewHolder.LAYOUT -> ItemPeriodDateFilterViewHolder(
                parent,
                itemPeriodDateFilterListener
            )
            ItemSortFilterPenaltyViewHolder.LAYOUT -> ItemSortFilterPenaltyViewHolder(
                parent,
                itemSortFilterPenaltyListener
            )
            ItemPenaltyLoadingViewHolder.LAYOUT -> ItemPenaltyLoadingViewHolder(parent)
            ItemPenaltyTickerViewHolder.LAYOUT -> ItemPenaltyTickerViewHolder(parent, itemPenaltyTickerListener)
            ItemPenaltyInfoNotificationViewHolder.LAYOUT -> ItemPenaltyInfoNotificationViewHolder(parent, itemPenaltyInfoNotificationListener)
            ItemPenaltySubsectionViewHolder.LAYOUT -> ItemPenaltySubsectionViewHolder(parent, itemPenaltySubsectionListener)
            ItemPenaltyPointCardViewHolder.LAYOUT -> ItemPenaltyPointCardViewHolder(parent, itemPenaltyPointCardListener)
            ItemPenaltyEmptyCustomViewHolder.LAYOUT -> ItemPenaltyEmptyCustomViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}

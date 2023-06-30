package com.tokopedia.shop.score.penalty.presentation.adapter

import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.shop.score.penalty.presentation.adapter.diffutil.ShopPenaltyDiffUtilCallback
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemSortFilterPenaltyViewHolder
import com.tokopedia.shop.score.penalty.presentation.model.BasePenaltyPage
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyEmptyStateUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyErrorUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyInfoNotificationUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyPointCardUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltySubsectionUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.model.ItemSortFilterPenaltyUiModel
import com.tokopedia.shop.score.penalty.presentation.widget.OnStickySingleHeaderListener
import com.tokopedia.shop.score.penalty.presentation.widget.StickySingleHeaderView

class PenaltyPageAdapter(private val penaltyPageAdapterFactory: PenaltyPageAdapterFactory) :
    BaseListAdapter<Visitable<*>, PenaltyPageAdapterFactory>(penaltyPageAdapterFactory),
    StickySingleHeaderView.OnStickySingleHeaderAdapter {

    private var onStickySingleHeaderViewListener: OnStickySingleHeaderListener? = null
    private var recyclerView: RecyclerView? = null
    private var initialPenaltyScoreCardUiModel: ItemPenaltyPointCardUiModel? = null

    private val penaltySortFilterPosition: Int?
        get() = visitables.indexOfFirst {
            it.javaClass == ItemSortFilterPenaltyUiModel::class.java
        }.takeIf { it != -1 }

    fun setPenaltyData(penaltyListUiModel: List<BasePenaltyPage>) {
        initialPenaltyScoreCardUiModel =
            penaltyListUiModel.find { it is ItemPenaltyPointCardUiModel } as? ItemPenaltyPointCardUiModel

        val diffCallback = ShopPenaltyDiffUtilCallback(visitables, penaltyListUiModel)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(penaltyListUiModel)
        diffResult.dispatchUpdatesTo(this)
    }

    fun removePenaltyListData() {
        val penaltyListCount = visitables.count { it is ItemPenaltyUiModel }
        visitables.removeAll { it is ItemPenaltyUiModel }
        notifyItemRangeRemoved(visitables.size, penaltyListCount)
    }

    fun removeShopPenaltyAllData() {
        val shopPerformanceDataCount = visitables.filterIsInstance<BasePenaltyPage>().count()
        if (shopPerformanceDataCount.isMoreThanZero()) {
            visitables.removeAll { it is BasePenaltyPage }
            notifyItemRangeRemoved(visitables.size, shopPerformanceDataCount)
        }
    }

    fun updatePenaltyListData(penaltyListUiModel: List<BasePenaltyPage>) {
        visitables.addAll(penaltyListUiModel)
        notifyItemRangeInserted(visitables.size, penaltyListUiModel.size)
    }

    fun updateChipsSelected(
        chipsList: List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>,
        isDateFilterApplied: Boolean? = null
    ) {
        val updateIndex = visitables.indexOfFirst { it is ItemSortFilterPenaltyUiModel }
        var hasDateFilterApplied = false
        visitables.filterIsInstance<ItemSortFilterPenaltyUiModel>()
            .firstOrNull()?.run {
                itemSortFilterWrapperList = chipsList
                hasDateFilterApplied = this.isDateFilterApplied
                isDateFilterApplied?.let {
                    this.isDateFilterApplied = it
                    hasDateFilterApplied = it
                }
            }
        if (updateIndex != RecyclerView.NO_POSITION) {
            notifyItemChanged(updateIndex)

            val shouldHidePointCard =
                chipsList.count { it.isSelected }.isMoreThanZero() || hasDateFilterApplied
            setInitialPointCard(shouldHidePointCard)
        }
    }

    fun updateDateFilterText(date: String) {
        val dateIndex = visitables.indexOfFirst { it is ItemPenaltySubsectionUiModel }
        visitables.find { it is ItemPenaltySubsectionUiModel }?.also {
            (it as ItemPenaltySubsectionUiModel).date = date
        }
        if (dateIndex != RecyclerView.NO_POSITION) {
            notifyItemChanged(dateIndex)
        }
    }

    fun setEmptyStatePenalty(pageType: String) {
        if (visitables.getOrNull(lastIndex) !is ItemPenaltyEmptyStateUiModel) {
            visitables.add(ItemPenaltyEmptyStateUiModel(pageType))
            notifyItemInserted(lastIndex)
        }
    }

    fun removeNotFoundPenalty() {
        if (visitables.getOrNull(lastIndex) is ItemPenaltyEmptyStateUiModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    fun setErrorStatePenalty(itemPenaltyErrorUiModel: ItemPenaltyErrorUiModel) {
        if (visitables.getOrNull(lastIndex) !is ItemPenaltyErrorUiModel) {
            visitables.add(itemPenaltyErrorUiModel)
            notifyItemInserted(lastIndex)
        }
    }

    fun removeErrorStatePenalty() {
        if (visitables.getOrNull(lastIndex) is ItemPenaltyErrorUiModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    fun removeShopPenaltyLoading() {
        if (visitables.getOrNull(lastIndex) is LoadingModel) {
            visitables.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    fun refreshSticky() {
        if (onStickySingleHeaderViewListener != null) {
            recyclerView?.post { onStickySingleHeaderViewListener?.refreshSticky() }
        }
    }

    fun removeRedDots() {
        val notificationUiModelIndex = visitables.indexOfFirst { it is ItemPenaltyInfoNotificationUiModel }
        visitables.find { it is ItemPenaltyInfoNotificationUiModel }?.also {
            if ((it as? ItemPenaltyInfoNotificationUiModel)?.shouldShowDot == true) {
                (it as? ItemPenaltyInfoNotificationUiModel)?.shouldShowDot = false
                if (notificationUiModelIndex != RecyclerView.NO_POSITION) {
                    notifyItemChanged(notificationUiModelIndex)
                }
            }
        }
    }

    private fun setInitialPointCard(shouldHide: Boolean) {
        if (shouldHide) {
            val cardIndex = visitables.indexOfFirst { it is ItemPenaltyPointCardUiModel }
            if (cardIndex > RecyclerView.NO_POSITION) {
                visitables.removeAt(cardIndex)
                notifyItemRemoved(cardIndex)
            }
        } else {
            val subsectionIndex = visitables.indexOfFirst { it is ItemSortFilterPenaltyUiModel }
            if (subsectionIndex > RecyclerView.NO_POSITION) {
                if (!visitables.any { it is ItemPenaltyPointCardUiModel }) {
                    initialPenaltyScoreCardUiModel?.let {
                        visitables.add(subsectionIndex, it)
                        notifyItemInserted(subsectionIndex)
                    }
                }
            }
        }
    }

    override val stickyHeaderPosition: Int
        get() = visitables.indexOfFirst { it is ItemSortFilterPenaltyUiModel }

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder? {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return penaltyPageAdapterFactory.createViewHolder(view, stickyViewType)
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is ItemSortFilterPenaltyViewHolder) {
            visitables.filterIsInstance(ItemSortFilterPenaltyUiModel::class.java).firstOrNull()
                ?.let {
                    viewHolder.bind(it)
                }
        }
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {
        this.onStickySingleHeaderViewListener = onStickySingleHeaderViewListener
    }

    override fun onStickyHide() {
        Handler(Looper.getMainLooper()).post {
            penaltySortFilterPosition?.let { notifyItemChanged(it) }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    companion object {
        const val PAYLOAD_SELECTED_FILTER = 508
    }
}

package com.tokopedia.shop.score.penalty.presentation.adapter

import android.os.Handler
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.shop.score.penalty.presentation.adapter.viewholder.ItemSortFilterPenaltyViewHolder
import com.tokopedia.shop.score.penalty.presentation.model.*
import com.tokopedia.shop.score.penalty.presentation.widget.OnStickySingleHeaderListener
import com.tokopedia.shop.score.penalty.presentation.widget.StickySingleHeaderView

class PenaltyPageAdapter(private val penaltyPageAdapterFactory: PenaltyPageAdapterFactory):
        BaseListAdapter<Visitable<*>, PenaltyPageAdapterFactory>(penaltyPageAdapterFactory),
        StickySingleHeaderView.OnStickySingleHeaderAdapter {

    private var onStickySingleHeaderViewListener: OnStickySingleHeaderListener? = null
    private var recyclerView: RecyclerView? = null

    private val penaltySortFilterPosition: Int?
        get() = visitables.indexOfFirst {
            it.javaClass == ItemSortFilterPenaltyUiModel::class.java
        }.takeIf { it != -1 }

    fun setPenaltyData(penaltyListUiModel: List<BasePenaltyPage>) {
        visitables.clear()
        visitables.addAll(penaltyListUiModel)
        notifyDataSetChanged()
    }

    fun removePenaltyListData() {
        val penaltyListCount = visitables.count { it is ItemPenaltyUiModel }
        visitables.removeAll { it is ItemPenaltyUiModel }
        notifyItemRangeRemoved(visitables.size, penaltyListCount)
    }

    fun updatePenaltyListData(penaltyListUiModel: List<BasePenaltyPage>) {
        visitables.addAll(penaltyListUiModel)
        notifyItemRangeInserted(visitables.size, penaltyListUiModel.size)
    }

    fun updateChipsSelected(chipsList: List<ItemSortFilterPenaltyUiModel.ItemSortFilterWrapper>,) {
        val updateIndex = visitables.indexOfFirst { it is ItemSortFilterPenaltyUiModel }
        visitables.filterIsInstance<ItemSortFilterPenaltyUiModel>().firstOrNull()?.itemSortFilterWrapperList = chipsList
        if (updateIndex != -1) {
            notifyItemChanged(updateIndex)
        }
    }

    fun updateDateFilterText(date: String) {
        val dateIndex = visitables.indexOfFirst { it is ItemPeriodDetailPenaltyUiModel }
        visitables.find { it is ItemPeriodDetailPenaltyUiModel }?.also {
            (it as ItemPeriodDetailPenaltyUiModel).periodDetail = date
        }
        if (dateIndex != -1) {
            notifyItemChanged(dateIndex, PAYLOAD_DATE_FILTER)
        }
    }

    fun setEmptyStatePenalty() {
        if (visitables.getOrNull(lastIndex) !is EmptyModel) {
            visitables.add(EmptyModel())
            notifyItemInserted(lastIndex)
        }
    }

    fun removeNotFoundPenalty() {
        if (visitables.getOrNull(lastIndex) is EmptyModel) {
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

    fun refreshSticky() {
        if (onStickySingleHeaderViewListener != null) {
            recyclerView?.post { onStickySingleHeaderViewListener?.refreshSticky() }
        }
    }

    override fun clearAllElements() {
        super.clearAllElements()
        refreshSticky()
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
            visitables.filterIsInstance(ItemSortFilterPenaltyUiModel::class.java).firstOrNull()?.let {
                viewHolder.bind(it)
            }
        }
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {
        this.onStickySingleHeaderViewListener = onStickySingleHeaderViewListener
    }

    override fun onStickyHide() {
        Handler().post {
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
        const val PAYLOAD_DATE_FILTER = 408
    }
}
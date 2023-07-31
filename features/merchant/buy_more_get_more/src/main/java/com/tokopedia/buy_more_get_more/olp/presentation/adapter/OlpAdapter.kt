package com.tokopedia.buy_more_get_more.olp.presentation.adapter

import android.os.Parcelable
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductSortingUiModel
import com.tokopedia.buy_more_get_more.olp.view.widget.OnStickySingleHeaderListener
import com.tokopedia.buy_more_get_more.olp.view.widget.StickySingleHeaderView

open class OlpAdapter(
    private val adapterTypeFactory: AdapterTypeFactory
): BaseListAdapter<Visitable<*>, AdapterTypeFactory>(adapterTypeFactory),
    StickySingleHeaderView.OnStickySingleHeaderAdapter{

    private var onStickySingleHeaderViewListener: OnStickySingleHeaderListener? = null
    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun getNewVisitableItems() = visitables.toMutableList()

    fun submitList(newList: List<Visitable<*>>) {
        val currentRecyclerViewState: Parcelable? = recyclerView?.layoutManager?.onSaveInstanceState()
        val diffCallback = OlpDiffUtilCallback(visitables, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
        currentRecyclerViewState?.let {
            recyclerView?.layoutManager?.onRestoreInstanceState(it)
        }
    }

    override val stickyHeaderPosition: Int
        get() = visitables.indexOfFirst {
            it::class.java == OfferProductSortingUiModel::class.java
        }

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val stickyViewType = getItemViewType(stickyHeaderPosition)
        val view = onCreateViewItem(parent, stickyViewType)
        return adapterTypeFactory.createViewHolder(view, stickyViewType)
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        if (viewHolder is OfferingProductSortingViewHolder) {
            visitables.filterIsInstance(OfferProductSortingUiModel::class.java).firstOrNull()?.let {
                viewHolder.bind(it)
            }
        }
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {
        this.onStickySingleHeaderViewListener = onStickySingleHeaderViewListener
    }

    override fun onStickyHide() {
        val newList = getNewVisitableItems()
        submitList(newList)
    }
}

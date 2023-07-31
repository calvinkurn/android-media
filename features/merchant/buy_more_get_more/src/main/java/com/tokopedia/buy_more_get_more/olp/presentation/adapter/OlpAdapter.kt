package com.tokopedia.buy_more_get_more.olp.presentation.adapter

import android.os.Parcelable
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
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
        get() = TODO("Not yet implemented")

    override fun createStickyViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder? {
        TODO("Not yet implemented")
    }

    override fun bindSticky(viewHolder: RecyclerView.ViewHolder?) {
        TODO("Not yet implemented")
    }

    override fun setListener(onStickySingleHeaderViewListener: OnStickySingleHeaderListener?) {
        TODO("Not yet implemented")
    }

    override fun onStickyHide() {
        TODO("Not yet implemented")
    }
}

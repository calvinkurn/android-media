package com.tokopedia.ordermanagement.snapshot.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder.SnapshotDetailsViewHolder
import com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder.SnapshotHeaderViewHolder
import com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder.SnapshotInfoViewHolder
import com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder.SnapshotShopViewHolder

/**
 * Created by fwidjaja on 1/15/21.
 */
class SnapshotAdapter : RecyclerView.Adapter<SnapshotAdapter.BaseViewHolder<*>>() {

    companion object {
        const val LAYOUT_HEADER = 0
        const val LAYOUT_INFO = 1
        const val LAYOUT_SHOP = 2
        const val LAYOUT_DETAILS = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            LAYOUT_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.uoh_loader_item, parent, false)
                SnapshotHeaderViewHolder(view)
            }
            LAYOUT_INFO -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.uoh_ticker_item, parent, false)
                SnapshotInfoViewHolder(view, actionListener)
            }
            LAYOUT_SHOP -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.uoh_list_item, parent, false)
                SnapshotShopViewHolder(view, actionListener)
            }
            LAYOUT_DETAILS -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.uoh_empty_state, parent, false)
                SnapshotDetailsViewHolder(view, actionListener)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}
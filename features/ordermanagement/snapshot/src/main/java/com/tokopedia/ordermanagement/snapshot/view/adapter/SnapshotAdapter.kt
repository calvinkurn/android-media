package com.tokopedia.ordermanagement.snapshot.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotResponse
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotTypeData
import com.tokopedia.ordermanagement.snapshot.util.SnapshotConsts.TYPE_CONTENT
import com.tokopedia.ordermanagement.snapshot.util.SnapshotConsts.TYPE_LOADER
import com.tokopedia.ordermanagement.snapshot.view.adapter.viewholder.*

/**
 * Created by fwidjaja on 1/15/21.
 */
class SnapshotAdapter : RecyclerView.Adapter<SnapshotAdapter.BaseViewHolder<*>>() {
    var listTypeData = mutableListOf<SnapshotTypeData>()
    var snapshotResponse = SnapshotResponse.Data.GetOrderSnapshot()

    companion object {
        const val LAYOUT_LOADER = 0
        const val LAYOUT_CONTENT = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            LAYOUT_LOADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.snapshot_loader_item, parent, false)
                SnapshotLoaderViewHolder(view)
            }
            LAYOUT_CONTENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.snapshot_content_item, parent, false)
                SnapshotContentViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T, position: Int)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = listTypeData[position]
        when (holder) {
            is SnapshotLoaderViewHolder -> {
                holder.bind(element, holder.adapterPosition)
            }
            is SnapshotContentViewHolder-> {
                holder.bind(element, holder.adapterPosition)
            }
            /*is SnapshotInfoViewHolder -> {
                holder.bind(element, holder.adapterPosition)
            }
            is SnapshotShopViewHolder-> {
                holder.bind(element, holder.adapterPosition)
            }
            is SnapshotDetailsViewHolder-> {
                holder.bind(element, holder.adapterPosition)
            }*/
        }
    }

    override fun getItemCount(): Int {
        return listTypeData.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (listTypeData[position].typeLayout) {
            TYPE_LOADER -> LAYOUT_LOADER
            TYPE_CONTENT -> LAYOUT_CONTENT
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    fun showContent() {
        listTypeData.clear()
        listTypeData.add(SnapshotTypeData(snapshotResponse, TYPE_CONTENT))
        notifyDataSetChanged()
    }

    fun showLoader() {
        listTypeData.clear()
        listTypeData.add(SnapshotTypeData("", TYPE_LOADER))
        notifyDataSetChanged()
    }
}
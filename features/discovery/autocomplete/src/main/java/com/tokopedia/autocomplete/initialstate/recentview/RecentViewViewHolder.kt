package com.tokopedia.autocomplete.initialstate.recentview

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.analytics.AutocompleteTracking
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import kotlinx.android.synthetic.main.layout_recent_view_item_autocomplete.view.*
import kotlinx.android.synthetic.main.layout_recyclerview_autocomplete.view.*

class RecentViewViewHolder(
        itemView: View,
        listener: InitialStateItemClickListener
) : AbstractViewHolder<RecentViewViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_recent_view_autocomplete
    }

    private val adapter: ItemAdapter

    init {
        val layoutManager = LinearLayoutManager(itemView.context,
                LinearLayoutManager.HORIZONTAL, false)
        itemView.recyclerView?.layoutManager = layoutManager
        ViewCompat.setLayoutDirection(itemView.recyclerView, ViewCompat.LAYOUT_DIRECTION_LTR)
        adapter = ItemAdapter(listener)
        itemView.recyclerView?.adapter = adapter
    }

    override fun bind(element: RecentViewViewModel) {
        adapter.setData(element.list)
    }

    private inner class ItemAdapter(private val clickListener: InitialStateItemClickListener) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
        private var data: List<BaseItemInitialStateSearch> = ArrayList()

        fun setData(data: List<BaseItemInitialStateSearch>) {
            this.data = data
            notifyItemRangeInserted(0, data.size)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_recent_view_item_autocomplete, parent, false)
            return ItemViewHolder(itemView, clickListener)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class ItemViewHolder(itemView: View, private val clickListener: InitialStateItemClickListener) : RecyclerView.ViewHolder(itemView) {
            fun bind(item: BaseItemInitialStateSearch) {
                ImageHandler.loadImageRounded2(
                        itemView.context,
                        itemView.autocompleteRecentViewItem,
                        item.imageUrl,
                        6.0f
                )
                itemView.autocompleteRecentViewItem?.setOnClickListener {
                    AutocompleteTracking.eventClickRecentView(
                            (adapterPosition + 1).toString(),
                            item
                    )
                    clickListener.onItemClicked(item.applink, item.url)
                }
            }
        }
    }
}
package com.tokopedia.autocompletecomponent.initialstate.recentview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutRecentViewAutocompleteBinding
import com.tokopedia.autocompletecomponent.databinding.LayoutRecentViewItemAutocompleteBinding
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocompletecomponent.util.loadImageRounded
import com.tokopedia.utils.view.binding.viewBinding

class RecentViewViewHolder(
    itemView: View,
    listener: RecentViewListener
) : AbstractViewHolder<RecentViewDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_recent_view_autocomplete
    }

    private val adapter: ItemAdapter = ItemAdapter(listener)
    private var binding: LayoutRecentViewAutocompleteBinding? by viewBinding()

    init {
        binding?.recyclerViewRecentView?.let { recyclerView ->
            val layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            recyclerView.layoutManager = layoutManager
            ViewCompat.setLayoutDirection(recyclerView, ViewCompat.LAYOUT_DIRECTION_LTR)
            recyclerView.adapter = adapter
        }
    }

    override fun bind(element: RecentViewDataView) {
        adapter.setData(element.list)
    }

    private inner class ItemAdapter(private val clickListener: RecentViewListener) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
        private var data: List<BaseItemInitialStateSearch> = ArrayList()

        fun setData(data: List<BaseItemInitialStateSearch>) {
            this.data = data
            notifyItemRangeInserted(0, data.size)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val itemView = LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.layout_recent_view_item_autocomplete,
                    parent,
                    false
                )
            return ItemViewHolder(itemView, clickListener)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class ItemViewHolder(
            itemView: View,
            private val recentViewListener: RecentViewListener,
        ) : RecyclerView.ViewHolder(itemView) {
            private var binding: LayoutRecentViewItemAutocompleteBinding? by viewBinding()

            fun bind(item: BaseItemInitialStateSearch) {
                val recentViewItem = binding?.autocompleteRecentViewItem ?: return

                recentViewItem.loadImageRounded(item.imageUrl)
                recentViewItem.setOnClickListener {
                    recentViewListener.onRecentViewClicked(item)
                }
            }

        }
    }
}

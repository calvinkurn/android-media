package com.tokopedia.autocomplete.initialstate.popularsearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.adapter.decorater.SpacingItemDecoration
import com.tokopedia.autocomplete.analytics.AutocompleteTracking
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import kotlinx.android.synthetic.main.layout_popular_item_autocomplete.view.*
import kotlinx.android.synthetic.main.layout_recyclerview_autocomplete.view.*

class PopularSearchViewHolder(
        itemView: View,
        listener: InitialStateItemClickListener
) : AbstractViewHolder<PopularSearchViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_popular_autocomplete
    }

    private val adapter: ItemAdapter

    init {
        val layoutManager = ChipsLayoutManager.newBuilder(itemView.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        val staticDimen8dp = itemView.context.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_8)
        itemView.recyclerView?.addItemDecoration(SpacingItemDecoration(staticDimen8dp))
        itemView.recyclerView?.layoutManager = layoutManager
        ViewCompat.setLayoutDirection(itemView.recyclerView, ViewCompat.LAYOUT_DIRECTION_LTR)
        adapter = ItemAdapter(listener)
        itemView.recyclerView?.adapter = adapter
    }

    override fun bind(element: PopularSearchViewModel) {
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
                    .inflate(R.layout.layout_popular_item_autocomplete, parent, false)
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
                itemView.autocompletePopularSearchItem?.chip_text?.text = item.title
                itemView.autocompletePopularSearchItem?.setOnClickListener {
                    AutocompleteTracking.eventClickPopularSearch(
                            itemView.context,
                            String.format(
                                    "value: %s - po: %s - applink: %s",
                                    item.title,
                                    (adapterPosition + 1).toString(),
                                    item.applink
                            )
                    )
                    clickListener.onItemClicked(item.applink, item.url)
                }
            }
        }
    }
}
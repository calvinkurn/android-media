package com.tokopedia.autocomplete.initialstate.recentview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.analytics.AutocompleteTracking
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import kotlinx.android.synthetic.main.layout_recent_view_item_autocomplete.view.*
import kotlinx.android.synthetic.main.layout_recyclerview_autocomplete.view.*

class RecentViewViewHolder(
        itemView: View,
        listener: InitialStateItemClickListener
) : AbstractViewHolder<RecentViewDataView>(itemView) {

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

    override fun bind(element: RecentViewDataView) {
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
                itemView.autocompleteRecentViewItem?.loadImageCircle(itemView.context, item.imageUrl)
                itemView.autocompleteRecentViewItem?.setOnClickListener {
                    AutocompleteTracking.eventClickRecentView(
                            (adapterPosition + 1).toString(),
                            item
                    )
                    clickListener.onItemClicked(item.applink, item.url)
                }
            }

            private fun ImageView.loadImageCircle(context: Context, url: String){
                Glide.with(context)
                        .load(url)
                        .transform(CenterCrop(), RoundedCorners(context.resources.getDimensionPixelSize(R.dimen.dp_6)))
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(this)
            }
        }
    }
}
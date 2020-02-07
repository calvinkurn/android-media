package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewmodel.EventGridViewModel
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_grid.view.*
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_grid_adapter_item.view.*

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventGridEventViewHolder(itemView: View): HomeEventViewHolder<EventGridViewModel>(itemView) {

    var itemAdapter = ItemAdapter()

    init {
        itemView.ent_recycle_view.apply {
            layoutManager = GridLayoutManager(itemView.context, 2)
            adapter = itemAdapter
        }
    }

    override fun bind(element: EventGridViewModel) {
        itemView.ent_title_card.text = element.title
        itemAdapter.items = element.items
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_event_grid
    }

    data class EventItemModel(var imageUrl: String,
                              var title : String,
                              var location: String,
                              var slashedPrice: String,
                              var price: String)

    class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

        class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        lateinit var items : List<EventItemModel>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_layout_viewholder_event_grid_adapter_item, parent, false)
            return ItemViewHolder(view)
        }
        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            Glide.with(holder.view).load(items.get(position).imageUrl).into(holder.view.image)
            holder.view.txt_location.text = items.get(position).location
            holder.view.txt_title.text = items.get(position).title
            holder.view.txt_price.text = items.get(position).price
        }
        override fun getItemCount() = items.size
    }

}
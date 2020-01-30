package com.tokopedia.entertainment.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.adapter.HomeViewHolder
import com.tokopedia.entertainment.adapter.viewmodel.EventCarouselViewModel
import kotlinx.android.synthetic.main.ent_layout_category_adapter_item.view.*
import kotlinx.android.synthetic.main.ent_layout_event_carousel_adapter_item.view.*
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_carouse.view.*

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventCarouselViewHolder(itemView: View): HomeViewHolder<EventCarouselViewModel>(itemView) {

    var itemAdapter = SimpleEventItemAdapter()

    init {
        itemView.ent_recycle_view.apply {
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = itemAdapter
        }
    }

    override fun bind(element: EventCarouselViewModel) {
        itemAdapter.items = element.items
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_event_carouse
    }

    data class EventItemModel(var imageUrl: String,
                              var title : String,
                              var location: String,
                              var price: String,
                              var date: String)

    class SimpleEventItemAdapter : RecyclerView.Adapter<SimpleEventItemAdapter.ItemViewHolder>() {

        class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        lateinit var items : List<EventItemModel>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_layout_event_carousel_adapter_item, parent, false)
            return ItemViewHolder(view)
        }
        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            Glide.with(holder.view).load(items.get(position).imageUrl).into(holder.view.event_image)
            holder.view.event_location.text = items.get(position).location
            holder.view.event_title.text = items.get(position).title
            holder.view.event_date.text = items.get(position).date
            holder.view.event_price.text = items.get(position).price
        }
        override fun getItemCount() = items.size
    }
}
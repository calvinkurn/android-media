package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.HomeViewHolder
import com.tokopedia.entertainment.home.adapter.viewmodel.EventLocationViewModel
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_location.view.*
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_location_adatper_item.view.*

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventLocationViewHolder(itemView: View): HomeViewHolder<EventLocationViewModel>(itemView) {

    var itemAdapter = ItemAdapter()

    init {
        itemView.ent_recycle_view.apply {
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = itemAdapter
        }
    }

    override fun bind(element: EventLocationViewModel) {
        itemView.ent_title_card.text = element.titleCard
        itemAdapter.items = element.items
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_event_location
    }

    data class EventItemModel(var imageUrl: String,
                              var title : String,
                              var tagline: String)

    class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

        class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        lateinit var items : List<EventItemModel>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_layout_viewholder_event_location_adatper_item, parent, false)
            return ItemViewHolder(view)
        }
        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            Glide.with(holder.view).load(items.get(position).imageUrl).into(holder.view.image)
            holder.view.txt_title.text = items.get(position).title
            holder.view.txt_subtitle.text = items.get(position).tagline
        }
        override fun getItemCount() = items.size
    }

}
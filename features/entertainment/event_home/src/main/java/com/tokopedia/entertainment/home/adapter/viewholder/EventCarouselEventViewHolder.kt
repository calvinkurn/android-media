package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewmodel.EventCarouselViewModel
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_carouse.view.*
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_carousel_adapter_item.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventCarouselEventViewHolder(itemView: View): HomeEventViewHolder<EventCarouselViewModel>(itemView) {

    var itemAdapter = ItemAdapter()

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

    class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

        class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        lateinit var items : List<EventItemModel>
        var sdf = SimpleDateFormat("dd/MM/yy")
        var newsdf = SimpleDateFormat("dd\nMMM")

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_layout_viewholder_event_carousel_adapter_item, parent, false)
            return ItemViewHolder(view)
        }
        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            Glide.with(holder.view).load(items.get(position).imageUrl).into(holder.view.event_image)
            holder.view.event_location.text = items.get(position).location
            holder.view.event_title.text = items.get(position).title
            holder.view.event_date.text = formatedSchedule(items.get(position).date)
            holder.view.event_price.text = items.get(position).price
        }

        private fun formatedSchedule(schedule: String): String? {
            return try {
                val date: Date
                date = if (schedule.contains("-")) {
                    val arr = schedule.split("-").toTypedArray()
                    sdf.parse(arr[0].trim { it <= ' ' })
                } else {
                    sdf.parse(schedule)
                }
                newsdf.format(date).toUpperCase()
            } catch (e: Exception) {
                ""
            }
        }

        override fun getItemCount() = items.size
    }


}
package com.tokopedia.entertainment.search.adapter.viewholder

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.search.analytics.EventCategoryPageTracking
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import kotlinx.android.synthetic.main.ent_search_event_grid_item.view.*

class EventGridAdapter(val listener: EventGridListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listEvent: MutableList<EventGrid> = mutableListOf()
    var isLoading = false
    val VIEW_TYPE_ITEM = 1
    val VIEW_TYPE_LOADING = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == VIEW_TYPE_ITEM) return EventGridViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.ent_search_event_grid_item, parent, false))

        else return ProgressBarViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.ent_search_progressbar, parent, false))
    }

    override fun getItemCount(): Int = if(isLoading) listEvent.size+1 else listEvent.size

    override fun getItemViewType(position: Int): Int {
        return if(isLoading && position >= listEvent.size) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(listEvent.size > 0 && position < listEvent.size){
            val event = listEvent.get(position)
            with((holder as EventGridViewHolder).view) {
                Glide.with(context)
                        .load(event.image_url)
                        .centerCrop()
                        .into(image)

                txt_location.text = event.location
                txt_title.text = event.nama_event
                if (event.harga_start.startsWith("Rp")) { //Saat diskon beri stroke
                    txt_start_title.paintFlags = txt_start_title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                txt_start_title.text = event.harga_start
                txt_price.text = event.harga_now

                addOnImpressionListener(event, {
                    listener.impressionCategory(event, listEvent, position)
                })

                setOnClickListener {
                    listener.clickCategory(event, listEvent, position)
                    RouteManager.route(holder.view.context, event.app_url)
                    //TODO Open new Activity

                }
            }
        }
    }

    class EventGridViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    class ProgressBarViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    data class EventGrid(
            val id: String,
            val image_url: String,
            val location: String,
            val nama_event: String,
            val harga_start: String,
            val harga_now: String,
            val app_url: String,
            val category_id: String = ""
    ) : ImpressHolder()

    interface EventGridListener{
        fun impressionCategory(event: EventGridAdapter.EventGrid,
                               listsEvent: List<EventGridAdapter.EventGrid>,
                               position: Int)
        fun clickCategory(event: EventGridAdapter.EventGrid,
                          listsEvent: List<EventGridAdapter.EventGrid>,
                          position: Int)
    }
}
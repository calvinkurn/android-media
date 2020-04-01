package com.tokopedia.entertainment.search.adapter.viewholder

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.entertainment.search.R
import com.tokopedia.entertainment.search.analytics.EventCategoryPageTracking
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import kotlinx.android.synthetic.main.ent_search_event_grid_item.view.*

class EventGridAdapter : RecyclerView.Adapter<EventGridAdapter.EventGridViewHolder>() {

    var listEvent: MutableList<EventGrid> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventGridViewHolder {
        return EventGridViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.ent_search_event_grid_item, parent, false))
    }

    override fun getItemCount(): Int = listEvent.size

    override fun onBindViewHolder(holder: EventGridViewHolder, position: Int) {
        val event = listEvent.get(position)
        with(holder.view) {
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
                EventCategoryPageTracking.getInstance().impressionGridViewProduct(event, listEvent, position + 1)
            })

            setOnClickListener {
                //TODO Open new Activity
                EventCategoryPageTracking.getInstance().onClickGridViewProduct(event, listEvent, position + 1)
            }
        }
    }

    class EventGridViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    data class EventGrid(
            val id: String,
            val image_url: String,
            val location: String,
            val nama_event: String,
            val harga_start: String,
            val harga_now: String
    ) : ImpressHolder()

    companion object {
        val LAYOUT = R.layout.ent_search_event_grid
    }
}
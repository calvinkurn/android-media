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
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemLocationModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventLocationViewModel
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_location.view.*
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_location_adaper_item.view.*

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventLocationEventViewHolder(itemView: View) : HomeEventViewHolder<EventLocationViewModel>(itemView) {

    var itemAdapter = InnerItemAdapter()

    init {
        itemView.ent_recycle_view.apply {
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = itemAdapter
        }
    }

    override fun bind(element: EventLocationViewModel) {
        itemAdapter.items = element.items
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_event_location
        val TAG = EventLocationEventViewHolder::class.java.simpleName
    }

    class InnerItemAdapter : RecyclerView.Adapter<InnerViewHolder>() {

        lateinit var items: List<EventItemLocationModel>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_layout_viewholder_event_location_adaper_item, parent, false)
            return InnerViewHolder(view)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            Glide.with(holder.view).load(items.get(position).imageUrl).into(holder.view.image)
            holder.view.txt_title.text = items.get(position).title
            holder.view.txt_subtitle.text = items.get(position).tagline
            holder.view.addOnImpressionListener(items.get(position), {
                EventHomePageTracking.getInstance().impressionLocationEvent(items.get(position), items,
                        position + 1)
            })
            holder.view.setOnClickListener {
                EventHomePageTracking.getInstance().clickLocationEvent(items.get(position), items,
                        position + 1)
            }
        }

        override fun getItemCount() = items.size
    }

    class InnerViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}
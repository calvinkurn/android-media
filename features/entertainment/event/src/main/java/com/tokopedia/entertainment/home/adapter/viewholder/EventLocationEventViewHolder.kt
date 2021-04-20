package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.listener.TrackingListener
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemLocationModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventLocationModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_location.view.*
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_location_adaper_item.view.*

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventLocationEventViewHolder(itemView: View,
                                   val locationListener: TrackingListener) : HomeEventViewHolder<EventLocationModel>(itemView) {

    var itemAdapter = InnerItemAdapter(locationListener)

    init {
        itemView.ent_recycle_view_location.apply {
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = itemAdapter
        }
    }

    override fun bind(element: EventLocationModel) {
        itemAdapter.items = element.items
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_event_location
        val TAG = EventLocationEventViewHolder::class.java.simpleName
    }

    class InnerItemAdapter(val locationListener: TrackingListener) : RecyclerView.Adapter<InnerViewHolder>() {

        lateinit var items: List<EventItemLocationModel>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_layout_viewholder_event_location_adaper_item, parent, false)
            return InnerViewHolder(view)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            var item = items.get(position)
            Glide.with(holder.view).load(item.imageUrl).into(holder.view.image)
            holder.view.txt_title.text = item.title
            holder.view.txt_subtitle.text = item.tagline
            holder.view.addOnImpressionListener(item, {
                locationListener.impressionLocationEvent(item, items,
                        position + 1)
            })
            holder.view.setOnClickListener {
                locationListener.clickLocationEvent(item, items,
                        position + 1)
                RouteManager.route(holder.view.context,
                        ApplinkConstInternalEntertainment.EVENT_CATEGORY, "", item.id, item.title)
            }
        }

        override fun getItemCount() = items.size
    }

    class InnerViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}
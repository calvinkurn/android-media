package com.tokopedia.entertainment.home.adapter.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewmodel.EventCarouselViewModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemModel
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking
import com.tokopedia.entertainment.home.fragment.EventHomeFragment
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_carouse.view.*
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_carousel_adapter_item.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventCarouselEventViewHolder(itemView: View, action: ((data: EventItemModel,
                                                             onSuccess: (EventItemModel) -> Unit,
                                                             onError: (Throwable) -> Unit) -> Unit))
    : HomeEventViewHolder<EventCarouselViewModel>(itemView) {

    var itemAdapter = ItemAdapter(action)

    init {
        itemView.ent_recycle_view.apply {
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL,
                    false)
            adapter = itemAdapter
        }
        itemView.ent_btn_see_more.setOnClickListener {
            EventHomePageTracking.getInstance().clickSeeAllTopEventProduct()
        }
    }

    override fun bind(element: EventCarouselViewModel) {
        itemAdapter.items = element.items
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_event_carouse
        val TAG = EventCarouselEventViewHolder::class.java.simpleName
    }

    class ItemAdapter(val action: (data: EventItemModel,
                                   onSuccess: (EventItemModel) -> Unit,
                                   onError: (Throwable) -> Unit) -> Unit)
        : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

        class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        lateinit var items: List<EventItemModel>
        var sdf = SimpleDateFormat("dd/MM/yy")
        var newsdf = SimpleDateFormat("dd\nMMM")

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_layout_viewholder_event_carousel_adapter_item, parent,
                            false)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            var item = items.get(position)
            Glide.with(holder.view).load(item.imageUrl).into(holder.view.event_image)
            holder.view.event_location.text = item.location
            holder.view.event_title.text = item.title
            holder.view.event_date.text = formatedSchedule(item.date)
            holder.view.event_price.text = item.price
            if (item.isLiked) {
                holder.view.iv_favorite.setImageResource(R.drawable.ent_ic_wishlist_active)
            } else {
                holder.view.iv_favorite.setImageResource(R.drawable.ent_ic_wishlist_inactive)
            }
            holder.view.setOnClickListener {
                RouteManager.route(holder.view.context, item.appUrl)
                EventHomePageTracking.getInstance().clickTopEventProduct(item, position + 1)
            }
            holder.view.addOnImpressionListener(item, {
                Log.d(EventGridEventViewHolder.TAG, "Impression On "+item.title)
            })
            holder.view.iv_favorite.setOnClickListener {
                action.invoke(item, ::onSuccessPostLiked, ::onErrorPostLiked)
            }
        }

        fun onSuccessPostLiked(data: EventItemModel) {
            notifyDataSetChanged()
        }

        fun onErrorPostLiked(throwable: Throwable) {
            notifyDataSetChanged()
            Log.e(TAG, throwable.localizedMessage)
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
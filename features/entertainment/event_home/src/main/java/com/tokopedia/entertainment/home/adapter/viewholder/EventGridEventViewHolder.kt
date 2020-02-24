package com.tokopedia.entertainment.home.adapter.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewmodel.EventGridViewModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemModel
import com.tokopedia.entertainment.home.analytics.EventHomePageTracking
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_grid.view.*
import kotlinx.android.synthetic.main.ent_layout_viewholder_event_grid_adapter_item.view.*

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventGridEventViewHolder(itemView: View, action: ((data: EventItemModel,
                                                         onSuccess: (EventItemModel) -> Unit,
                                                         onError: (Throwable) -> Unit) -> Unit))
    : HomeEventViewHolder<EventGridViewModel>(itemView) {

    var itemAdapter = ItemAdapter(action)

    init {
        itemView.ent_recycle_view.apply {
            layoutManager = GridLayoutManager(itemView.context, 2)
            adapter = itemAdapter
        }
    }

    override fun bind(element: EventGridViewModel) {
        itemView.ent_title_card.text = element.title
        itemAdapter.items = element.items
        itemView.btn_see_all.setOnClickListener {
            EventHomePageTracking.getInstance().clickSeeAllCuratedEventProduct()
        }
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_event_grid
        val TAG = EventGridEventViewHolder::class.java.simpleName
    }

    class ItemAdapter(val action: (data: EventItemModel,
                                   onSuccess: (EventItemModel) -> Unit,
                                   onError: (Throwable) -> Unit) -> Unit)
        : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

        class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        lateinit var items: List<EventItemModel>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_layout_viewholder_event_grid_adapter_item, parent,
                            false)
            return ItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            var item = items.get(position)
            Glide.with(holder.view).load(item.imageUrl).into(holder.view.image)
            holder.view.txt_location.text = item.location
            holder.view.txt_title.text = item.title
            holder.view.txt_price.text = item.price
            if (item.isLiked) {
                holder.view.iv_favorite.setImageResource(R.drawable.ent_ic_wishlist_active)
            } else {
                holder.view.iv_favorite.setImageResource(R.drawable.ent_ic_wishlist_inactive)
            }
            holder.view.setOnClickListener {
                RouteManager.route(holder.view.context, item.appUrl)
                EventHomePageTracking.getInstance().clickSectionEventProduct(item, items,
                        position + 1)
            }
            holder.view.addOnImpressionListener(item, {
                EventHomePageTracking.getInstance().impressionSectionEventProduct(item, items,
                        position + 1)
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

        override fun getItemCount() = items.size
    }

}
package com.tokopedia.entertainment.home.adapter.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.ItemUtilCallback
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

    var itemAdapter = InnerItemAdapter(action)

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
            RouteManager.route(itemView.context, ApplinkConstInternalEntertainment.EVENT_CATEGORY,
                    element.id)
            EventHomePageTracking.getInstance().clickSeeAllCuratedEventProduct(element.title,
                    adapterPosition + 1)
        }
    }

    companion object {
        @LayoutRes
        @kotlin.jvm.JvmField
        var LAYOUT: Int = R.layout.ent_layout_viewholder_event_grid
        val TAG = EventGridEventViewHolder::class.java.simpleName
    }

    class InnerItemAdapter(val action: (data: EventItemModel,
                                        onSuccess: (EventItemModel) -> Unit,
                                        onError: (Throwable) -> Unit) -> Unit)
        : RecyclerView.Adapter<InnerViewHolder>() {

        lateinit var items: List<EventItemModel>
        var pos: Int = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_layout_viewholder_event_grid_adapter_item, parent,
                            false)
            return InnerViewHolder(view)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            var item = items.get(position)
            this.pos = position
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
            notifyItemChanged(pos)
        }

        fun onErrorPostLiked(throwable: Throwable) {
            notifyItemChanged(pos)
            Log.e(TAG, throwable.localizedMessage)
        }

        override fun getItemCount() = items.size
    }

    class InnerViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}
package com.tokopedia.entertainment.home.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalEntertainment
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntLayoutViewholderEventLocationAdaperItemBinding
import com.tokopedia.entertainment.databinding.EntLayoutViewholderEventLocationBinding
import com.tokopedia.entertainment.home.adapter.listener.TrackingListener
import com.tokopedia.entertainment.home.adapter.viewmodel.EventItemLocationModel
import com.tokopedia.entertainment.home.adapter.viewmodel.EventLocationModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Author errysuprayogi on 27,January,2020
 */
class EventLocationEventViewHolder(itemView: View,
                                   locationListener: TrackingListener)
    : AbstractViewHolder<EventLocationModel>(itemView) {

    var itemAdapter = InnerItemAdapter(locationListener)
    private val binding: EntLayoutViewholderEventLocationBinding? by viewBinding()

    init {
        binding?.entRecycleViewLocation?.run {
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
    }

    class InnerItemAdapter(private val locationListener: TrackingListener) : RecyclerView.Adapter<InnerViewHolder>() {

        lateinit var items: List<EventItemLocationModel>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            val binding = EntLayoutViewholderEventLocationAdaperItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return InnerViewHolder(binding, locationListener, items)
        }

        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            return holder.bind(items[position])
        }

        override fun getItemCount() = items.size
    }

    class InnerViewHolder(private val binding: EntLayoutViewholderEventLocationAdaperItemBinding, val locationListener: TrackingListener, var items: List<EventItemLocationModel>) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EventItemLocationModel) {
            with(binding) {
                image.loadImage(item.imageUrl)
                txtTitle.text = item.title
                txtSubtitle.text = item.tagline
                root.addOnImpressionListener(item) {
                    locationListener.impressionLocationEvent(
                        item, items,
                        position + Int.ONE
                    )
                }
                root.setOnClickListener {
                    locationListener.clickLocationEvent(item, items,
                        position + Int.ONE)
                    RouteManager.route(root.context,
                        ApplinkConstInternalEntertainment.EVENT_CATEGORY, "", item.id, item.title)
                }
            }
        }
    }

}

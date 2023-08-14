package com.tokopedia.entertainment.search.adapter.viewholder

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntSearchEventGridItemBinding
import com.tokopedia.entertainment.databinding.EntSearchProgressbarBinding
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.media.loader.loadImage

class EventGridAdapter(val listener: EventGridListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listEvent: MutableList<EventGrid> = mutableListOf()
    var isLoading = false
    val VIEW_TYPE_ITEM = 1
    val VIEW_TYPE_LOADING = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == VIEW_TYPE_ITEM) return EventGridViewHolder(EntSearchEventGridItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), listEvent, listener)

        else return ProgressBarViewHolder(EntSearchProgressbarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int = if(isLoading) listEvent.size+1 else listEvent.size

    override fun getItemViewType(position: Int): Int {
        return if(isLoading && position >= listEvent.size) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is EventGridViewHolder) {
            holder.bind(listEvent[position])
        }
    }

    class EventGridViewHolder(val binding: EntSearchEventGridItemBinding, var listEvent: MutableList<EventGrid>, val listener: EventGridListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventGrid) {
            with(binding) {
                image.loadImage(event.image_url)

                txtLocation.text = event.location
                txtTitle.text = event.nama_event
                if (event.harga_start.startsWith("Rp")) {
                    txtStartTitle.paintFlags = txtStartTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                txtStartTitle.apply {
                    if (event.isFree){
                        gone()
                    } else {
                        show()
                        text = event.harga_start
                    }
                }
                txtPrice.apply {
                    if (event.isFree){
                        text = resources.getString(R.string.ent_free_price)
                    } else {
                        text = event.harga_now
                    }
                }

                root.addOnImpressionListener(event, {
                    listener.impressionCategory(event, listEvent, position)
                })

                root.setOnClickListener {
                    listener.clickCategory(event, listEvent, position)
                    RouteManager.route(root.context, event.app_url)
                }
            }
        }
    }
    class ProgressBarViewHolder(val binding: EntSearchProgressbarBinding) : RecyclerView.ViewHolder(binding.root)

    data class EventGrid(
            val id: String,
            val image_url: String,
            val location: String,
            val nama_event: String,
            val harga_start: String,
            val harga_now: String,
            val app_url: String,
            val category_id: String = "",
            val isFree: Boolean = false
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

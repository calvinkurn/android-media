package com.tokopedia.entertainment.pdp.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.entertainment.R
import com.tokopedia.kotlin.extensions.view.loadImage

class WidgetEventPDPCarouselAdapter(private val images: MutableList<String>, private val clickListener: WidgetEventPDPCarousel.ImageViewPagerListener?) :
        RecyclerView.Adapter<WidgetEventPDPCarouselAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_event_pdp_image_slider, parent, false))

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (images[position] != null && images[position].isNotEmpty()) {
            if (clickListener != null) holder.bannerImage.setOnClickListener { clickListener.onImageClicked(position) }
        }
        holder.bannerImage.loadImage(images.get(position), R.drawable.ic_loading_event)
    }

    fun addImages(list: List<String>) {
        images.clear()
        images.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val bannerImage: ImageView = view.findViewById(R.id.image_banner)
    }
}
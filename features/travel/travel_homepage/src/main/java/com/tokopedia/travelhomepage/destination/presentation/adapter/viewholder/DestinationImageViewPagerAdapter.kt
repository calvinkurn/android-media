package com.tokopedia.travelhomepage.destination.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.widget.DestinationImageViewPager

/**
 * @author by jessica on 16/04/19
 */

class DestinationImageViewPagerAdapter(val images: MutableList<String>, val clickListener: DestinationImageViewPager.ImageViewPagerListener?) : RecyclerView.Adapter<DestinationImageViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_image_slider, parent, false))

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (images.get(position).isNotEmpty()) {
            if (clickListener != null) holder.bannerImage.setOnClickListener { clickListener.onImageClicked(position) }
        }
        holder.bannerImage.loadImage(images.get(position), R.drawable.travel_homepage_ic_loading_image)
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


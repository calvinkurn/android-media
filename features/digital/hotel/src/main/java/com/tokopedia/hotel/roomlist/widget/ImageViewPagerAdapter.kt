package com.tokopedia.hotel.roomlist.widget

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.hotel.R
import com.tokopedia.kotlin.extensions.view.loadImage

/**
 * @author by jessica on 16/04/19
 */

class ImageViewPagerAdapter(val images: MutableList<String>, val clickListener: ImageViewPager.ImageViewPagerListener?): RecyclerView.Adapter<ImageViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_image_slider, parent, false))

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (images.get(position) != null && images.get(position).length > 0) {
            if (clickListener != null) holder.bannerImage.setOnClickListener{ clickListener.onImageClicked(position) }
        }
        holder.bannerImage.loadImage(images.get(position), R.drawable.ic_loading_image)
    }

    fun addImages(list: List<String>) {
        images.clear()
        images.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val bannerImage: ImageView = view.findViewById(R.id.image_banner)
    }

}


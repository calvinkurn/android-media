package com.tokopedia.hotel.roomlist.widget

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.hotel.R

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
        try {
            Glide.with(holder.itemView.context)
                    .load(images.get(position))
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(com.tokopedia.design.R.drawable.ic_loading_image)
                    .error(com.tokopedia.design.R.drawable.ic_loading_image)
                    .centerCrop()
                    .into(holder.bannerImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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


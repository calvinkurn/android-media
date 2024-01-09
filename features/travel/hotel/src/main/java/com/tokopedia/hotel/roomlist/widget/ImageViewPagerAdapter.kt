package com.tokopedia.hotel.roomlist.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.R as unifycomponentsR

/**
 * @author by jessica on 16/04/19
 */

class ImageViewPagerAdapter(private val images: MutableList<String>, private val clickListener: ImageViewPager.ImageViewPagerListener?) : RecyclerView.Adapter<ImageViewPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_hotel_image_slider, parent, false))

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (images.size <= position) return

        val imageUrl = images[position]
        if (imageUrl.isNotEmpty()) {
            holder.bannerImage.loadImage(imageUrl){
                setPlaceHolder(unifycomponentsR.drawable.imagestate_placeholder)
            }
            holder.bannerImage.setOnClickListener {
                clickListener?.onImageClicked(holder.bannerImage, position)
            }
        }
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


package com.tokopedia.imagepreviewslider.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepreviewslider.R
import com.tokopedia.imagepreviewslider.presentation.listener.ImageSliderListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy

/**
 * @author by resakemal on 03/05/19
 */

class ImagePreviewSliderAdapter(val images: MutableList<String>,
                                imagePosition: Int,
                                val clickListener: ImageSliderListener): RecyclerView.Adapter<ImagePreviewSliderAdapter.ViewHolder>() {

    var selectedPos = imagePosition

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image_preview_slider, parent, false))

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bannerImage.setOnClickListener {
            clickListener.onImageClicked(position)
            selectedPos = position
        }

        try {
            holder.bannerImage.loadImage(images[position]) {
                setCacheStrategy(MediaCacheStrategy.ALL)
                setPlaceHolder(R.drawable.ic_loading_image)
                setErrorDrawable(R.drawable.ic_loading_image)
                centerCrop()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.view.isSelected = selectedPos == position

        if (selectedPos == position) {
            holder.bannerImage.setBackgroundResource(R.drawable.bg_highlight_border)
        } else {
            holder.bannerImage.background = null
        }
    }

    fun setSelectedImage(position: Int) {
        notifyItemChanged(selectedPos)
        selectedPos = position
        notifyItemChanged(selectedPos)
    }

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val bannerImage: ImageView = view.findViewById(R.id.image_banner)

        init {
            itemView.setOnClickListener {
                setSelectedImage(layoutPosition)
            }
        }
    }

}


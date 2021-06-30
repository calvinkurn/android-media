package com.tokopedia.gallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gallery.adapter.viewholder.ImageSliderViewHolder
import com.tokopedia.gallery.adapter.viewholder.LoadingSliderViewHolder
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import java.util.*

class SliderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val imageReviewItemList = ArrayList<ImageReviewItem>()
    var isLoadingItemEnabled = true
        private set

    val galleryItemCount: Int
        get() = imageReviewItemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageSliderViewHolder) {
            holder.bind(imageReviewItemList[position], position == imageReviewItemList.lastIndex)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(viewType, parent, false)
        return if (viewType == ImageSliderViewHolder.LAYOUT) {
            ImageSliderViewHolder(view)
        } else {
            LoadingSliderViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return if (isLoadingItemEnabled) imageReviewItemList.size + 1 else imageReviewItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGalleryItem(position)) {
            ImageSliderViewHolder.LAYOUT
        } else {
            LoadingSliderViewHolder.LAYOUT
        }
    }

    fun appendItems(imageReviewItems: List<ImageReviewItem>) {
        imageReviewItemList.addAll(imageReviewItems)
        notifyDataSetChanged()
    }

    fun resetState() {
        imageReviewItemList.clear()
        isLoadingItemEnabled = true
        notifyDataSetChanged()
    }

    fun isGalleryItem(position: Int): Boolean {
        return position < imageReviewItemList.size
    }

    fun removeLoading() {
        isLoadingItemEnabled = false
        notifyDataSetChanged()
    }

    fun addLoading() {
        isLoadingItemEnabled = true
        notifyDataSetChanged()
    }
}
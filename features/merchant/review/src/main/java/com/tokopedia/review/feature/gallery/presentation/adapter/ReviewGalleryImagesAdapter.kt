package com.tokopedia.review.feature.gallery.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.feature.gallery.presentation.adapter.viewholder.ReviewGalleryImagesViewHolder
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryImageListener

class ReviewGalleryImagesAdapter(private val imageListener: ReviewGalleryImageListener) : RecyclerView.Adapter<ReviewGalleryImagesViewHolder>() {

    private var images: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewGalleryImagesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_review_gallery_image, parent, false)
        return ReviewGalleryImagesViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ReviewGalleryImagesViewHolder, position: Int) {
        holder.bind(images[position], imageListener)
    }

    fun setData(images: List<String>) {
        this.images = images
    }

    fun reloadImageAtIndex(index: Int) {
        notifyItemChanged(index)
    }
}
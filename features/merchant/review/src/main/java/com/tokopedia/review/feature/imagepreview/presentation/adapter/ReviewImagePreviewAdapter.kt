package com.tokopedia.review.feature.imagepreview.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.feature.imagepreview.presentation.adapter.viewholder.ReviewImagePreviewViewHolder
import com.tokopedia.review.feature.imagepreview.presentation.listener.ReviewImagePreviewListener

class ReviewImagePreviewAdapter(private val imagePreviewListener: ReviewImagePreviewListener) : RecyclerView.Adapter<ReviewImagePreviewViewHolder>() {

    private var images: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewImagePreviewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_review_image_preview, parent, false)
        return ReviewImagePreviewViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ReviewImagePreviewViewHolder, position: Int) {
        holder.bind(images[position], imagePreviewListener)
    }

    fun setData(images: List<String>) {
        this.images = images
    }

    fun reloadImageAtIndex(index: Int) {
        notifyItemChanged(index)
    }
}
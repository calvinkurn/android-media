package com.tokopedia.review.common.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.common.presentation.adapter.viewholder.ReviewAttachedProductViewHolder

class ReviewAttachedImagesAdapter : RecyclerView.Adapter<ReviewAttachedProductViewHolder>() {

    private var attachedImages: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAttachedProductViewHolder {
        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.item_review_attached_image, parent, false)
        return ReviewAttachedProductViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return attachedImages.size
    }

    override fun onBindViewHolder(holder: ReviewAttachedProductViewHolder, position: Int) {
        holder.bind(attachedImages[position])
    }

    fun setData(attachedImages: List<String>) {
        val updatedImages = attachedImages.toMutableList()
        updatedImages.add("")
        this.attachedImages = updatedImages
        notifyDataSetChanged()
    }
}
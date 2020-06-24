package com.tokopedia.review.feature.inbox.history.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.feature.inbox.history.presentation.adapter.viewholder.ReviewHistoryAttachedProductViewHolder

class ReviewHistoryAttachedProductAdapter : RecyclerView.Adapter<ReviewHistoryAttachedProductViewHolder>() {

    private var attachedImages: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHistoryAttachedProductViewHolder {
        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.item_review_history_attached_image, parent, false)
        return ReviewHistoryAttachedProductViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return attachedImages.size
    }

    override fun onBindViewHolder(holder: ReviewHistoryAttachedProductViewHolder, position: Int) {
        holder.bind(attachedImages[position])
    }

    fun setData(attachedImages: List<String>) {
        val updatedImages = attachedImages.toMutableList()
        updatedImages.add("")
        this.attachedImages = updatedImages
        notifyDataSetChanged()
    }
}
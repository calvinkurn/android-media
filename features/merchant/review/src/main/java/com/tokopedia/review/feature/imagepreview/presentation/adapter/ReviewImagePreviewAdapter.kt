package com.tokopedia.review.feature.imagepreview.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.feature.imagepreview.presentation.adapter.viewholder.ReviewImagePreviewViewHolder
import com.tokopedia.review.feature.imagepreview.presentation.listener.ReviewImagePreviewListener
import com.tokopedia.review.feature.imagepreview.presentation.uimodel.ReviewImagePreviewUiModel

class ReviewImagePreviewAdapter(private val imagePreviewListener: ReviewImagePreviewListener) : RecyclerView.Adapter<ReviewImagePreviewViewHolder>() {

    private var images: MutableList<ReviewImagePreviewUiModel> = mutableListOf()

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

    fun addData(images: List<ReviewImagePreviewUiModel>) {
        this.images.addAll(images)
    }

    fun reloadImageAtIndex(index: Int) {
        notifyItemChanged(index)
    }
}
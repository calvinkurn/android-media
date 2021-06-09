package com.tokopedia.review.common.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.R
import com.tokopedia.review.common.data.ProductrevReviewAttachment
import com.tokopedia.review.common.presentation.adapter.viewholder.ReviewAttachedProductViewHolder
import com.tokopedia.review.common.presentation.util.ReviewAttachedImagesClickListener
import com.tokopedia.review.feature.inbox.history.presentation.util.ReviewHistoryItemListener

class ReviewAttachedImagesAdapter(private val imageClickListener: ReviewAttachedImagesClickListener,
                                  private val productName: String,
                                  private val reviewHistoryItemListener: ReviewHistoryItemListener? = null,
                                  private val productId: Long? = null,
                                  private val feedbackId: Long? = null) : RecyclerView.Adapter<ReviewAttachedProductViewHolder>() {

    private var attachedImages: List<String> = listOf()
    private var fullSizeImages: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAttachedProductViewHolder {
        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.item_review_attached_image, parent, false)
        return ReviewAttachedProductViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return attachedImages.size
    }

    override fun onBindViewHolder(holder: ReviewAttachedProductViewHolder, position: Int) {
        holder.bind(attachedImages[position], imageClickListener, fullSizeImages,
                productName, reviewHistoryItemListener, productId, feedbackId)
    }

    fun setData(attachedImages: List<ProductrevReviewAttachment>) {
        val updatedImages = attachedImages.map {
            it.thumbnail
        }.toMutableList()
        fullSizeImages = attachedImages.map {
            it.fullSize
        }
        updatedImages.add(0, "")
        this.attachedImages = updatedImages
        notifyDataSetChanged()
    }
}
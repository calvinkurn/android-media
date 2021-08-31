package com.tokopedia.review.feature.reading.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.data.ProductReviewAttachments
import com.tokopedia.review.feature.reading.presentation.adapter.viewholder.ReadReviewAttachedImageViewHolder
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewAttachedImagesListener

class ReadReviewAttachedImagesAdapter(private val imageClickListener: ReadReviewAttachedImagesListener) : RecyclerView.Adapter<ReadReviewAttachedImageViewHolder>() {

    private var attachedImages: List<String> = listOf()
    private var productReview: ProductReview = ProductReview()
    private var shopId: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadReviewAttachedImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(com.tokopedia.review.inbox.R.layout.item_review_attached_image, parent, false)
        return ReadReviewAttachedImageViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return attachedImages.size
    }

    override fun onBindViewHolder(holder: ReadReviewAttachedImageViewHolder, position: Int) {
        holder.bind(attachedImages[position], imageClickListener, productReview, shopId)
    }

    fun setData(attachedImages: List<ProductReviewAttachments>, productReview: ProductReview, shopId: String) {
        val updatedImages = attachedImages.map {
            it.imageThumbnailUrl
        }.toMutableList()
        updatedImages.add(0, "")
        this.attachedImages = updatedImages
        this.productReview = productReview
        this.shopId = shopId
        notifyDataSetChanged()
    }
}
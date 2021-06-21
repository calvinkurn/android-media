package com.tokopedia.review.feature.reading.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.feature.reading.data.ProductReview
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewAttachedImagesListener
import com.tokopedia.unifycomponents.ImageUnify

class ReadReviewAttachedImageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private var blankSpace: View? = null
    private var attachedImage: ImageUnify? = null

    init {
        bindViews()
    }

    private fun bindViews() {
        blankSpace = view.findViewById(R.id.reviewHistoryAttachedImageBlankSpace)
        attachedImage = view.findViewById(R.id.reviewHistoryAttachedImage)
    }

    fun bind(attachedImageUrl: String, listener: ReadReviewAttachedImagesListener, productReview: ProductReview, shopId: String) {
        if (attachedImageUrl.isEmpty()) {
            itemView.apply {
                blankSpace?.show()
                attachedImage?.hide()
            }
            return
        }
        itemView.apply {
            blankSpace?.hide()
            attachedImage?.show()
            attachedImage?.loadImage(attachedImageUrl)
            setOnClickListener {
                listener.onAttachedImagesClicked(productReview, adapterPosition, shopId)
            }
        }
    }
}
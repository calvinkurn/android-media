package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.view.View
import com.tokopedia.review.feature.createreputation.model.DefaultImageReviewModel
import com.tokopedia.review.feature.createreputation.presentation.listener.ImageClickListener

class DefaultImageReviewViewHolder(val view: View, private val imageClickListener: ImageClickListener?) : BaseImageReviewViewHolder<DefaultImageReviewModel>(view) {

    override fun bind(element: DefaultImageReviewModel) {
        view.setOnClickListener {
            imageClickListener?.onAddImageClick()
        }
    }
}
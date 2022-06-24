package com.tokopedia.review.feature.createreputation.presentation.viewholder.old

import android.view.View
import com.tokopedia.review.feature.createreputation.model.DefaultImageReviewUiModel
import com.tokopedia.review.feature.createreputation.presentation.listener.ImageClickListener

class DefaultImageReviewViewHolder(val view: View, private val imageClickListener: ImageClickListener?) : BaseImageReviewViewHolder<DefaultImageReviewUiModel>(view) {

    override fun bind(element: DefaultImageReviewUiModel) {
        view.setOnClickListener {
            imageClickListener?.onAddImageClick()
        }
    }
}
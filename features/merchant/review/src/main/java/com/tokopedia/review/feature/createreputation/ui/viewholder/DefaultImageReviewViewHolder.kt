package com.tokopedia.review.feature.createreputation.ui.viewholder

import android.view.View
import com.tokopedia.review.feature.createreputation.model.DefaultImageReviewModel
import com.tokopedia.review.feature.createreputation.ui.listener.OnAddImageClickListener

class DefaultImageReviewViewHolder(val view: View, private val onAddImageClickListener: OnAddImageClickListener?) : BaseImageReviewViewHolder<DefaultImageReviewModel>(view) {

    override fun bind(element: DefaultImageReviewModel) {
        view.setOnClickListener {
            onAddImageClickListener?.onAddImageClick()
        }
    }
}
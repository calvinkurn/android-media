package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.viewholder

import android.view.View
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.DefaultImageReviewViewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.listener.OnAddImageClickListener

class DefaultImageReviewViewHolder(val view: View, private val onAddImageClickListener: OnAddImageClickListener?) : BaseImageReviewViewHolder<DefaultImageReviewViewModel>(view) {

    override fun bind(element: DefaultImageReviewViewModel) {
        view.setOnClickListener {
            onAddImageClickListener?.onAddImageClick()
        }
    }
}
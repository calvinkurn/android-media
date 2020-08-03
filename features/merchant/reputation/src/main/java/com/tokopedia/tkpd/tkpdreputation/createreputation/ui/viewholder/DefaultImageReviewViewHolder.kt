package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.viewholder

import android.view.View
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.DefaultImageReviewUiModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.listener.OnAddImageClickListener

class DefaultImageReviewViewHolder(val view: View, private val onAddImageClickListener: OnAddImageClickListener?) : BaseImageReviewViewHolder<DefaultImageReviewUiModel>(view) {

    override fun bind(element: DefaultImageReviewUiModel) {
        view.setOnClickListener {
            onAddImageClickListener?.onAddImageClick()
        }
    }
}
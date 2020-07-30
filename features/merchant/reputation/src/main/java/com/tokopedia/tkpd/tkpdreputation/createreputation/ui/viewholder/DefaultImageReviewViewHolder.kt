package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.viewholder

import android.view.View
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.DefaultImageReviewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.listener.OnAddImageClickListener

class DefaultImageReviewViewHolder(val view: View, private val onAddImageClickListener: OnAddImageClickListener?) : BaseImageReviewViewHolder<DefaultImageReviewModel>(view) {

    override fun bind(element: DefaultImageReviewModel) {
        view.setOnClickListener {
            onAddImageClickListener?.onAddImageClick()
        }
    }
}
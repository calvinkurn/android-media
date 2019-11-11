package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.viewholder

import android.view.View
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.DefaultImageReviewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.util.OnAddImageClick

class DefaultImageReviewViewHolder(val view: View, val addDataClick: OnAddImageClick) : BaseImageReviewViewHolder<DefaultImageReviewModel>(view) {

    override fun bind(element: DefaultImageReviewModel) {
        view.setOnClickListener {
            addDataClick.invoke()
        }
    }
}
package com.tokopedia.tkpd.tkpdreputation.createreputation.ui.viewholder

import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.tkpd.tkpdreputation.R
import com.tokopedia.tkpd.tkpdreputation.createreputation.model.ImageReviewViewModel
import com.tokopedia.tkpd.tkpdreputation.createreputation.ui.listener.OnAddImageClickListener
import kotlinx.android.synthetic.main.item_image_chooser_review_old.view.*

class ImageReviewViewHolder(val view: View, private val onAddImageClickListener: OnAddImageClickListener?) : BaseImageReviewViewHolder<ImageReviewViewModel>(view) {
    override fun bind(element: ImageReviewViewModel) {
        view.setOnClickListener {
            onAddImageClickListener?.onAddImageClick()
        }
        if (adapterPosition == 3 && element.shouldDisplayOverlay) {
            view.review_container_overlay.visibility = View.VISIBLE
            view.txt_other_img_overlay.text = view.context.getString(R.string.review_overlay_count, element.otherImageCount)
        } else {
            view.review_container_overlay.visibility = View.GONE
        }

        ImageHandler.loadImageRounded(view.context, view.image_review, element.imageUrl, 10F)
    }

}
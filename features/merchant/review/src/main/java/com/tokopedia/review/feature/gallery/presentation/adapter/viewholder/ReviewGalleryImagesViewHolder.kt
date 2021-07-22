package com.tokopedia.review.feature.gallery.presentation.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryImageListener
import com.tokopedia.unifycomponents.ImageUnify

class ReviewGalleryImagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var image: ImageUnify? = null
    private var background: ConstraintLayout? = null

    init {
        image = view.findViewById(R.id.review_gallery_image)
        background = view.findViewById(R.id.review_gallery_image_background)
    }

    fun bind(imageUrl: String, imageListener: ReviewGalleryImageListener) {
        image?.apply {
            setOnClickListener {
                imageListener.onImageClicked()
            }
            this@ReviewGalleryImagesViewHolder.background?.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_N75))
            loadImage(imageUrl) {
                listener(
                    onSuccess = { _, _ ->
                        this@ReviewGalleryImagesViewHolder.background?.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                android.R.color.transparent
                            )
                        )
                    },
                    onError = {
                        imageListener.onImageLoadFailed(adapterPosition)
                    }
                )
            }
        }
    }
}
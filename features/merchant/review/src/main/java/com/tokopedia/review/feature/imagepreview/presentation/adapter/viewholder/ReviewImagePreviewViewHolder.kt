package com.tokopedia.review.feature.imagepreview.presentation.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.image_gallery.ImagePreview
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.feature.imagepreview.presentation.listener.ReviewImagePreviewListener

class ReviewImagePreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        const val ZOOM_SCALE_FACTOR = 2f
        const val UNZOOM_SCALE_FACTOR = 1f
    }

    private var image: ImagePreview? = null
    private var background: ConstraintLayout? = null

    init {
        image = view.findViewById(R.id.review_gallery_image_preview)
    }

    fun bind(imageUrl: String, imagePreviewListener: ReviewImagePreviewListener) {
        image?.apply {
            mLoaderView.hide()
            mImageView.apply {
                image?.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifycomponents.R.color.Unify_N75
                    )
                )
                loadImage(imageUrl) {
                    listener(
                        onSuccess = { _, _ ->
                            mLoaderView.hide()
                            image?.setBackgroundColor(
                                ContextCompat.getColor(
                                    context,
                                    android.R.color.transparent
                                )
                            )
                        },
                        onError = {
                            mLoaderView.hide()
                            imagePreviewListener.onImageLoadFailed(adapterPosition)
                        }
                    )
                }
            }
            onImageClickListener = {
                imagePreviewListener.onImageClicked()
            }
            onImageDoubleClickListener = {
                if (mScaleFactor == UNZOOM_SCALE_FACTOR) {
                    setScaleFactor(ZOOM_SCALE_FACTOR)
                } else {
                    setScaleFactor(UNZOOM_SCALE_FACTOR)
                }
            }
            imagePreviewUnifyListener = object : ImagePreview.ImagePreviewUnifyListener {
                override fun onZoomStart(scaleFactor: Float) {

                }

                override fun onZoom(scaleFactor: Float) {

                }

                override fun onZoomEnd(scaleFactor: Float) {
                    if (scaleFactor > UNZOOM_SCALE_FACTOR) {
                        imagePreviewListener.disableScroll()
                    } else {
                        imagePreviewListener.enableScroll()
                    }
                }
            }
        }
    }
}
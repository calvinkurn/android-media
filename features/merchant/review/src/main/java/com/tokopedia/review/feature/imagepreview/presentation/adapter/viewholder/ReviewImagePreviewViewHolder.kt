package com.tokopedia.review.feature.imagepreview.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.image_gallery.ImagePreview
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.feature.imagepreview.presentation.listener.ReviewImagePreviewListener
import com.tokopedia.review.feature.imagepreview.presentation.uimodel.ReviewImagePreviewUiModel

class ReviewImagePreviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        const val ZOOM_SCALE_FACTOR = 2f
        const val UNZOOM_SCALE_FACTOR = 1f
    }

    private var image: ImagePreview? = null

    init {
        image = view.findViewById(R.id.review_gallery_image_preview)
    }

    fun bind(
        imagePreviewUiModel: ReviewImagePreviewUiModel,
        imagePreviewListener: ReviewImagePreviewListener
    ) {
        image?.apply {
            mLoaderView.hide()
            setLoadingBehavior(imagePreviewUiModel, imagePreviewListener)
            setOnClickListener(imagePreviewListener)
            setDoubleClickListener()
            addOnImpressionListener(imagePreviewUiModel.impressHolder) {
                imagePreviewListener.onImageImpressed()
            }
            setZoomListener(imagePreviewListener)
        }
    }

    private fun setLoadingBehavior(
        imagePreviewUiModel: ReviewImagePreviewUiModel,
        imagePreviewListener: ReviewImagePreviewListener
    ) {
        image?.mImageView?.apply {
            image?.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifycomponents.R.color.Unify_N75
                )
            )
            loadImage(imagePreviewUiModel.imageUrl) {
                listener(
                    onSuccess = { _, _ ->
                        image?.mLoaderView?.hide()
                        image?.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                android.R.color.transparent
                            )
                        )
                    },
                    onError = {
                        image?.mLoaderView?.hide()
                        imagePreviewListener.onImageLoadFailed(adapterPosition, it)
                    }
                )
            }
        }
    }

    private fun setOnClickListener(imagePreviewListener: ReviewImagePreviewListener) {
        image?.onImageClickListener = {
            imagePreviewListener.onImageClicked()
        }
    }

    private fun setDoubleClickListener() {
        image?.apply {
            onImageDoubleClickListener = {
                if (mScaleFactor == UNZOOM_SCALE_FACTOR) {
                    setScaleFactor(ZOOM_SCALE_FACTOR)
                } else {
                    setScaleFactor(UNZOOM_SCALE_FACTOR)
                }
            }
        }
    }

    private fun setZoomListener(imagePreviewListener: ReviewImagePreviewListener) {
        image?.imagePreviewUnifyListener = object : ImagePreview.ImagePreviewUnifyListener {
            override fun onZoomStart(scaleFactor: Float) {
                imagePreviewListener.disableScroll()
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
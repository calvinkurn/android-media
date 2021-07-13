package com.tokopedia.review.feature.gallery.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.image_gallery.ImagePreview
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryImageListener

class ReviewGalleryImagesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        const val ZOOM_SCALE_FACTOR = 2f
        const val UNZOOM_SCALE_FACTOR = 1f
    }

    private var image: ImagePreview? = null

    init {
        image = view.findViewById(R.id.review_gallery_image)
    }

    fun bind(imageUrl: String, imageListener: ReviewGalleryImageListener) {
        image?.apply {
            mLoaderView.hide()
            mImageView.loadImage(imageUrl)
            setOnClickListener {
                imageListener.onImageClicked()
            }
            onImageDoubleClickListener = {
                if (mScaleFactor == UNZOOM_SCALE_FACTOR) {
                    setScaleFactor(ZOOM_SCALE_FACTOR)
                } else {
                    setScaleFactor(UNZOOM_SCALE_FACTOR)
                }
            }
            imagePreviewUnifyListener = object: ImagePreview.ImagePreviewUnifyListener {
                override fun onZoomStart(scaleFactor: Float) {
                }
                override fun onZoom(scaleFactor: Float) {

                }
                override fun onZoomEnd(scaleFactor: Float) {
                    if(scaleFactor >= UNZOOM_SCALE_FACTOR) {
                        imageListener.disableScroll()
                    } else {
                        imageListener.enableScroll()
                    }
                }
            }
        }
    }
}
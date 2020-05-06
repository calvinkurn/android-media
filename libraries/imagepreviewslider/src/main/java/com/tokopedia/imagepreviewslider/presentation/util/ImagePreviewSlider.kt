package com.tokopedia.imagepreviewslider.presentation.util

import android.content.Context
import android.os.Build
import android.widget.ImageView
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.imagepreviewslider.presentation.view.ImagePreviewViewer

/**
 * @author by jessica on 2019-12-16
 * Android OS < 19 will use ImagePreviewSliderActivity
 * Android OS >= 19 will use ImagePreviewViewer with pinch image feature (zoom)
 */

class ImagePreviewSlider {

    companion object {
        @JvmStatic
        val instance by lazy { ImagePreviewSlider() }
    }

    fun start(context: Context?, title: String = "", imageUrls: List<String>,
              imageThumbnailUrls: List<String>, imagePosition: Int = 0, imageViewTransitionFrom: ImageView?) {
        context?.let {
            if (Build.VERSION.SDK_INT < 19) {
                it.run {
                    startActivity(ImagePreviewSliderActivity.getCallingIntent(it, title, imageUrls, imageThumbnailUrls, imagePosition))
                }
            } else {
                ImagePreviewViewer.instance.startImagePreviewViewer(title, imageViewTransitionFrom, imageUrls, it, imagePosition)
            }
        }
    }
}
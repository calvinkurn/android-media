package com.tokopedia.media.loader

import android.widget.ImageView
import com.tokopedia.media.loader.common.Properties

object JvmMediaLoader {
    @JvmStatic fun loadImage(imageView: ImageView, url: String) {
        imageView.loadImage(url)
    }

    @JvmStatic fun loadImageFitCenter(imageView: ImageView, url: String) {
        imageView.loadImageFitCenter(url)
    }

    @JvmStatic fun loadImage(imageView: ImageView, url: String, properties: Properties.() -> Unit) {
        imageView.loadImage(url, properties)
    }
}
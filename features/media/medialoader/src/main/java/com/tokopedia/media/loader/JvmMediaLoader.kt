package com.tokopedia.media.loader

import android.graphics.Bitmap
import android.widget.ImageView
import com.tokopedia.media.loader.data.MediaException
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.listener.MediaListener
import com.tokopedia.media.loader.wrapper.MediaDataSource

/**
 * A static function to loading any images and video thumbnail for java-based.
 *
 * The media-loader module is designed to be a media loader for the Kotlin-first module.
 * The module provides an extension-syntax to ensure the page owner who already adopting a Kotlin is
 * able to fetch the image or video thumbnail easily.
 *
 * As we still have a few Java-based, thus we provided [JvmMediaLoader] static function to help optimize
 * the image loader right the same experience as the extension one.
 */
object JvmMediaLoader {
    @JvmStatic fun loadImage(imageView: ImageView, url: String) {
        imageView.loadImage(url)
    }

    @JvmStatic fun loadImage(imageView: ImageView, resource: Int) {
        imageView.loadImage(resource)
    }

    @JvmStatic fun loadImageFitCenter(imageView: ImageView, url: String) {
        imageView.loadImageFitCenter(url)
    }

    @JvmStatic fun loadImage(imageView: ImageView, url: String, properties: Properties.() -> Unit) {
        imageView.loadImage(url, properties)
    }

    @JvmStatic
    fun loadImage(
        imageView: ImageView,
        url: String,
        listener: MediaListener
    ) {
        imageView.loadImage(url) {
            this.loaderListener = listener
        }
    }
}

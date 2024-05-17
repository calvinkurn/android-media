package com.tokopedia.media.loader

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.tokopedia.media.loader.data.MediaException
import com.tokopedia.media.loader.data.Properties
import com.tokopedia.media.loader.listener.MediaListener
import com.tokopedia.media.loader.module.GlideApp
import com.tokopedia.media.loader.wrapper.MediaDataSource
import java.io.File

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

    @JvmStatic fun loadImageWithCacheData(imageView: ImageView, url: String) {
        imageView.loadImageWithCacheData(url)
    }

    @JvmStatic fun loadImageFitCenter(imageView: ImageView, url: String) {
        imageView.loadImageFitCenter(url)
    }

    @JvmStatic fun loadImageRounded(imageView: ImageView, url: String, radius: Float) {
        imageView.loadImageRounded(url, radius)
    }

    @JvmStatic fun loadImage(imageView: ImageView, url: String, properties: Properties.() -> Unit) {
        imageView.loadImage(url, properties)
    }

    @JvmStatic fun loadImage(imageView: ImageView, uri: Uri, properties: Properties.() -> Unit) {
        imageView.loadImage(uri, properties)
    }

    @JvmStatic fun loadGif(
        imageView: ImageView,
        url: String,
        properties: Properties.() -> Unit,
        onSuccess: (GifDrawable?, MediaDataSource?) -> Unit,
        onError: (MediaException?) -> Unit
    ) {
        imageView.loadAsGif(url){
            this.apply(properties)
            listener(
                onSuccessGif = {gifDrawable, mediaDataSource ->
                    onSuccess(gifDrawable, mediaDataSource)
                },
                onError = {
                    onError(it)
                }
            )
        }
    }

    @JvmStatic
    fun loadImage(
        imageView: ImageView,
        url: String,
        properties: Properties.() -> Unit,
        onSuccess: (Bitmap?, MediaDataSource?, Boolean) -> Unit,
        onError: (MediaException?) -> Unit
    ) {
        imageView.loadImage(url) {
            this.apply(properties)
            this.listener(
                onSuccessWithResource = {bitmap, mediaDataSource, isFirstResource ->
                    onSuccess(bitmap, mediaDataSource, isFirstResource)
                },
                onError = { exception ->
                    onError(exception)
                }
            )
        }
    }

    @JvmStatic
    fun getBitmapImageUrl(
        context: Context,
        url: String,
        properties: Properties.() -> Unit,
        onSuccess: (Bitmap?, MediaDataSource?, Boolean) -> Unit,
        onError: (MediaException?) -> Unit
    ) {
        url.getBitmapImageUrl(context, {
            this.apply(properties)
            listener(
                onSuccessWithResource = {bitmap, mediaDataSource, isFirstResource ->
                    onSuccess(bitmap, mediaDataSource, isFirstResource)
                },
                onError = {
                    onError(it)
                }
            )
        })
    }

    @JvmStatic
    fun downloadBitmapFromUrl(
        context: Context,
        url: String,
        properties: Properties.() -> Unit
    ): File? {
        return url.downloadImageFromUrl(context, properties = properties)
    }

    @JvmStatic
    fun getBitmapImageUrl(
        context: Context,
        url: String,
        timeout: Long,
        properties: Properties.() -> Unit
    ): Bitmap?{
        return url.getBitmapFromUrl(context, timeout, properties = properties)
    }

    @JvmStatic
    fun clearImage(imageView: ImageView?) {
        if (imageView != null && imageView.context.isValid()) {
            GlideApp.with(imageView.context).clear(imageView)
        }
    }
}

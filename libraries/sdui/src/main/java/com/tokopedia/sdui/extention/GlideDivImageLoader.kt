package com.tokopedia.sdui.extention

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.tokopedia.media.loader.clear
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.media.loader.getGifDrawable
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.media.loader.wrapper.MediaDataSource
import com.yandex.div.core.images.BitmapSource
import com.yandex.div.core.images.CachedBitmap
import com.yandex.div.core.images.DivImageDownloadCallback
import com.yandex.div.core.images.DivImageLoader
import com.yandex.div.core.images.LoadReference

class GlideDivImageLoader(
    private val context: Context
) : DivImageLoader {

    override fun loadImage(imageUrl: String, callback: DivImageDownloadCallback): LoadReference {
        val imageUri = Uri.parse(imageUrl)
        // create target to be able to cancel loading
        val target = MediaBitmapEmptyTarget<Bitmap>()

        // load result will be handled by RequestListener to get dataSource
        imageUri.getBitmapImageUrl(context.applicationContext, {
            listener(
                onSuccess = {bitmap, mediaDataSource ->
                    bitmap?.let {  bitmapResult ->
                        mediaDataSource?.let { sourceResult ->
                            val dataSource = MediaDataSource.mapTo(sourceResult)
                            callback.onSuccess((CachedBitmap(bitmapResult, imageUri, dataSource.toBitmapSource())))
                        }
                    }
                },
                onError = {
                    callback.onError()
                }
            )
        }, target)

        return LoadReference {
            target.clear(context.applicationContext)
        }
    }

    override fun loadImage(imageUrl: String, imageView: ImageView): LoadReference {
        val imageUri = Uri.parse(imageUrl)

        imageView.loadImage(imageUri)

        return LoadReference {
            imageView.clearImage()
        }
    }

    override fun loadImageBytes(imageUrl: String, callback: DivImageDownloadCallback): LoadReference {
        val imageUri = Uri.parse(imageUrl)
        // create target to be able to cancel loading
        val target = MediaBitmapEmptyTarget<GifDrawable>()

        // load result will be handled by RequestListener to get dataSource
        imageUri.getGifDrawable(context.applicationContext, {
            listener(
                onSuccessGif = { gifDrawable, mediaDataSource ->
                    gifDrawable?.let {  gifDrawableResult ->
                        mediaDataSource?.let { mediaDatasourceResult ->
                            val dataSource = MediaDataSource.mapTo(mediaDatasourceResult)
                            val imageBytes = ByteArray(gifDrawableResult.buffer.remaining())
                            val byteArray = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            val bitmap = CachedBitmap(byteArray, imageBytes, imageUri, dataSource.toBitmapSource())
                            callback.onSuccess(bitmap)
                        }
                    }
                },
                onError = {
                    callback.onError()
                }
            )
        }, target)

        return LoadReference {
            target.clear(context.applicationContext)
        }
    }
}

private fun DataSource.toBitmapSource(): BitmapSource {
    return when (this) {
        DataSource.REMOTE -> BitmapSource.NETWORK
        DataSource.LOCAL -> BitmapSource.DISK
        else -> BitmapSource.MEMORY // here can be DataSource.DISK_CACHE or DataSource.MEMORY_CACHE
    }
}

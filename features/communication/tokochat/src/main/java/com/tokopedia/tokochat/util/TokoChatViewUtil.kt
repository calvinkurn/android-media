package com.tokopedia.tokochat.util

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.InputStream

object TokoChatViewUtil {

    fun loadByteArrayImage(
        context: Context,
        imageView: ImageView,
        inputStream: InputStream,
        onFinishLoading: () -> Unit
    ) {
        try {
            Glide.with(context)
                .asBitmap()
                .load(inputStream.readBytes())
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(getRequestListenerByteArray(inputStream, onFinishLoading))
                .into(imageView)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            closeInputStream(inputStream)
        }
    }

    private fun getRequestListenerByteArray(
        inputStream: InputStream,
        onFinishLoading: () -> Unit
    ): RequestListener<Bitmap> {
        return object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                onFinishLoading()
                closeInputStream(inputStream)
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onFinishLoading()
                closeInputStream(inputStream)
                return false
            }
        }
    }

    private fun closeInputStream(inputStream: InputStream) {
        try {
            inputStream.close()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }
}

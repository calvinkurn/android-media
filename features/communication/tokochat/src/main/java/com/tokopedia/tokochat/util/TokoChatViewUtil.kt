package com.tokopedia.tokochat.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.config.GlobalConfig
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.EIGHT_DP
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.file.FileUtil
import java.io.File
import java.io.InputStream

object TokoChatViewUtil {

    private const val TOKOCHAT_RELATIVE_PATH = "/TokoChat"
    private const val JPEG_EXT = ".jpeg"

    fun downloadAndSaveByteArrayImage(
        context: Context,
        fileName: String,
        inputStream: InputStream,
        onImageReady: (File?) -> Unit,
        onError: () -> Unit,
        onDirectLoad: () -> Unit,
        imageView: ImageView?
    ) {
        try {
            val imageResult: File? = writeImageToTokoChatPath(inputStream.readBytes(), fileName)
            onImageReady(imageResult)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            loadByteArrayImage(context, imageView, inputStream, onError, onDirectLoad)
        } finally {
            closeInputStream(inputStream)
        }
    }

    private fun closeInputStream(inputStream: InputStream) {
        try {
            inputStream.close()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    private fun writeImageToTokoChatPath(buffer: ByteArray?, fileName: String): File? {
        if (buffer != null) {
            val photo: File = getTokoChatPhotoPath(fileName)
            if (photo.exists()) {
                photo.delete()
            }
            if (FileUtil.writeBufferToFile(buffer, photo.path)) {
                return photo
            }
        }
        return null
    }

    fun getTokoChatPhotoPath(fileName: String): File {
        return File(getInternalCacheDirectory().absolutePath, fileName + JPEG_EXT)
    }

    fun getInternalCacheDirectory(): File {
        val directory = File(GlobalConfig.INTERNAL_CACHE_DIR, TOKOCHAT_RELATIVE_PATH)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }

    // Handle fail save image and directly load the image

    private fun loadByteArrayImage(
        context: Context,
        imageView: ImageView?,
        inputStream: InputStream,
        onError: () -> Unit,
        onDirectLoad: () -> Unit,
    ) {
        try {
            if (imageView != null) {
                directLoadImageOnUiThread(context, imageView, inputStream, onError, onDirectLoad)
            } else {
                onError()
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            closeInputStream(inputStream)
        }
    }

    private fun directLoadImageOnUiThread(
        context: Context,
        imageView: ImageView,
        inputStream: InputStream,
        onError: () -> Unit,
        onDirectLoad: () -> Unit,
    ) {
        Handler(Looper.getMainLooper()).post {
            Glide.with(context)
                .asBitmap()
                .placeholder(com.tokopedia.tokochat_common.R.drawable.tokochat_bg_image_bubble_gradient)
                .error(com.tokopedia.tokochat_common.R.drawable.tokochat_bg_image_bubble_gradient)
                .load(inputStream.readBytes())
                .transform(CenterCrop(), RoundedCorners(EIGHT_DP.toPx()))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(getRequestListenerByteArray(inputStream, onError, onDirectLoad))
                .into(imageView)
        }
    }

    private fun getRequestListenerByteArray(
        inputStream: InputStream,
        onError: () -> Unit,
        onDirectLoad: () -> Unit
    ): RequestListener<Bitmap> {
        return object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                onError()
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
                onDirectLoad()
                closeInputStream(inputStream)
                return false
            }
        }
    }
}

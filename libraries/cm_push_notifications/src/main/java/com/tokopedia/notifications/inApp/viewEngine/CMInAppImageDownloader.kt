package com.tokopedia.notifications.inApp.viewEngine

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tokopedia.notifications.common.CMNotificationUtils
import com.tokopedia.notifications.image.downloaderFactory.PARENT_DIR
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

const val PNG_QUALITY = 95

abstract class CMInAppImageDownloader(val cmInApp: CMInApp) {

    abstract suspend fun downloadImages(context: Context): CMInApp

    protected abstract suspend fun verifyAndUpdate()

    protected fun downloadAndStore(context: Context, url: String, imageSizeAndTimeout: ImageSizeAndTimeout): String? {
        val bitmap = downloadImage(context, url, imageSizeAndTimeout)
        return bitmap?.let {
            storeBitmapToFile(context, bitmap)
        }
    }

    private fun downloadImage(context: Context, url: String, imageSizeAndTimeout: ImageSizeAndTimeout): Bitmap? {
        try {
            return Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .override(imageSizeAndTimeout.width, imageSizeAndTimeout.height)
                    .submit(imageSizeAndTimeout.width, imageSizeAndTimeout.height)
                    .get(imageSizeAndTimeout.seconds, TimeUnit.SECONDS)
        } catch (e: CancellationException) {
        } catch (e: ExecutionException) {
        } catch (e: InterruptedException) {
        } catch (e: TimeoutException) {
        } catch (e: IOException) {
        }
        return null
    }

    private fun storeBitmapToFile(context: Context, bitmap: Bitmap): String? {
        var fileSaved = false
        val parentDirectory = getParentImageDirectory(context)
        val directory = File("${parentDirectory.absolutePath}/" +
                "${cmInApp.id}_${cmInApp.endTime}")
        if (!directory.exists())
            directory.mkdir()
        val imagePath = File(directory, "${System.currentTimeMillis()}.png")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(imagePath)
            bitmap.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, fos)
            fileSaved = true
        } catch (e: Exception) {
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
            }
        }
        return if (fileSaved)
            imagePath.absolutePath
        else
            null
    }

    private fun getParentImageDirectory(context: Context): File {
        val cw = ContextWrapper(context.applicationContext)
        val internalDirectory = cw.getDir(PARENT_DIR, Context.MODE_PRIVATE)
        if (!internalDirectory.exists())
            internalDirectory.mkdir()
        return internalDirectory
    }

}


class ImageSizeAndTimeout(internal val width: Int, internal val height: Int, internal val seconds: Long) {
    companion object {
        fun getBigImageSize(context: Context): ImageSizeAndTimeout {
            val metrics = DisplayMetrics()
            (context as AppCompatActivity).windowManager?.defaultDisplay?.getMetrics(metrics)
            return ImageSizeAndTimeout(metrics.widthPixels, metrics.heightPixels, 10L)
        }

        fun getCenterImageSize(context: Context): ImageSizeAndTimeout {
            val metrics = DisplayMetrics()
            (context as AppCompatActivity).windowManager?.defaultDisplay?.getMetrics(metrics)
            return ImageSizeAndTimeout(metrics.widthPixels, metrics.widthPixels / 2, 10L)
        }

        fun getAlertImageSize(context: Context): ImageSizeAndTimeout {
            val dp80 = CMNotificationUtils.getPXtoDP(context, 80F)
            return ImageSizeAndTimeout(dp80.toInt(), dp80.toInt(), 5L)
        }
    }
}
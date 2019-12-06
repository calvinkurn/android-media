package com.tokopedia.notifications.image.downloaderFactory

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.tokopedia.notifications.common.CarouselUtilities
import com.tokopedia.notifications.model.BaseNotificationModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

const val PARENT_DIR = "CM_RESOURCE"
const val PNG_QUALITY = 95

abstract class NotificationImageDownloader(val baseNotificationModel: BaseNotificationModel) {

    abstract suspend fun downloadImages(context: Context): BaseNotificationModel?

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
        } catch (e: Exception) {
        }
        return null
    }

    private fun storeBitmapToFile(context: Context, bitmap: Bitmap): String? {
        var fileSaved = false
        val parentDirectory = getParentImageDirectory(context)
        val directory = File("${parentDirectory.absolutePath}/" +
                "${baseNotificationModel.notificationId}_${baseNotificationModel.endTime}")
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
                fos?.close()
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


enum class ImageSizeAndTimeout(val width: Int, val height: Int, val seconds: Long) {
    ACTION_BUTTON_ICON(100, 100, 3L),
    BIG_IMAGE(720, 360, 10L),
    PRODUCT_IMAGE(360, 360, 5L),
    CAROUSEL(720, 360, 10L),
    VISUAL_COLLAPSED(360, 64, 5L),
    VISUAL_EXPANDED(720, 360, 10L),
    BANNER_COLLAPSED(180, 64, 5L)
}
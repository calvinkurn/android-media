package com.tokopedia.notifications.image.downloaderFactory

import android.content.Context
import android.content.ContextWrapper
import android.graphics.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.notifications.model.BaseNotificationModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

const val PARENT_DIR = "CM_RESOURCE"
const val PNG_QUALITY = 95

abstract class NotificationImageDownloader(val baseNotificationModel: BaseNotificationModel) {

    abstract suspend fun downloadAndVerify(context: Context): BaseNotificationModel?

    protected abstract suspend fun verifyAndUpdate()

    protected fun downloadAndStore(
            context: Context, url: Any?,
            properties: ImageSizeAndTimeout,
            rounded: Int = 0
    ): String? {
        val bitmap = url?.let { downloadImage(context, url, rounded, properties) }
        return bitmap?.let { storeBitmapToFile(context, bitmap) }
    }

    private fun downloadImage(
            context: Context,
            url: Any,
            rounded: Int,
            properties: ImageSizeAndTimeout
    ): Bitmap? {
        try {
            val bitmap =  Glide.with(context)
                    .asBitmap()
                    .load(url).apply {
                        if (rounded != 0) {
                            transform(RoundedCorners(rounded))
                        }
                    }.override(properties.width, properties.height)
                    .submit(properties.width, properties.height)
                    .get(properties.seconds, TimeUnit.SECONDS)
            if(properties.is2x1Required){
                return resizeImageTO2X1Ration(bitmap)
            }
            return bitmap
        } catch (e: Exception) {
        }
        return null
    }

    private fun resizeImageTO2X1Ration(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null)
            return null
        val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
        if (ratio < 2F) {
            val expectedHeight = bitmap.height
            val expectedWidth = bitmap.height * 2
            val startX = ((expectedWidth - bitmap.width).toFloat() / 2F)
            val endX = startX + bitmap.width
            val resizedBitmap = Bitmap.createBitmap(expectedWidth, expectedHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(resizedBitmap)
            canvas.drawColor(Color.TRANSPARENT)
            val frameToDraw = Rect(0, 0, bitmap.width, bitmap.height)
            val whereToDraw = RectF(startX, 0F,
                    endX, canvas.height.toFloat())
            canvas.drawBitmap(bitmap, frameToDraw, whereToDraw, Paint())
            bitmap.recycle()
            return resizedBitmap
        }else if (ratio > 2F){
            val expectedHeight = bitmap.width.toFloat()/2F
            val expectedWidth = bitmap.width.toFloat()
            val topY = ((expectedHeight - bitmap.height) / 2F)
            val bottomY = topY + bitmap.height
            val resizedBitmap = Bitmap.createBitmap(expectedWidth.toInt(), expectedHeight.toInt(), Bitmap.Config.ARGB_8888)
            val canvas = Canvas(resizedBitmap)
            canvas.drawColor(Color.TRANSPARENT)
            val frameToDraw = Rect(0, 0, bitmap.width, bitmap.height)
            val whereToDraw = RectF(0F, topY, canvas.width.toFloat(), bottomY)
            canvas.drawBitmap(bitmap, frameToDraw, whereToDraw, Paint())
            bitmap.recycle()
            return resizedBitmap
        }
        return bitmap
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


enum class ImageSizeAndTimeout(val width: Int, val height: Int, val seconds: Long, val is2x1Required : Boolean = false) {
    ACTION_BUTTON_ICON(100, 100, 3L),
    BIG_IMAGE(720, 360, 10L, true),
    BIG_IMAGE_GRID(720, 360, 10L),
    PRODUCT_IMAGE(360, 360, 5L),
    CAROUSEL(720, 360, 10L, true),
    VISUAL_COLLAPSED(360, 64, 5L, false),
    VISUAL_EXPANDED(720, 360, 10L, true),
    BANNER_COLLAPSED(180, 64, 5L),
    FREE_ONGKIR(290, 60, 5L),
    STAR_REVIEW(60, 60, 5L)
}
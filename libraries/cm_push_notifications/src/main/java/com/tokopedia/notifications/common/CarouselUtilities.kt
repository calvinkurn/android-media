package com.tokopedia.notifications.common

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import com.tokopedia.notifications.model.Carousel
import java.io.*

/**
 * Created by Ashwani Tyagi on 13/12/18.
 */
object CarouselUtilities {

    private const val IMAGE_DIR = "Tokopedia"
    private const val PNG_QUALITY = 100

    fun downloadImages(context: Context, carouselList: List<Carousel>) {
        for (carousel in carouselList) {
            if (!TextUtils.isEmpty(carousel.filePath)) {
                continue
            }
            val bitmap = CMNotificationUtils.loadBitmapFromUrl(carousel.icon)
            if (null != bitmap) {
                val path = carouselSaveBitmapToInternalStorage(context, bitmap, System.currentTimeMillis().toString())
                carousel.filePath = path
            }
        }

    }

    /**
     * @param context
     * @param bitmapImage
     * @param fileName
     * @return
     */
    private fun carouselSaveBitmapToInternalStorage(context: Context, bitmapImage: Bitmap, fileName: String): String? {
        var fileSaved = false
        val cw = ContextWrapper(context.applicationContext)
        val directory = cw.getDir(IMAGE_DIR, Context.MODE_PRIVATE)
        val imagePath = File(directory, "$fileName.png")

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(imagePath)
            bitmapImage.compress(Bitmap.CompressFormat.PNG, PNG_QUALITY, fos)
            fileSaved = true
        } catch (e: Exception) {
            e.printStackTrace()
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

    /**
     * @param path
     * @return
     */
    fun carouselLoadImageFromStorage(path: String?): Bitmap? {
        var b: Bitmap? = null
        try {
            val f = File(path)
            b = BitmapFactory.decodeStream(FileInputStream(f))
        } catch (e: FileNotFoundException) {
            return null
        }
        return b
    }

    fun deleteCarouselImageDirectory(context: Context) {
        try {
            val cw = ContextWrapper(context.applicationContext)
            val dir = cw.getDir(IMAGE_DIR, Context.MODE_PRIVATE)
            if (dir.isDirectory) {
                val children = dir.list()
                for (i in children.indices) {
                    File(dir, children[i]).delete()
                }
            }
        } catch (e: Exception) {

        }

    }
}

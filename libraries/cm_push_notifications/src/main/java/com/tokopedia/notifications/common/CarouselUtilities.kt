package com.tokopedia.notifications.common

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import com.tokopedia.notifications.model.Carousel
import com.tokopedia.notifications.model.ProductInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*

/**
 * Created by Ashwani Tyagi on 13/12/18.
 */
object CarouselUtilities {

    private const val IMAGE_DIR_CAROUSEL = "Tokopedia_Carousel"
    private const val IMAGE_DIR_PRODUCT = "Tokopedia_Product"
    private const val PNG_QUALITY = 100

    fun downloadCarouselImages(context: Context, carouselList: List<Carousel>) {
        for (carousel in carouselList) {
            if (TextUtils.isEmpty(carousel.icon)) {
                continue
            }
            val bitmap = CMNotificationUtils.loadBitmapFromUrl(carousel.icon)
            if (null != bitmap) {
                val path = saveBitmapToInternalStorage(context, bitmap, IMAGE_DIR_CAROUSEL)
                carousel.filePath = path
            }
        }
    }

    fun downloadProductImages(context: Context, productInfoList: List<ProductInfo>) {
        for (productInfo in productInfoList) {
            if (TextUtils.isEmpty(productInfo.productImage)) {
                continue
            }
            val bitmap = CMNotificationUtils.loadBitmapFromUrl(productInfo.productImage)
            if (null != bitmap) {
                val path = saveBitmapToInternalStorage(context, bitmap, IMAGE_DIR_PRODUCT)
                path?.let {
                    productInfo.productImage = path
                }
            }
        }
    }

    private fun saveBitmapToInternalStorage(context: Context, bitmapImage: Bitmap, dirName: String): String? {
        var fileSaved = false
        val cw = ContextWrapper(context.applicationContext)
        val directory = cw.getDir(dirName, Context.MODE_PRIVATE)
        val imagePath = File(directory, "${System.currentTimeMillis()}.png")

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
    fun loadImageFromStorage(path: String?): Bitmap? {
        if (path == null)
            return null
        var b: Bitmap? = null
        try {
            val f = File(path)
            b = BitmapFactory.decodeStream(FileInputStream(f))
        } catch (e: FileNotFoundException) {
            return null
        }
        return b
    }

    suspend fun deleteCarouselImageDirectory(context: Context) = withContext(Dispatchers.IO) {
        try {
            val cw = ContextWrapper(context.applicationContext)
            val dir = cw.getDir(IMAGE_DIR_CAROUSEL, Context.MODE_PRIVATE)
            if (dir.isDirectory) {
                val children = dir.list()
                for (i in children.indices) {
                    File(dir, children[i]).delete()
                }
            }
        } catch (e: Exception) {

        }

    }

    suspend fun deleteProductImageDirectory(context: Context) = withContext(Dispatchers.IO) {
        try {
            val cw = ContextWrapper(context.applicationContext)
            val dir = cw.getDir(IMAGE_DIR_PRODUCT, Context.MODE_PRIVATE)
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

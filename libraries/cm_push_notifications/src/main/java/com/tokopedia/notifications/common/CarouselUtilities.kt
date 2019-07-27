package com.tokopedia.notifications.common

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.annotation.Nullable
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.tokopedia.notifications.model.Carousel
import com.tokopedia.notifications.model.ProductInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


/**
 * Created by Ashwani Tyagi on 13/12/18.
 */
object CarouselUtilities {

    private const val IMAGE_DIR_CAROUSEL = "Tokopedia_Carousel"
    private const val IMAGE_DIR_PRODUCT = "Tokopedia_Product"
    private const val PNG_QUALITY = 100

    private const val MAX_WIDTH = 720
    private const val MAX_HEIGHT = 720

    fun downloadCarouselImages(context: Context, carouselList: List<Carousel>) {
        for (carousel in carouselList) {
            if (TextUtils.isEmpty(carousel.icon)) {
                continue
            }
            val bitmap = getBitmap(context, carousel.icon)
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
            val bitmap = getBitmap(context, productInfo.productImage)
            if (null != bitmap) {
                val path = saveBitmapToInternalStorage(context, bitmap, IMAGE_DIR_PRODUCT)
                path?.let {
                    productInfo.productImage = path
                }
            }
        }
    }


    private fun getBitmap(context: Context, url: String?): Bitmap? {
        return try {
            Glide.with(context).load(url)
                    .asBitmap()
                    .into(MAX_WIDTH, MAX_HEIGHT)
                    .get(30, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            return null
        } catch (e: ExecutionException) {
            return null
        } catch (e: TimeoutException) {
            return null
        } catch (e: IllegalArgumentException) {
            return null
        }
    }

    @Nullable
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
    @Nullable
    fun loadImageFromStorage(path: String?): Bitmap? {
        if (path == null)
            return null
        try {
            val f = File(path)
            return BitmapFactory.decodeStream(FileInputStream(f))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
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


/*



    private fun getCompressedBitmap(imagePath: String): Bitmap? {
        try {
            var scaledBitmap: Bitmap? = null
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imagePath, options)
            var actualHeight = options.outHeight
            var actualWidth = options.outWidth

            var imgRatio = actualWidth.toFloat() / actualHeight.toFloat()
            val maxRatio = MAX_WIDTH / MAX_HEIGHT

            if (actualHeight > MAX_HEIGHT || actualWidth > MAX_WIDTH) {
                when {
                    imgRatio < maxRatio -> {
                        imgRatio = (MAX_HEIGHT / actualHeight).toFloat()
                        actualWidth = (imgRatio * actualWidth).toInt()
                        actualHeight = MAX_HEIGHT as Int
                    }
                    imgRatio > maxRatio -> {
                        imgRatio = (MAX_WIDTH / actualWidth).toFloat()
                        actualHeight = (imgRatio * actualHeight).toInt()
                        actualWidth = MAX_WIDTH as Int
                    }
                    else -> {
                        actualHeight = MAX_HEIGHT as Int
                        actualWidth = MAX_WIDTH as Int

                    }
                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
            options.inJustDecodeBounds = false
            options.inDither = false
            options.inPurgeable = true
            options.inInputShareable = true
            options.inTempStorage = ByteArray(16 * 1024)
            var bmp: Bitmap
            try {
                bmp = BitmapFactory.decodeFile(imagePath, options)
            } catch (exception: OutOfMemoryError) {
                return null

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565)
            } catch (exception: OutOfMemoryError) {
                return null
            }


            val ratioX = actualWidth / options.outWidth.toFloat()
            val ratioY = actualHeight / options.outHeight.toFloat()
            val middleX = actualWidth / 2.0f
            val middleY = actualHeight / 2.0f

            val scaleMatrix = Matrix()
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

            val canvas = Canvas(scaledBitmap)
            canvas.matrix = scaleMatrix
            canvas.drawBitmap(bmp, middleX - bmp!!.width / 2,
                    middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))

            bmp.recycle()

            val exif = ExifInterface(imagePath)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            matrix.postRotate(readExifOrientation(orientation))
            return Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap!!.width, scaledBitmap.height, matrix, true)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
        }
        return null

    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }

        return inSampleSize
    }

    internal fun readExifOrientation(exifOrientation: Int): Float {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_NORMAL, ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> 0F
            ExifInterface.ORIENTATION_ROTATE_180, ExifInterface.ORIENTATION_FLIP_VERTICAL -> 180F
            ExifInterface.ORIENTATION_ROTATE_90, ExifInterface.ORIENTATION_TRANSPOSE -> 90F
            ExifInterface.ORIENTATION_ROTATE_270, ExifInterface.ORIENTATION_TRANSVERSE -> 270F
            else -> 0F
        }
    }
*/
